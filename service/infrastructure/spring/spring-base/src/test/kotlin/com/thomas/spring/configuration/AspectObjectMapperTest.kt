package com.thomas.spring.configuration

import com.thomas.core.aop.MaskField
import com.thomas.core.model.general.Gender
import com.thomas.core.model.general.Gender.CIS_MALE
import java.time.LocalDate
import java.util.UUID
import java.util.UUID.fromString
import kotlin.random.Random
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode.STRICT

class AspectObjectMapperTest {

    private val mapper = JacksonConfiguration().aspectMapper()

    @Test
    fun `Serialize masking fields`() {
        val expected = """
            {
               "personId": "4fe78258-d744-4fb0-97a6-3cdf4e850e3a",
               "fullName": "Anthony Stark",
               "personEmail": "******************",
               "personGender": "CIS_MALE",
               "birthDate": "1990-04-28",
               "secretNumber": "**",
               "secretCode": "************************************"
            }
        """.trimIndent()
        val json = mapper.writeValueAsString(Person())
        JSONAssert.assertEquals(expected, json, STRICT)
    }

    @Test
    fun `Serialize masking field null`() {
        val expected = """
            {
               "personId": "4fe78258-d744-4fb0-97a6-3cdf4e850e3a",
               "fullName": "Anthony Stark",
               "personEmail": "******************",
               "personGender": "CIS_MALE",
               "birthDate": "1990-04-28",
               "secretNumber": "**",
               "secretCode": null
            }
        """.trimIndent()
        val json = mapper.writeValueAsString(Person(secretCode = null))
        JSONAssert.assertEquals(expected, json, STRICT)
    }

    private data class Person(
        val personId: UUID = fromString("4fe78258-d744-4fb0-97a6-3cdf4e850e3a"),
        val fullName: String = "Anthony Stark",
        @MaskField val personEmail: String = "iron.man@email.com",
        val personGender: Gender = CIS_MALE,
        val birthDate: LocalDate = LocalDate.of(1990, 4, 28),
        @MaskField val secretNumber: Int = Random.nextInt(10, 99),
        @MaskField val secretCode: UUID? = fromString("99c081c5-9bbd-4491-be1f-f0dab18f4147"),
    )

}
