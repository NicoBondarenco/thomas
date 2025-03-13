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
import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.core.util.DateUtils.randomZonedDateTime
import com.thomas.core.util.NumberUtils.randomInteger
import com.thomas.core.util.NumberUtils.randomLong
import com.thomas.management.data.neo4j.model.node.GroupUnitNode
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME
import java.util.UUID
import java.util.UUID.randomUUID
import org.junit.jupiter.api.Test

class GeneratorTest {

    private val formatter = ISO_ZONED_DATE_TIME
    private val startZonedDateTime: ZonedDateTime = ZonedDateTime.parse("2025-01-01T00:00:00.000000Z", formatter)
    private val endZonedDateTime: ZonedDateTime = ZonedDateTime.parse("2025-12-31T23:59:59.999999Z", formatter)

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

                val roleUnits = listOf(0,0,1,2,3).random().let { quantity ->
                    units.shuffled().take(quantity)
                }.map { unit -> unit to SecurityUnitRole.entries
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
                    groupUnits.add("MATCH (u:Unit {" +
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
                            "} ]->(u);")
                }
            }
            groups.add("")
            groupUnits.add("")
        }

        groups.forEach(::println)
        groupUnits.forEach(::println)
    }

}