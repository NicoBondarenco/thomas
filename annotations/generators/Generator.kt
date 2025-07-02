package com.thomas.locality.data.komapper.repository

import com.thomas.core.extension.toIsoOffsetDateTime
import com.thomas.core.model.general.AddressState
import com.thomas.core.util.DateUtils.randomOffsetDateTime
import com.thomas.core.util.NumberUtils.randomInteger
import com.thomas.core.util.NumberUtils.randomLong
import com.thomas.core.util.StringUtils.randomString
import com.thomas.locality.data.entity.AddressEntity
import org.junit.jupiter.api.Test

class Generator {

    private val entity: AddressEntity
        get() = AddressEntity(
            zipcodeNumber = randomInteger(10000000, 99999999).toString(),
            addressStreet = listOf(randomString(), null).random(),
            addressComplement = listOf(randomString(), null).random(),
            addressUnit = listOf(randomString(), null).random(),
            addressNeighborhood = listOf(randomString(), null).random(),
            addressCity = randomString(numbers = false),
            addressState = AddressState.entries.random(),
            cityCode = listOf(randomString(), null).random(),
            referenceCode = listOf(randomString(), null).random(),
            phoneCode = listOf(randomInteger(10, 99).toString(), null).random(),
            federalCode = listOf(randomString(), null).random(),
        )

    fun sql(
        entity: AddressEntity
    ): String {
        val createdAt = randomOffsetDateTime()
        val updatedAt = createdAt.plusSeconds(randomLong(100_000))
        return "INSERT INTO \"locality\".\"address\" (" +
                "\"id\", " +
                "\"zipcode_number\", " +
                "\"address_street\", " +
                "\"address_complement\", " +
                "\"address_unit\", " +
                "\"address_neighborhood\", " +
                "\"address_city\", " +
                "\"address_state\", " +
                "\"city_code\", " +
                "\"reference_code\", " +
                "\"phone_code\", " +
                "\"federal_code\", " +
                "\"created_at\", " +
                "\"updated_at\") " +
                "VALUES (" +
                "'${entity.id}'::uuid, " +
                "${entity.zipcodeNumber.singleQuote()}, " +
                "${entity.addressStreet?.singleQuote()}, " +
                "${entity.addressComplement?.singleQuote()}, " +
                "${entity.addressUnit?.singleQuote()}, " +
                "${entity.addressNeighborhood?.singleQuote()}, " +
                "${entity.addressCity.singleQuote()}, " +
                "${entity.addressState}, " +
                "${entity.cityCode?.singleQuote()}, " +
                "${entity.referenceCode?.singleQuote()}, " +
                "${entity.phoneCode?.singleQuote()}, " +
                "${entity.federalCode?.singleQuote()}, " +
                "${createdAt.toIsoOffsetDateTime().singleQuote()}, " +
                "${updatedAt.toIsoOffsetDateTime().singleQuote()}" +
                ");"
    }

    private fun String.singleQuote(): String = "'$this'"

    @Test
    fun `generator`(){
        repeat(10) {
            println(sql(entity = entity))
        }
    }

}