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
import com.thomas.core.util.DateUtils.randomZonedDateTime
import com.thomas.core.util.NumberUtils.randomInteger
import com.thomas.core.util.NumberUtils.randomLong
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME
import java.util.UUID
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
            entities.add("OrganizationEntity(" +
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
                    "),")
        }
        println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------")
        cyphers.forEach { println(it)}
        println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------")
        entities.forEach { println(it)}
        println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------")
    }

}