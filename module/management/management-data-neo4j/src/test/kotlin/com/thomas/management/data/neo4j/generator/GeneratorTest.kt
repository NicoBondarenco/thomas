package com.thomas.management.data.neo4j.generator

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY
import com.fasterxml.jackson.annotation.PropertyAccessor.ALL
import com.fasterxml.jackson.core.JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_INVALID_SUBTYPE
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE
import com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature.NullIsSameAsDefault
import com.fasterxml.jackson.module.kotlin.KotlinFeature.NullToEmptyCollection
import com.fasterxml.jackson.module.kotlin.KotlinFeature.NullToEmptyMap
import com.fasterxml.jackson.module.kotlin.KotlinFeature.SingletonSupport
import com.fasterxml.jackson.module.kotlin.KotlinFeature.StrictNullChecks
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.thomas.core.extension.onlyNumbers
import com.thomas.core.extension.unaccentedLower
import com.thomas.core.model.general.Gender
import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.core.util.DateUtils.randomZonedDateTime
import com.thomas.core.util.NumberUtils.randomInteger
import com.thomas.core.util.NumberUtils.randomLong
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE
import java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME
import java.util.UUID
import java.util.UUID.randomUUID
import org.junit.jupiter.api.Test

class GeneratorTest {

    private val formatter = ISO_ZONED_DATE_TIME
    private val startZonedDateTime: ZonedDateTime = ZonedDateTime.parse("2025-01-01T00:00:00.000000Z", formatter)
    private val endZonedDateTime: ZonedDateTime = ZonedDateTime.parse("2025-12-31T23:59:59.999999Z", formatter)

    private fun generateBirthday(): String = LocalDate.of(2025, 1, 1).let {
        ISO_LOCAL_DATE.format(it.minusYears(randomLong(18, 45)).plusDays(randomLong(0, 365)))
    }

    private val mapper: ObjectMapper = ObjectMapper()
        .registerModule(
            KotlinModule.Builder()
                .withReflectionCacheSize(512)
                .configure(NullToEmptyCollection, false)
                .configure(NullToEmptyMap, false)
                .configure(NullIsSameAsDefault, false)
                .configure(SingletonSupport, false)
                .configure(StrictNullChecks, false)
                .build()
        )
        .registerModule(JavaTimeModule())
        .setPropertyNamingStrategy(SNAKE_CASE)
        .enable(WRITE_BIGDECIMAL_AS_PLAIN)
        .disable(FAIL_ON_UNKNOWN_PROPERTIES)
        .disable(FAIL_ON_INVALID_SUBTYPE)
        .disable(WRITE_DATES_AS_TIMESTAMPS)
        .disable(FAIL_ON_EMPTY_BEANS)
        .setVisibility(ALL, ANY)

    @Test
    fun `Generate organization`() {
        val content = this::class.java.classLoader.getResourceAsStream("data.json").bufferedReader().readText()
        val companies = mapper.readValue<List<CompanyJson>>(content)
        val cyphers = mutableListOf<String>()
        val entities = mutableListOf<String>()
        companies.forEach { company ->
            val id = UUID.randomUUID()
            val maximumUsers = randomInteger(10, 20)
            val maximumUnits = randomInteger(10, 20)
            val addressNumber = randomInteger(10, 3000)
            val isActive = listOf(true, false).random().toString()
            val date = randomZonedDateTime(startZonedDateTime, endZonedDateTime)
            val createdAt = formatter.format(date)
            val updatedAt = formatter.format(date.plusNanos(randomLong(1_000_000_000, 9_999_999_999_999)))
            cyphers.add(
                "CREATE (e:Organization {" +
                        "id: \"$id\", " +
                        "organization_name: \"${company.razaoSocial}\", " +
                        "fantasy_name: \"${company.nomeFantasia}\", " +
                        "registration_number: \"${company.cnpj}\", " +
                        "maximum_users: $maximumUsers, " +
                        "maximum_units: $maximumUnits, " +
                        "main_email: \"${company.email}\", " +
                        "main_phone: \"${company.celular.onlyNumbers()}\", " +
                        "address_zipcode: \"${company.cep.onlyNumbers()}\", " +
                        "address_street: \"${company.endereco}\", " +
                        "address_number: \"$addressNumber\", " +
                        "address_complement: null, " +
                        "address_neighborhood: \"${company.bairro}\", " +
                        "address_city: \"${company.cidade}\", " +
                        "address_state: \"${company.estado}\", " +
                        "is_active: $isActive, " +
                        "created_at: datetime(\"$createdAt\"), " +
                        "updated_at: datetime(\"$updatedAt\")})"
            )
            entities.add(
                "OrganizationEntity(" +
                        "id = UUID.fromString(\"$id\")," +
                        "organizationName = \"${company.razaoSocial}\"," +
                        "fantasyName = \"${company.nomeFantasia}\"," +
                        "registrationNumber = \"${company.cnpj}\"," +
                        "maximumUsers = $maximumUsers," +
                        "maximumUnits = $maximumUnits," +
                        "mainEmail = \"${company.email}\"," +
                        "mainPhone = \"${company.celular.onlyNumbers()}\"," +
                        "addressZipcode = \"${company.cep.onlyNumbers()}\"," +
                        "addressStreet = \"${company.endereco}\"," +
                        "addressNumber = \"$addressNumber\"," +
                        "addressComplement = null," +
                        "addressNeighborhood = \"${company.bairro}\"," +
                        "addressCity = \"${company.cidade}\"," +
                        "addressState = ${company.estado}," +
                        "isActive = $isActive," +
                        "createdAt = OffsetDateTime.parse(\"$createdAt\")," +
                        "updatedAt = OffsetDateTime.parse(\"$updatedAt\")" +
                        "),"
            )
        }
        println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------")
        cyphers.forEach { println(it) }
        println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------")
        entities.forEach { println(it) }
        println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------")
    }

    @Test
    fun `Generate Group`() {
        val groups = mutableListOf<String>()
        val groupUnits = mutableListOf<String>()
        mapOf(
            "69743b95-5c49-4286-adb4-f02c10143de4" to listOf(
                "69743b95-5c49-4286-adb4-f02c10143de4",
                "744a0082-037a-4722-8730-60da67d2eea4",
                "ed45dad6-8afc-49f7-9215-ce1de2849f74",
                "56dd7a7b-6c92-45e4-a0c0-08cdb0b89eb7",
                "fac41743-7871-47e6-bbc5-e66e8a2ed6b6",
                "8821cf99-c127-4051-99a5-93ed4ae61d37",
                "a83654cb-63ea-4859-81f6-8948539d5d3c",
                "1ca12768-b26b-415a-acbc-bbc53ebbd457",
                "3c4b206c-b244-4131-907d-46726e879d33",
                "ee0ef1d1-b6f6-4b9b-880c-77ace23ce0bb",
                "8ed0244a-55c5-4a0c-9cb3-9629932a548a",
                "5b7365a1-b021-4f4f-92e2-6f2b2f86615a",
                "a69c3b5a-9238-42da-9184-0cd647fbfcf2",
                "ece0191e-80cf-406f-9e79-b70cb570b277",
                "651b3d3b-6617-4bd7-8a03-1e2ef7cbeb12",
                "775b9b70-c216-4c09-9cad-5692d97faa0d",
                "a94f4d6b-31b7-4c93-98e0-8bfdacd8c87e",
                "17c26451-2cad-46a7-a2e5-0620debb2206",
                "23ace064-239d-4785-a71c-da3a8910521d",
                "6e7be1dc-9510-40f9-ad91-0ea15dd73eaa",
            ),
            "744a0082-037a-4722-8730-60da67d2eea4" to listOf(
                "4445d37b-a36a-49c7-8180-2d95bb5f186d",
                "1d9d0f4a-32d0-4d0a-aa0f-b4f35cf0166d",
                "7e9a0397-8108-44ff-b586-6377148543eb",
                "83aeebcc-d5d4-41b2-a566-5f88ca8394b1",
                "a405d7f5-9c0e-46b9-a22d-07c5a6472a65",
                "4f244794-a4c7-4da7-bdd9-dd5bc5dc191c",
                "3aba9b5b-143a-4888-b81e-073d3938b5e2",
                "b5ec0c45-2808-4d17-a001-fb3708a099ab",
                "a2f7b371-b9e1-4fda-a2a0-82f5eea488c5",
                "ab53f98b-0afc-4bb0-b1e8-b25f9dcefbde",
                "567f1073-714a-4d55-856e-44ce43db9cdc",
                "427d884f-2821-40eb-af39-6eca6d7bd3c2",
                "87700ea6-6b18-4fe4-946e-a7f60875ec9e",
                "041e9b76-a676-456f-966d-7ce31a24acf4",
                "902e0a68-a9ec-48a8-9667-40a897d548c8",
                "b481dbc4-ff89-4662-8f9f-3e92ebb392dc",
                "701b71f6-aa58-4983-a8e2-7d37bbfa1b46",
                "dcb63236-6257-4830-98bc-d1ef651686c1",
                "3ff99e1f-bade-466f-a3fe-7264c42531d9",
                "ae1a7be3-45ec-47fd-8d24-57c27d65b7c7",
            ),
            "ed45dad6-8afc-49f7-9215-ce1de2849f74" to listOf(
                "6d96baef-7daa-4ae6-b8c1-eedbd5e6c55a",
                "115d1ca2-86cf-4d80-8896-ab7793cc86d3",
                "73aa358f-071c-41c5-bd3c-56fd2876983c",
                "ae5304e2-8cb3-4728-8a3a-4563a631f246",
                "43894332-0d3d-4015-8eba-04da2a4baf23",
                "7bdde3a4-1e21-400b-aace-c5b9acb40e77",
                "1b789fdf-dc4d-41bb-a8f9-ab6dd90b1748",
                "3878a7d7-4d7b-49d6-9892-4dbd130deef3",
                "a0580b7f-d4b2-4b03-8f1e-12d53d42bf58",
                "85f200cd-aaf3-4509-9e2d-ebce50599856",
                "97125ae8-1066-4aed-992e-c2a99044e7aa",
                "13bba0ba-02c8-4b42-b1ac-26a3bdda479a",
                "b76cd2ce-c78b-4ec7-9b6d-550a4f10d62e",
                "fc07e595-a23c-4233-a272-f11ab9ef5ef9",
                "d37165b4-e571-4fc6-80b6-22f8472f96e4",
                "19545d7a-647d-4b35-a553-9eed66b4dfdf",
                "e9db8754-33e0-431a-b881-ad81c880687f",
                "62f4e710-f784-4dcf-81dd-a1c62f24add5",
                "dcba09c5-fa34-4bfd-902d-1ef741137b2f",
                "4c96a390-684f-42be-87df-63dfb9526a15",
            ),
        ).forEach { (organizationId, units) ->
            mapOf(
                "Administradores de Banco de Dados" to "Grupo de Gestão dos Bancos de dados dos sistemas",
                "Administração" to "Grupo do departamento de administração",
                "Arquitetos de sistema" to "Grupo dos arquitetos de sistemas informatizados",
                "Atendimento ao Cliente" to "S.A.C. do cliente",
                "Atendimento ao Colaborador" to "Grupo de apoio ao Colaborador",
                "Avaliadores de Processo" to "Avaliação dos Processos Executados",
                "Bioquímica" to "Departamento de bioquímica",
                "Bioquímica Avançada" to "Departamento de bioquímica avançada",
                "Cobranças" to "Área de Cobranças de Pagamentos",
                "Compras" to "Departamento de compras externas",
                "Contábil" to "Setor contábil",
                "Contábil e Fiscal" to "Setor de Contabilidade Fiscal",
                "Departamento de Química Avançada" to "Setor responsável por gerir produtos químicos avançados",
                "Desenvolvedores de sistema" to "Programadores do sistema",
                "Desenvolvimento Químico" to "Desenvolvimento de produtos químicos",
                "Desenvolvimento de produtos" to "Desenvolvedores de produtos internos e externos",
                "Diretoria" to "Diretores da Empresa",
                "Distribuição" to "Setor responsável pela distribuição dos produtos",
                "Enfermaria" to "Departamento de enfermaria e primeiros socorros",
                "Execução Orçamentária" to "Execução dos Orçamentos Aprovados",
                "Expedição" to "Setor responsável por emitir a expedição de produtos",
                "Experiência de usuário" to "Setor responsável pela gestão experiência do usuário final",
                "Fabricação" to "Setor de fabricação de produtos",
                "Financeiro" to "Departamento dos gestores financeiros da empresa",
                "Frota" to "Departamento de gestão da frota de veículos",
                "Funcionários" to "Grupo Geral dos Funcionários",
                "Gerentes Internacionais" to "Gerentes de Gestão de territórios exteriores",
                "Gerentes Nacionais" to "Gerentes de Gestão do território nacional",
                "Gerentes Regionais Centro-Oeste" to "Gerentes de Gestão da Região Centro-Oeste",
                "Gerentes Regionais Nordeste" to "Gerentes de Gestão da Região Nordeste",
                "Gerentes Regionais Norte" to "Gerentes de Gestão da Região Norte",
                "Gerentes Regionais Sudeste" to "Gerentes de Gestão da Região Sudeste",
                "Gerentes Regionais Sul" to "Gerentes de Gestão da Região Sul",
                "Gestores Comerciais" to "Gestores de Pessoas do Comercial",
                "Gestores Tecnógicos" to "Gestores de Pessoas de TI",
                "Gestores de Vendas" to "Gestores de Pessoas dos Vendedores",
                "Gestão Ambiental" to "Gestão Ambiental dos Recursos",
                "Gestão da Qualidade" to "Gestão da qualidade da organização",
                "Gestão de Recursos Externos" to "Gestão dos Recusos Usados Externamente",
                "Gestão de Recursos Internos" to "Gestão dos Recusos Usados Internamente",
                "Gestão de Redes Sociais" to "Gestão de todas redes sociais",
                "Gestão de Resíduos Orgânicos" to "Departamento de gestão de resíduos orgânicos gerados",
                "Gestão de Resíduos Químicos" to "Departamento de gestão de resíduos químicos gerados",
                "Gestão de Resíduos Sólidos" to "Departamento de gestão de resíduos sólidos gerados",
                "Importação de Ferramentas e Máquinas" to "Setor responsável pelas importações de ferramentas e maquinários",
                "Importação de Matéria Prima" to "Setor responsável pelas importações de matéria prima",
                "Importação de Produtos" to "Setor responsável pelas importações de produtos",
                "Infraestrutura Predial" to "Setor responsável por manter as condições prediais",
                "Infraestrutura de Bancos de Dados" to "Setor responsável pela gestão dos bancos de dados",
                "Infraestrutura de Rede" to "Setor responsável pela internet e intranet",
                "Infraestrutura de Servidores" to "Setor responsável pela manutenção dos servidores",
                "Jurídico" to "Setor legal e jurídico",
                "Logística" to "Departamento de logística",
                "Líderes Técnicos" to "Líderes técnicos dos setores de técnologia",
                "Manutenção" to "Departamento de manutenções",
                "Marketing" to "Grupo de marketing",
                "Marketing para Mídias de Rádio e TV" to "Grupo de marketing para mídias de rádio e tv",
                "Marketing para Redes Sociais" to "Grupo de marketing para redes sociais",
                "Operações Especiais" to "Gestão de Operações Especiais da Empresa",
                "Operações Externas" to "Gestão de Operações Externas da Empresa",
                "Operações Internas" to "Gestão de Operações Internas da Empresa",
                "Pesquisa" to "Setor de pesquisas empresariais",
                "Pesquisa de Mercado" to "Setor de Pesquisa de Mercado",
                "Pesquisa de Novos Produtos" to "Setor de Pesquisa de Novos Produtos",
                "Pesquisa de Satisfação" to "Setor de Pesquisa de Satisfação do Cliente",
                "Pesquisas Técnológicas" to "Setor de Pesquisa de Novas Técnologias",
                "Processamento de Matéria Prima" to "Grupo de permissões de gestão de processamento de matéria prima",
                "Processamento de Pedidos" to "Grupo de permissões de gestão de processamento de pedidos",
                "Processamento de Produtos" to "Grupo de permissões de gestão de processamento de produtos",
                "Produção" to "Produção de items da empresa",
                "Qualidade de produto" to "Departamento para análise e qualidade do produto",
                "Química" to "Setor de manuseio de produtos químicos",
                "Recursos Humanos" to "Setor dos recursos humanos",
                "Relações Públicas" to "Setor de Gestão da Relação da Empresa com o Público",
                "Segurança do Trabalho" to "Departamento de gestão da segurança do trabalho",
                "Setor Comercial" to "Setor Geral Comercial",
                "Setor Operacional" to "Setor de Operações da Empresa",
                "Setor de Segurança" to "Setor de Acessos e Monitoria",
                "Sistema de Atendimento ao Consumidor" to "S.A.C. da empresa",
                "Suporte Interno" to "Departamento de suporte interno aos colaboradores",
                "Suprimentos" to "Departamento de compras internas",
                "Tecnologia da Informação" to "Departamento de tecnologia da informação e afins",
                "Vendas" to "Setor de venda da produção da empresa",
            ).forEach { (name, description) ->
                val groupId = randomUUID()
                val date = randomZonedDateTime(startZonedDateTime, endZonedDateTime)
                val isActive = listOf(true, true, false).random().toString()
                val createdAt = formatter.format(date)
                val updatedAt = formatter.format(date.plusNanos(randomLong(1_000_000_000, 9_999_999_999_999)))
                val roles = SecurityOrganizationRole.entries.shuffled()
                    .take(randomInteger(0, SecurityOrganizationRole.entries.size))
                    .joinToString(",") { "\"$it\"" }

                val roleUnits = listOf(0, 0, 1, 2, 3).random().let { quantity ->
                    units.shuffled().take(quantity)
                }.map { unit ->
                    unit to SecurityUnitRole.entries
                        .take(randomInteger(0, SecurityUnitRole.entries.size))
                        .joinToString(",") { "\"$it\"" }
                }

                groups.add(
                    "MATCH (o:Organization {" +
                            "id: \"$organizationId\"" +
                            "}) " +
                            "CREATE(g:Group {" +
                            "id: \"$groupId\", " +
                            "group_name: \"$name\", " +
                            "group_description: \"$description\", " +
                            "is_active: $isActive, " +
                            "created_at: datetime(\"$createdAt\"), " +
                            "updated_at: datetime(\"$updatedAt\")})" +
                            "-[:GROUP_BELONGS_TO_ORGANIZATION {" +
                            "id: \"$groupId-$organizationId\", " +
                            "group_id: \"$groupId\", " +
                            "organization_id: \"$organizationId\", " +
                            "group_roles: [$roles]" +
                            "} ]->(o);"
                )

                roleUnits.forEach { roleUnit ->
                    groupUnits.add(
                        "MATCH (u:Unit {" +
                                "id: \"${roleUnit.first}\"}" +
                                ") " +
                                "MATCH (g:Group {" +
                                "id: \"$groupId\"}" +
                                ") " +
                                "CREATE (g)-[:GROUP_ALLOWED_IN_UNIT {" +
                                "id: \"$groupId-${roleUnit.first}\", " +
                                "group_id: \"$groupId\", " +
                                "unit_id: \"${roleUnit.first}\", " +
                                "group_roles: [${roleUnit.second}]" +
                                "} ]->(u);"
                    )
                }
            }
            groups.add("")
            groupUnits.add("")
        }

        groups.forEach(::println)
        groupUnits.forEach(::println)
    }

    @Test
    fun `Generate User`() {
        val users = mutableListOf<String>()
        val usersUnits = mutableListOf<String>()
        val usersGroups = mutableListOf<String>()

        val organizationGroups = mapOf(
            "69743b95-5c49-4286-adb4-f02c10143de4" to listOf(
                "101c1508-bd0e-4d50-bd63-65493c535bf2",
                "38c17097-83c1-494e-a0a8-7c5b8527a03c",
                "31d2c198-96e2-45cf-962c-3de611e65c06",
                "f81255ca-cdd9-4d91-86f5-f379299a13f9",
                "58532c1d-ab89-45bf-b409-2d785c05ed55",
                "cb66194b-c9bc-42a9-800c-0aa87d9825ee",
                "dd84f6ba-45e1-4d06-bafe-4a915d56c334",
                "80fc344f-12b7-4fe5-98ce-03e5bb4135f3",
                "d8325b35-03be-423d-9d46-5998c5265acf",
                "6a901b0a-05a5-4d16-be86-b8811cc932e5",
                "10917565-1785-4d73-b8a0-af74026c8702",
                "39c60520-a8a0-4081-afa2-b2a85a00e2a0",
                "1fff5c44-e0fb-49b8-9bbe-bb5e9d0dd586",
                "ede5fdb9-f366-4ec2-8026-7958eba810c2",
                "5fabc6d4-ba1e-4472-9a77-3fbe4096daa0",
                "569573e7-2406-4763-bf02-b2c887d49149",
                "44ac374f-047d-493a-928f-9be28f4f5a8d",
                "f6696bb5-d105-4be1-bcf3-fdb0b5e161cc",
                "c1f42552-7d0e-486c-a057-e148e64eda3c",
                "6152f2e7-31a9-46bc-87fb-bf7045bc2fe8",
                "fa14abc0-7fae-4a6b-9ce9-40d9e1748f88",
                "f554251d-f135-4f88-bcf0-352620d78e2f",
                "e8d895d5-cae3-4882-99f8-2dbc4349b371",
                "cdf15a60-d111-45fa-9d9d-34282c4b6200",
                "d68c5b45-ed11-41d2-bb8d-ef310ea713cf",
                "fbaa1d95-bcc5-4172-8db4-2233f91549c6",
                "f3988a25-3c83-4ef0-a3d8-84fd399b7e6c",
                "59b4df46-0a36-461a-be40-693ec34b61b9",
                "530c5c7e-d9aa-4c2f-92c0-b3827930035c",
                "2a617cb6-a633-4d26-a540-255715038c26",
                "90eadc01-67b8-4bee-9594-3915d2220ecf",
                "a58d81b7-7c67-4c45-9cc7-b6e7a11899ef",
                "81e283b0-caa0-4e7e-a70b-36777ce56958",
                "42f32387-eb36-4368-9439-5c15873aea31",
                "57aa62a7-5aec-4456-9df2-c75695ac83f8",
                "58b69b46-7728-48fb-933d-35b32ff63a7a",
                "8d02007c-9249-4678-8eda-d2730e0e96bf",
                "f7d5544b-c964-4ccb-ba6b-5b66842b0c1a",
                "b994c000-4337-4d41-9817-fecdfbce1050",
                "c1809251-2214-46cc-b831-088470277d4b",
                "0f954c0d-9953-4b4b-b5ee-bad5594a3171",
                "22490228-2bd5-44cf-b8a2-9db71fdac3d8",
                "f35b107d-5585-4148-adaa-708e7718ef36",
                "36135370-f8dc-481e-bbe6-7e1c83b7d801",
                "672c8f87-3f27-498d-b013-2767174c4b8b",
                "fa6a768e-a198-4b8b-a554-9511c4aa5298",
                "402184a4-3e80-4291-a6e1-761258478d79",
                "60468eb9-3c23-48f1-86a0-d8a9fdf74f54",
                "d8f3aecd-31a6-4e98-a30f-7c5cfec0a413",
                "aae8d859-544f-4949-887b-7101b73fc4a8",
                "e6b3a7de-74bd-462d-b702-4fbc23afa9b1",
                "b2747d66-2a32-4a5e-8ee9-8b2be462a750",
                "e9c34299-1d52-48c0-a020-e46866a369c2",
                "c716c0d0-4f19-4ab1-9e06-b593200a2dba",
                "63a472ab-0a4a-48c3-816c-92a4a589a73d",
                "cae10022-1eec-4e2a-b14a-2a4c51a52b24",
                "e019e761-58b4-4329-adcd-3d427cde9c07",
                "b6b62b6b-fbe8-401f-bc8f-7b17a42f085f",
                "7ec3dc90-3073-480d-af95-327c668b3071",
                "9fe093f0-27d3-4cd5-a7dd-31f0586bc41d",
                "8e8e0fd6-0027-48c2-aac8-ac750e459433",
                "a8afe683-4f46-45af-8b2a-6fbb9bfb89f6",
                "f8f59635-88a8-4132-aa12-03be53d4085d",
                "c70424c0-63e9-499e-bb71-a5c3ae1865ea",
                "05530819-9ea6-451f-895b-d2748ae9e8af",
                "d2facf12-f23d-4c67-b1c6-5f2354a9c79a",
                "dd0d28e4-a57f-4e7b-bd5b-2f55a023c3a9",
                "b669b91d-fbd0-4643-bd56-22c903d753e2",
                "d0992a7f-4081-46df-b8c7-f4815ff035a1",
                "7e12204e-b08e-4095-bac1-a39d2b30278f",
                "4ddacb8b-3e4e-4b9d-837a-da5c96ee5b24",
                "4a24f9fb-dd06-4231-95b8-b6c26bf24f95",
                "43ae9a88-6717-4517-8519-ff499b9c32db",
                "4a3c9975-c7f7-4e28-aaa8-17a234eb05c9",
                "d4ef1c4f-0d8f-405e-a509-f6ec9cbbb7cb",
                "2ed7f030-26b8-461f-b979-eae2f78b84f7",
                "2ba44f21-af2e-47cb-a540-2d107db9ca01",
                "ab11333d-5d1d-4ef4-a5c1-225176554e2b",
                "33c629a7-1ac4-4be3-9424-3e0871df989f",
                "e9b5bccf-82e0-4687-8ea9-9fa3d8135cb3",
                "7a50d4d4-2c22-40e3-934d-0331b0be0080",
                "0f574257-b252-469d-8fb1-1a099e9ef848",
                "ec75a7e3-6ba6-4d8a-95e2-43ca80dd552d",
            ),
            "744a0082-037a-4722-8730-60da67d2eea4" to listOf(
                "abdf5ed0-99f5-45b5-a784-059d6d19a59c",
                "955de262-a424-4b58-8644-c7e2d8b35da8",
                "82c09acf-f3a4-4f57-afa5-34754805d276",
                "da0b50dd-1aeb-4046-b980-6fbeca82bf95",
                "11f25940-82ca-4d4b-8eea-21eaba3ada12",
                "37a76f53-ef86-4f15-bbaf-97186c7fc915",
                "5fe6eaf9-a9dc-485c-b29e-2e95daa21b7b",
                "c7d077ae-7f87-4fab-b7c0-3861f14c34cf",
                "5569b5aa-db6e-4a83-b0d8-a75d60751ee8",
                "a039b08d-c531-45bc-af78-fd1d5ff65355",
                "c155eac9-f274-4cf1-85d1-9bb3ee0ae061",
                "080c0142-1003-474b-a2b1-651abbb4f1e3",
                "0a77b92c-f889-40d7-86eb-8cfdf40ecbe9",
                "b08ea51a-2e2f-44ad-b343-f573262e807b",
                "c6220133-48ee-4476-80f5-e263eb873961",
                "57ef21c8-205f-4091-a8e3-2d5ab7c02f78",
                "a89d23b9-5aed-4f10-96d5-bd4437973832",
                "307a7137-7f27-4ba1-bee2-58f7ff97185e",
                "d8a4757c-a5b1-423c-8737-0a9b1fc56d72",
                "ee72c41c-d2ec-43fa-802b-6510b34ec01f",
                "4b9083fc-9af0-4c2d-9289-153f667549f5",
                "6b39d9d7-5815-40bc-872b-aeb5b848c57c",
                "ee366ac9-4300-4f49-9628-faad6e2845f8",
                "23f90acf-7642-40a3-8528-960c2b5eebfb",
                "95e69c5d-5eed-4e5d-88c4-9f70ad28c05a",
                "50a5fc53-fb04-43f4-a491-7df62f935764",
                "707a9443-3ac7-4730-9fc9-14ebf0fe67fb",
                "58b531fd-e600-43f8-9d4a-d840a6752634",
                "f5cd6617-1286-4e18-b544-02383d463646",
                "3fcb8974-6332-429c-86f3-57826c94e566",
                "b1c8e7d2-3559-4530-88a0-d00183be9bdc",
                "baab83ff-f408-4c97-82ce-6897c750edf5",
                "c127ea16-5f11-496e-bf23-c476872655d2",
                "ad576d42-2c5a-420b-ad19-145be2d8a318",
                "c60570d2-8de5-46fd-8f4b-9dd631b9446e",
                "cee39d26-370f-40dc-b8d6-fb45e8fc5dd9",
                "b4411721-2de7-459b-a17b-8e61a05c67b1",
                "a6abd8a7-4652-4f0d-b647-8791b4d5b3d7",
                "c04a2e8e-e67c-4277-b219-d95b02666d39",
                "42ca5a11-eec4-4cc0-a53e-e5aa1167ec2f",
                "4032587d-afa3-439e-b1cd-4f2928076888",
                "b6d24481-e824-484c-a9ff-5e2f965ed291",
                "4c11d398-aa4e-4688-af10-8c17628777a5",
                "12046681-c50b-43eb-8fb6-129c4a1e84c7",
                "e079df55-078d-4fea-8452-4f269eb0dffd",
                "df27c983-8336-49b3-af16-e05692a14693",
                "ff73900b-bcd4-44d4-8d50-676b00806a39",
                "18a00780-7937-4395-83c7-07d2d3563379",
                "01135a72-794a-4330-b062-4a469ef2a064",
                "a50be4ac-37f2-4b93-a569-1f3feb266726",
                "43a1b194-7ca1-4024-a3e1-7a08b4c00363",
                "99ea369c-4528-4dd4-9ac0-67ef303a2128",
                "a62438d4-240e-445c-9ed4-f3e6a32f706c",
                "d66a43a2-21ce-4c7e-98d2-3ddf0bd7ee98",
                "d7e9c60e-f66d-45d7-8e14-b6fd65f9e83b",
                "e8ec59c1-36ca-4475-9cb8-901d457c4d19",
                "239b6e4f-ecb2-4444-8762-e41ed9467e2b",
                "4c788213-fda9-4877-b78f-572db13fbab5",
                "d7a3d81a-0ce1-402a-86f9-c08bf95045c9",
                "8b66afbe-c521-4bfb-8d86-7b13dd25abc6",
                "b5cfd172-a0ff-4d84-a117-f96b394fcaea",
                "25eafc6d-e9d2-46a0-899c-5f9cc922b08e",
                "da181d66-d1f4-4fe8-b080-d244d1369c77",
                "139ab838-2d6d-4361-8783-68b6d0ec0667",
                "77bd6e0b-4f3c-4e74-b06e-5358afc8cba5",
                "cb20dc59-e37c-4d42-b666-0420ac15ac3d",
                "78f2a52b-8343-441b-9315-404112d9a80d",
                "e5846ecb-b4f8-455f-af05-55af8094df15",
                "f86626ec-0070-4052-94c7-7defc2d8fcc4",
                "45c16f79-38ca-4ee0-a6ce-7c4a10edbd31",
                "9c1b607e-4f9c-40ec-b75f-9dae21715416",
                "21993f69-301d-499f-a5e0-6bad83c07823",
                "e050be70-eb76-4e35-9a15-92dbf0176ef1",
                "ea937966-13e8-44bb-b723-fe4100d444ec",
                "76409106-a50c-46fb-b38a-1e73feeba0a8",
                "fd82b2a8-804b-40ed-b302-eed0f18fd673",
                "273721f8-5b45-4482-8e9f-37060b4cc94b",
                "1284199e-e4cd-46a3-9505-a19ddfb4ca35",
                "8ae03b32-66cb-4189-98f1-af8742cf8f0d",
                "f604cd35-3089-4b65-9000-c21ff0a8c858",
                "a16e7b97-7524-48e5-91dc-57661e62589d",
                "c098dc2f-a425-4657-998a-5e10aca22f4f",
                "16a3afe2-ec23-4b81-b39b-dbeb6a1fc04b",
            ),
            "ed45dad6-8afc-49f7-9215-ce1de2849f74" to listOf(
                "ce6918a9-9da7-4435-80d7-7ecf07d5a5ba",
                "e964441c-1e5e-456f-90e7-afbb1621b6ad",
                "4ab67f58-d7b0-4f9b-ae0c-8d7c53e5b1ca",
                "71a9847a-ef84-48da-9e99-c7e747bddb19",
                "58ad621c-9111-4deb-9d4d-717f7bb81f50",
                "a8edbc94-0afa-47c7-bd04-264b60fb2328",
                "b8d9bcd9-b774-4310-9d77-e1da1b82396f",
                "191d2ac5-da76-4eed-ad4d-f4679ab5fb92",
                "ef1605f2-bb6a-4fc9-a6f1-6190ff7775b0",
                "e4c1c14e-c1f1-46ea-b6ba-0b6a88134078",
                "7f9534da-e2a3-46ba-af3d-b3fe88e8b9dd",
                "a04d1bed-e97f-4ce2-942c-a9c1e1aa5807",
                "959a14d5-3898-4017-8f5e-e29baf005df4",
                "f07b2465-dfef-493e-bd84-a18e01a749d6",
                "2b162ca0-d3b2-4dd8-a522-9495080a9588",
                "c8468095-4a5c-437f-9418-656e82f99ed8",
                "0b931f84-54d2-4c64-8dc4-7fc61ca7ab18",
                "34bd8ff3-9741-4e72-a3a7-b82a6bbc9c5f",
                "d6ff34d0-2b4f-49c7-a5ae-0b4293bcc249",
                "6a4529b5-b3f4-415f-ae20-952e57888519",
                "0eadd96b-25ae-486d-9d06-8cb209422e3d",
                "cdef7427-3179-4f2a-b610-89a65122681e",
                "947ecec1-296c-4553-9aef-fe8db9dd7f7f",
                "fa0dc10b-ef4b-4824-a538-b54a1a55560b",
                "41c146d9-b60d-401a-ba9c-2244ddf78766",
                "f05e7975-54ec-4f09-aad5-675fd4cd2511",
                "9f303291-0834-4e5e-af81-051fb1291232",
                "f5711619-da7f-4e29-8813-9539765d19f0",
                "faadf8fa-68a2-4c89-a2be-0b64aca8296a",
                "03abd1d0-dd51-4a81-944b-d90e2536e5cd",
                "a3e03893-772b-4bf9-b240-162715105917",
                "0b880b88-9507-4bb2-9613-0ad259378f29",
                "e9141492-fa95-4a67-b7f6-5427e3d5ae7d",
                "728f74b2-a79f-4f90-a08e-10767b505e6d",
                "97deb79a-e45d-4886-9813-c894d8526c3b",
                "e4a5f8b0-e0bd-42dd-bb43-9fe01ad01327",
                "96187631-6da2-4070-ba93-4d4c4fffd743",
                "f2dab2fc-c2b5-41f8-a61a-946294572a78",
                "697f314b-0fc6-489d-9bb7-3ac1b84c11b6",
                "f8b3f3bb-2875-441e-9191-ecb3d3d3ef7d",
                "da7b2a2a-b69d-4c20-ab8e-5e143fa0f03f",
                "84e67418-c3f4-4bab-8a88-a6d4d7066ced",
                "c0b02aa9-3412-42ff-92d4-782cc47f8598",
                "b7679da8-9824-4986-a5bf-0398981f0f27",
                "df9379a4-7a8c-4619-811d-74d72c2ef73e",
                "32aab78b-9714-4c90-9566-fc5e5d4273ea",
                "23ce5fc2-4871-4581-b8b3-e4349c23cd6f",
                "6bfdd23c-6731-4381-b4c9-d7ff306fedb5",
                "69f691d3-6354-4e13-a146-fd5d99b62f35",
                "628cb7d3-46d9-405f-8c1c-ba7d8e313e4b",
                "3178f1a3-1cd1-497e-a4d0-5795022fa3a5",
                "b0fe894b-ad53-4434-9e16-7a2a379d7208",
                "37001a29-cbfd-40ff-9c29-c06af59b9a7c",
                "fddc3f02-5103-4483-96c9-93a536c89764",
                "318ffa0c-b757-4dff-b16c-68479e90efac",
                "42a623ae-1cd8-4e96-a8dc-f9c2a6d057f4",
                "8b2d71c0-bf2b-4ba1-a888-1fc84629f255",
                "04c490ea-aff4-40e3-8f27-ad878636c73a",
                "4c23ee62-233b-4a7d-bc02-c393dca7a8b6",
                "dbbcd266-c716-4865-ac6b-4b3a81493614",
                "b4348770-e16e-4af6-9db3-27e6dddffe84",
                "cbc01e3e-af10-4d5b-bef1-d74c35b0dbee",
                "7c0d4c49-f1e2-4cfa-aae8-3518c7025c28",
                "8c6ad196-d8a7-453a-9432-bb4c38f55e09",
                "2aa343f4-e476-42b4-aa51-171ddc49c809",
                "b5bd29fa-61a1-42eb-be95-a560471028c6",
                "d34b4d47-f04e-4d37-a8aa-a7f3ff16e74f",
                "8161dc53-f1c4-42df-a3a6-a9d2300688e7",
                "dd61afbe-96cc-44f1-af53-413b831017d0",
                "64ff108c-48f9-498d-9d0f-dd01a461d6b0",
                "20196f62-6f85-4d09-a19a-06c1ab8c6f39",
                "3cee2815-51b5-4bb5-9ce7-afccfe037b9c",
                "a56a69e9-ec69-46bf-89bc-9e13a75ab6ee",
                "2a7b30c9-ea73-4b76-aa53-34bc6d0cc7ff",
                "04e467e1-917b-4450-905f-3ba57ae332df",
                "8ce72b5a-2275-4ba2-a034-23db2054c95b",
                "4dbc18ef-36a7-450e-af71-7125b482f2c4",
                "984d5e48-a9c5-49ec-a916-5dad41b0a831",
                "2082e7d8-9010-41aa-9cf1-961fcda8b823",
                "5450809f-8528-43ec-987f-f7e0dcc10488",
                "d1d4769c-dab4-4f0b-9169-1f3b0459d19b",
                "732696cb-0904-4c6d-a3dd-08c5b99cedc9",
                "42431e73-1411-4f91-adbc-380cf216ebf4",
            ),
        )

        mapOf(
            "69743b95-5c49-4286-adb4-f02c10143de4" to listOf(
                "69743b95-5c49-4286-adb4-f02c10143de4",
                "744a0082-037a-4722-8730-60da67d2eea4",
                "ed45dad6-8afc-49f7-9215-ce1de2849f74",
                "56dd7a7b-6c92-45e4-a0c0-08cdb0b89eb7",
                "fac41743-7871-47e6-bbc5-e66e8a2ed6b6",
                "8821cf99-c127-4051-99a5-93ed4ae61d37",
                "a83654cb-63ea-4859-81f6-8948539d5d3c",
                "1ca12768-b26b-415a-acbc-bbc53ebbd457",
                "3c4b206c-b244-4131-907d-46726e879d33",
                "ee0ef1d1-b6f6-4b9b-880c-77ace23ce0bb",
                "8ed0244a-55c5-4a0c-9cb3-9629932a548a",
                "5b7365a1-b021-4f4f-92e2-6f2b2f86615a",
                "a69c3b5a-9238-42da-9184-0cd647fbfcf2",
                "ece0191e-80cf-406f-9e79-b70cb570b277",
                "651b3d3b-6617-4bd7-8a03-1e2ef7cbeb12",
                "775b9b70-c216-4c09-9cad-5692d97faa0d",
                "a94f4d6b-31b7-4c93-98e0-8bfdacd8c87e",
                "17c26451-2cad-46a7-a2e5-0620debb2206",
                "23ace064-239d-4785-a71c-da3a8910521d",
                "6e7be1dc-9510-40f9-ad91-0ea15dd73eaa",
            ),
            "744a0082-037a-4722-8730-60da67d2eea4" to listOf(
                "4445d37b-a36a-49c7-8180-2d95bb5f186d",
                "1d9d0f4a-32d0-4d0a-aa0f-b4f35cf0166d",
                "7e9a0397-8108-44ff-b586-6377148543eb",
                "83aeebcc-d5d4-41b2-a566-5f88ca8394b1",
                "a405d7f5-9c0e-46b9-a22d-07c5a6472a65",
                "4f244794-a4c7-4da7-bdd9-dd5bc5dc191c",
                "3aba9b5b-143a-4888-b81e-073d3938b5e2",
                "b5ec0c45-2808-4d17-a001-fb3708a099ab",
                "a2f7b371-b9e1-4fda-a2a0-82f5eea488c5",
                "ab53f98b-0afc-4bb0-b1e8-b25f9dcefbde",
                "567f1073-714a-4d55-856e-44ce43db9cdc",
                "427d884f-2821-40eb-af39-6eca6d7bd3c2",
                "87700ea6-6b18-4fe4-946e-a7f60875ec9e",
                "041e9b76-a676-456f-966d-7ce31a24acf4",
                "902e0a68-a9ec-48a8-9667-40a897d548c8",
                "b481dbc4-ff89-4662-8f9f-3e92ebb392dc",
                "701b71f6-aa58-4983-a8e2-7d37bbfa1b46",
                "dcb63236-6257-4830-98bc-d1ef651686c1",
                "3ff99e1f-bade-466f-a3fe-7264c42531d9",
                "ae1a7be3-45ec-47fd-8d24-57c27d65b7c7",
            ),
            "ed45dad6-8afc-49f7-9215-ce1de2849f74" to listOf(
                "6d96baef-7daa-4ae6-b8c1-eedbd5e6c55a",
                "115d1ca2-86cf-4d80-8896-ab7793cc86d3",
                "73aa358f-071c-41c5-bd3c-56fd2876983c",
                "ae5304e2-8cb3-4728-8a3a-4563a631f246",
                "43894332-0d3d-4015-8eba-04da2a4baf23",
                "7bdde3a4-1e21-400b-aace-c5b9acb40e77",
                "1b789fdf-dc4d-41bb-a8f9-ab6dd90b1748",
                "3878a7d7-4d7b-49d6-9892-4dbd130deef3",
                "a0580b7f-d4b2-4b03-8f1e-12d53d42bf58",
                "85f200cd-aaf3-4509-9e2d-ebce50599856",
                "97125ae8-1066-4aed-992e-c2a99044e7aa",
                "13bba0ba-02c8-4b42-b1ac-26a3bdda479a",
                "b76cd2ce-c78b-4ec7-9b6d-550a4f10d62e",
                "fc07e595-a23c-4233-a272-f11ab9ef5ef9",
                "d37165b4-e571-4fc6-80b6-22f8472f96e4",
                "19545d7a-647d-4b35-a553-9eed66b4dfdf",
                "e9db8754-33e0-431a-b881-ad81c880687f",
                "62f4e710-f784-4dcf-81dd-a1c62f24add5",
                "dcba09c5-fa34-4bfd-902d-1ef741137b2f",
                "4c96a390-684f-42be-87df-63dfb9526a15",
            ),
        ).forEach { (organizationId, units) ->
            listOf(
                UserDataGenerator("André", "Pôssa", "55355226272", "andre.pôssa54@outlook.com.br", "85955089369"),
                UserDataGenerator("André", "Sánchez", "52227705167", "andre.sanchez14@outlook.com.br", "68998429992"),
                UserDataGenerator("André", "Valêncio", "43282095317", "andre.valêncio75@gmail.com", "96935724151"),
                UserDataGenerator("André", "Ávila", "27917106632", "andre.avila28@live.com.br", "24981987791"),
                UserDataGenerator("Antônio", "Côrtes", "35192409985", "antônio.côrtes79@live.com.br", "85948930301"),
                UserDataGenerator("Antônio", "Péricles", "04656283380", "antônio.pericles53@hotmail.com", "26940179539"),
                UserDataGenerator("Bruno", "Ramos", "44927436691", "bruno.ramos94@outlook.com.br", "85943816906"),
                UserDataGenerator("Bruno", "Silva", "59598337944", "bruno.silva9@hotmail.com", "45976491343"),
                UserDataGenerator("Bruno", "Teixeira", "31255340118", "bruno.teixeira15@live.com.br", "51995570381"),
                UserDataGenerator("Carlos", "Ferreira", "40391377280", "carlos.ferreira23@gmail.com", "39953004193"),
                UserDataGenerator("Carlos", "Ramos", "89803805002", "carlos.ramos1@gmail.com", "59925917874"),
                UserDataGenerator("Carlos", "Silva", "63411812052", "carlos.silva1@hotmail.com", "86947163473"),
                UserDataGenerator("Cláudia", "Muníz", "58165294636", "claudia.muniz82@gmail.com", "48917935420"),
                UserDataGenerator("Cláudia", "Valêncio", "44550176580", "claudia.valêncio47@live.com.br", "70946448506"),
                UserDataGenerator("Cláudia", "Ávila", "66455983270", "claudia.avila3@hotmail.com", "17919543347"),
                UserDataGenerator("Cássio", "Barbósa", "86336041913", "cassio.barbosa90@live.com.br", "33912978610"),
                UserDataGenerator("Cássio", "Fagúndez", "99665021494", "cassio.fagundez87@gmail.com", "59933570130"),
                UserDataGenerator("Cássio", "Márquez", "00320049582", "cassio.marquez50@gmail.com", "14957362212"),
                UserDataGenerator("Cássio", "Nóbrega", "99301013142", "cassio.nobrega100@hotmail.com", "29989695626"),
                UserDataGenerator("Daniel", "Cardoso", "91950962059", "daniel.cardoso14@hotmail.com", "84972003917"),
                UserDataGenerator("Daniel", "Moreira", "57237338854", "daniel.moreira24@live.com.br", "30920763460"),
                UserDataGenerator("Daniel", "Silva", "35478829072", "daniel.silva26@outlook.com.br", "40912290634"),
                UserDataGenerator("Felipe", "Almeida", "35032083216", "felipe.almeida30@hotmail.com", "52967163998"),
                UserDataGenerator("Felipe", "Lima", "28540867753", "felipe.lima77@gmail.com", "48964743515"),
                UserDataGenerator("Felipe", "Moura", "23410069305", "felipe.moura70@outlook.com.br", "78943843380"),
                UserDataGenerator("Felipe", "Oliveira", "50084490950", "felipe.oliveira14@outlook.com.br", "28951638445"),
                UserDataGenerator("Felipe", "Silva", "69284175445", "felipe.silva86@outlook.com.br", "46911975387"),
                UserDataGenerator("Felipe", "Teixeira", "61088340997", "felipe.teixeira81@gmail.com", "60966324001"),
                UserDataGenerator("Fernando", "Ferreira", "98556321259", "fernando.ferreira56@outlook.com.br", "92916217592"),
                UserDataGenerator("Fernando", "Lima", "38729052440", "fernando.lima94@live.com.br", "70921413266"),
                UserDataGenerator("Fernando", "Martins", "34681963267", "fernando.martins31@hotmail.com", "54920801460"),
                UserDataGenerator("Fernando", "Ramos", "55614775951", "fernando.ramos36@gmail.com", "94913391540"),
                UserDataGenerator("Fábio", "Batistá", "27012935375", "fabio.batista26@gmail.com", "79988963518"),
                UserDataGenerator("Fábio", "López", "95954384649", "fabio.lopez71@hotmail.com", "49984198061"),
                UserDataGenerator("Fábio", "Pérez", "54842309008", "fabio.perez9@gmail.com", "77917866119"),
                UserDataGenerator("Fábio", "Péricles", "21818517272", "fabio.pericles88@gmail.com", "57996063442"),
                UserDataGenerator("Gabriel", "Almeida", "31942580525", "gabriel.almeida37@gmail.com", "83941674313"),
                UserDataGenerator("Gabriel", "Costa", "47923407800", "gabriel.costa55@hotmail.com", "28977254993"),
                UserDataGenerator("Gabriel", "Ramos", "51150532505", "gabriel.ramos24@gmail.com", "17958134755"),
                UserDataGenerator("Gabriel", "Teixeira", "27531017016", "gabriel.teixeira95@live.com.br", "47931218365"),
                UserDataGenerator("Joseane", "López", "01716421390", "joseane.lopez60@outlook.com.br", "41983987644"),
                UserDataGenerator("Joseane", "Márquez", "19935272222", "joseane.marquez34@outlook.com.br", "21953083723"),
                UserDataGenerator("Joseane", "Nóbrega", "34961170100", "joseane.nobrega14@gmail.com", "66964166734"),
                UserDataGenerator("Joseane", "Péricles", "69519487123", "joseane.pericles3@outlook.com.br", "63971141137"),
                UserDataGenerator("Josefa", "Côrtes", "25486132409", "josefa.côrtes45@gmail.com", "25976407302"),
                UserDataGenerator("Josefa", "Ávila", "18713056166", "josefa.avila40@gmail.com", "51937832092"),
                UserDataGenerator("Joselito", "Batistá", "24794377606", "joselito.batista62@gmail.com", "14922003262"),
                UserDataGenerator("Joselito", "Muníz", "77872541734", "joselito.muniz42@hotmail.com", "96920627827"),
                UserDataGenerator("Joselí", "Barbosa", "00055888046", "joseli.barbosa98@live.com.br", "63979868488"),
                UserDataGenerator("Joselí", "Moura", "33013565918", "joseli.moura65@live.com.br", "35972116506"),
                UserDataGenerator("Joselí", "Ramos", "12439634255", "joseli.ramos55@hotmail.com", "40942965818"),
                UserDataGenerator("José", "Camões", "03041555948", "jose.camões57@outlook.com.br", "57996459460"),
                UserDataGenerator("José", "Côrtes", "27406427093", "jose.côrtes29@gmail.com", "99946090124"),
                UserDataGenerator("José", "Márquez", "16272322496", "jose.marquez19@hotmail.com", "86985406754"),
                UserDataGenerator("Joséfina", "Almeida", "86554110780", "josefina.almeida73@gmail.com", "37996283746"),
                UserDataGenerator("Joséfina", "Lima", "23618600747", "josefina.lima8@outlook.com.br", "20996927990"),
                UserDataGenerator("Joséfina", "Ramos", "24641344736", "josefina.ramos67@outlook.com.br", "89951876662"),
                UserDataGenerator("João", "Batistá", "26092765403", "joão.batista70@hotmail.com", "11952866923"),
                UserDataGenerator("João", "Camões", "04344904311", "joão.camões32@outlook.com.br", "69918452741"),
                UserDataGenerator("João", "López", "06403224689", "joão.lopez11@live.com.br", "21943417207"),
                UserDataGenerator("João", "Nóbrega", "00376056150", "joão.nobrega33@live.com.br", "96928469459"),
                UserDataGenerator("João", "Pôssa", "86482062784", "joão.pôssa17@outlook.com.br", "65978211613"),
                UserDataGenerator("João", "Sánchez", "44237425940", "joão.sanchez93@hotmail.com", "71934662414"),
                UserDataGenerator("João", "Valêncio", "06000681615", "joão.valêncio95@hotmail.com", "45932545612"),
                UserDataGenerator("Leandro", "Ferreira", "56736486343", "leandro.ferreira95@outlook.com.br", "48959586785"),
                UserDataGenerator("Leandro", "Oliveira", "66569734938", "leandro.oliveira6@hotmail.com", "56914291958"),
                UserDataGenerator("Leandro", "Silva", "98626026439", "leandro.silva97@gmail.com", "71982337366"),
                UserDataGenerator("Leandro", "Teixeira", "23221198493", "leandro.teixeira28@live.com.br", "94919254878"),
                UserDataGenerator("Lucas", "Oliveira", "82865735010", "lucas.oliveira79@live.com.br", "39987451517"),
                UserDataGenerator("Lucas", "Silva", "16592153318", "lucas.silva50@outlook.com.br", "99930225622"),
                UserDataGenerator("Lúcia", "Batistá", "17509973635", "lucia.batista3@hotmail.com", "64924025183"),
                UserDataGenerator("Lúcia", "Sánchez", "24234052949", "lucia.sanchez98@outlook.com.br", "62938311928"),
                UserDataGenerator("Lúcia", "Valêncio", "68043155615", "lucia.valêncio85@live.com.br", "39915064558"),
                UserDataGenerator("Marcos", "Moreira", "87202782847", "marcos.moreira56@hotmail.com", "67993089228"),
                UserDataGenerator("Marcos", "Rocha", "94045649824", "marcos.rocha61@hotmail.com", "75992295072"),
                UserDataGenerator("Márcia", "Péricles", "42408586585", "marcia.pericles49@gmail.com", "39943180043"),
                UserDataGenerator("Mário", "Ataíde", "18825562837", "mario.ataide58@live.com.br", "12921019015"),
                UserDataGenerator("Mário", "Barbósa", "74513959282", "mario.barbosa67@hotmail.com", "11951285949"),
                UserDataGenerator("Mário", "Méndez", "97573359160", "mario.mendez72@hotmail.com", "79929815873"),
                UserDataGenerator("Mário", "Nóbrega", "70796061602", "mario.nobrega72@live.com.br", "96960178001"),
                UserDataGenerator("Mário", "Valêncio", "76055131358", "mario.valêncio57@gmail.com", "50925573202"),
                UserDataGenerator("Paulo", "Barbosa", "24662183995", "paulo.barbosa29@gmail.com", "35962049082"),
                UserDataGenerator("Paulo", "Costa", "14269700130", "paulo.costa100@gmail.com", "92962487060"),
                UserDataGenerator("Paulo", "Moreira", "66914584869", "paulo.moreira25@outlook.com.br", "82985686146"),
                UserDataGenerator("Paulo", "Moura", "11747474479", "paulo.moura77@gmail.com", "88954501496"),
                UserDataGenerator("Paulo", "Rocha", "05910440802", "paulo.rocha68@outlook.com.br", "49934061835"),
                UserDataGenerator("Paulo", "Souza", "93758837014", "paulo.souza82@gmail.com", "42953388354"),
                UserDataGenerator("Paulo", "Teixeira", "52629410013", "paulo.teixeira93@gmail.com", "24974484042"),
                UserDataGenerator("Renato", "Moreira", "44901621114", "renato.moreira18@live.com.br", "14920751965"),
                UserDataGenerator("Renato", "Moura", "36433208843", "renato.moura68@outlook.com.br", "73916521507"),
                UserDataGenerator("Renato", "Rocha", "93996147430", "renato.rocha72@live.com.br", "49915691123"),
                UserDataGenerator("Renato", "Silva", "04385641463", "renato.silva70@hotmail.com", "58976600583"),
                UserDataGenerator("Ricardo", "Almeida", "08128101129", "ricardo.almeida97@live.com.br", "30960900788"),
                UserDataGenerator("Ricardo", "Cardoso", "35417462624", "ricardo.cardoso47@live.com.br", "29940049153"),
                UserDataGenerator("Ricardo", "Teixeira", "90584046928", "ricardo.teixeira54@gmail.com", "41993890708"),
                UserDataGenerator("Rogério", "Batistá", "99077175172", "rogerio.batista42@hotmail.com", "85989822397"),
                UserDataGenerator("Rogério", "Góes", "52244964469", "rogerio.goes12@gmail.com", "75942944001"),
                UserDataGenerator("Rogério", "López", "90356062805", "rogerio.lopez88@outlook.com.br", "75982705517"),
                UserDataGenerator("Rogério", "Márquez", "47388546820", "rogerio.marquez43@live.com.br", "74915979582"),
                UserDataGenerator("Rogério", "Ávila", "76982436721", "rogerio.avila89@gmail.com", "86947526803"),
                UserDataGenerator("Séfora", "Barbósa", "02977697748", "sefora.barbosa16@hotmail.com", "12934353464"),
                UserDataGenerator("Séfora", "Méndez", "98305678180", "sefora.mendez38@outlook.com.br", "81950068969"),
                UserDataGenerator("Thiago", "Barbosa", "71341405559", "thiago.barbosa35@gmail.com", "42959860404"),
                UserDataGenerator("Thiago", "Cardoso", "91971115703", "thiago.cardoso45@live.com.br", "96955856060"),
                UserDataGenerator("Thiago", "Martins", "07732915631", "thiago.martins96@gmail.com", "17923211845"),
                UserDataGenerator("Thiago", "Moura", "69678959585", "thiago.moura96@hotmail.com", "91964904538"),
                UserDataGenerator("Tássia", "Camões", "83586532121", "tassia.camões49@live.com.br", "27912471647"),
                UserDataGenerator("Tássia", "López", "35727164389", "tassia.lopez45@live.com.br", "21967605860"),
                UserDataGenerator("Tássia", "Ribeirão", "22110316764", "tassia.ribeirão79@live.com.br", "49935542366"),
                UserDataGenerator("Vítor", "Côrtes", "14483237110", "vitor.côrtes31@live.com.br", "27911305398"),
                UserDataGenerator("Vítor", "Sánchez", "47737466029", "vitor.sanchez47@gmail.com", "16935100215"),
                UserDataGenerator("Álvaro", "Ataíde", "18613086047", "alvaro.ataide53@live.com.br", "58987096631"),
                UserDataGenerator("Álvaro", "Nóbrega", "84637342279", "alvaro.nobrega66@gmail.com", "81959215066"),
                UserDataGenerator("Álvaro", "Rômulo", "46824578967", "alvaro.rômulo6@hotmail.com", "31937253053"),
                UserDataGenerator("Álvaro", "Valêncio", "78396870756", "alvaro.valêncio84@gmail.com", "80916158071"),
                UserDataGenerator("Érica", "Batistá", "81126389773", "erica.batista23@outlook.com.br", "83932171846"),
                UserDataGenerator("Érica", "Côrtes", "89349517450", "erica.côrtes38@outlook.com.br", "29923946430"),
                UserDataGenerator("Érica", "Márquez", "10783421052", "erica.marquez81@outlook.com.br", "41918723698"),
                UserDataGenerator("Érica", "Méndez", "02825418374", "erica.mendez50@live.com.br", "30925364982"),
                UserDataGenerator("Érica", "Pérez", "42527536190", "erica.perez95@outlook.com.br", "20942463921"),
                UserDataGenerator("Érica", "Ribeirão", "78490718911", "erica.ribeirão82@live.com.br", "38968178908"),
                UserDataGenerator("Érica", "Rômulo", "98579816408", "erica.rômulo69@hotmail.com", "68977676047"),
                UserDataGenerator("Ítalo", "Barbósa", "67649467791", "italo.barbosa17@outlook.com.br", "91992635598"),
                UserDataGenerator("Ítalo", "Camões", "45188233541", "italo.camões97@gmail.com", "32973582753"),
                UserDataGenerator("Ítalo", "Valêncio", "48724424404", "italo.valêncio77@gmail.com", "18957703240"),
            ).forEach { userData ->

                val userId = randomUUID()
                val date = randomZonedDateTime(startZonedDateTime, endZonedDateTime)
                val isActive = listOf(true, true, false).random().toString()
                val createdAt = formatter.format(date)
                val updatedAt = formatter.format(date.plusNanos(randomLong(1_000_000_000, 9_999_999_999_999)))

                val roles = SecurityOrganizationRole.entries.shuffled()
                    .take(randomInteger(0, SecurityOrganizationRole.entries.size))
                    .joinToString(",") { "\"$it\"" }

                val roleUnits = listOf(0, 0, 1, 2, 3).random().let { quantity ->
                    units.shuffled().take(quantity)
                }.map { unit ->
                    unit to SecurityUnitRole.entries
                        .take(randomInteger(0, SecurityUnitRole.entries.size))
                        .joinToString(",") { "\"$it\"" }
                }

                val groupList = listOf(0, 0, 1, 2, 3, 4).random().let { quantity ->
                    organizationGroups[organizationId]!!.shuffled().take(quantity)
                }

                users.add(
                    "MATCH (o:Organization {" +
                            "id: \"$organizationId\"" +
                            "}) " +
                            "CREATE(u:User {" +
                            "id: \"$userId\", " +
                            "first_name: \"${userData.firstName}\", " +
                            "last_name: \"${userData.lastName}\", " +
                            "document_number: \"${userData.documentNumber}\", " +
                            "profile_photo: \"http://profile-photo.com/${userData.mainEmail.substringBefore("@")}.jpeg\", " +
                            "user_gender: \"${Gender.entries.random()}\", " +
                            "birth_date: date(\"${generateBirthday()}\"), " +
                            "password_salt: \"${randomUUID()}\", " +
                            "password_hash: \"${randomUUID()}\", " +
                            "main_email: \"${userData.mainEmail.unaccentedLower()}\", " +
                            "main_phone: \"${userData.phoneName}\", " +
                            "is_active: $isActive, " +
                            "created_at: datetime(\"$createdAt\"), " +
                            "updated_at: datetime(\"$updatedAt\")})" +
                            "-[:USER_BELONGS_TO_ORGANIZATION {" +
                            "id: \"${randomUUID()}\", " +
                            "user_id: \"$userId\", " +
                            "organization_id: \"$organizationId\", " +
                            "user_roles: [$roles]" +
                            "} ]->(o);"
                )

                roleUnits.forEach { roleUnit ->
                    usersUnits.add(
                        "MATCH (u:Unit {" +
                                "id: \"${roleUnit.first}\"}" +
                                ") " +
                                "MATCH (a:User {" +
                                "id: \"$userId\"}" +
                                ") " +
                                "CREATE (a)-[:USER_ALLOWED_IN_UNIT {" +
                                "id: \"${randomUUID()}\", " +
                                "user_id: \"$userId\", " +
                                "unit_id: \"${roleUnit.first}\", " +
                                "user_roles: [${roleUnit.second}]" +
                                "} ]->(u);"
                    )
                }

                groupList.forEach { userGroup ->
                    usersGroups.add(
                        "MATCH (u:User {" +
                                "id: \"$userId\"}" +
                                ") " +
                                "MATCH (g:Group {" +
                                "id: \"$userGroup\"}" +
                                ") " +
                                "CREATE (u)-[:USER_IN_GROUP {" +
                                "id: \"${randomUUID()}\", " +
                                "user_id: \"$userId\", " +
                                "group_id: \"$userGroup\"" +
                                "} ]->(g);"
                    )
                }

            }

            users.add("")
            usersUnits.add("")
            usersGroups.add("")
        }

        users.forEach(::println)
        usersUnits.forEach(::println)
        usersGroups.forEach(::println)
    }

    private data class UserDataGenerator(
        val firstName: String,
        val lastName: String,
        val documentNumber: String,
        val mainEmail: String,
        val phoneName: String,
    )

}