package com.thomas.locality.data.entity

import com.thomas.core.model.general.AddressRegion
import com.thomas.core.model.general.AddressState
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class AddressEntityTest {

    private fun createValidAddressEntity(
        id: UUID = UUID.randomUUID(),
        zipcodeNumber: String = "12345678",
        addressStreet: String? = "Rua das Flores, 123",
        addressComplement: String? = "Apto 45",
        addressUnit: String? = "A",
        addressNeighborhood: String? = "Centro",
        addressCity: String = "São Paulo",
        addressState: AddressState = AddressState.SP,
        addressRegion: AddressRegion = AddressRegion.SOUTHEAST,
        cityCode: String? = "3550308",
        referenceCode: String? = "REF123",
        phoneCode: String = "11",
        federalCode: String? = "35",
        createdAt: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC),
        updatedAt: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC)
    ) = AddressEntity(
        id = id,
        zipcodeNumber = zipcodeNumber,
        addressStreet = addressStreet,
        addressComplement = addressComplement,
        addressUnit = addressUnit,
        addressNeighborhood = addressNeighborhood,
        addressCity = addressCity,
        addressState = addressState,
        cityCode = cityCode,
        referenceCode = referenceCode,
        phoneCode = phoneCode,
        federalCode = federalCode,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    @Test
    fun `Should create valid AddressEntity successfully`() {
        // Given & When
        val addressEntity = createValidAddressEntity()

        // Then
        assertNotNull(addressEntity)
        assertEquals("12345678", addressEntity.zipcodeNumber)
        assertEquals("Rua das Flores, 123", addressEntity.addressStreet)
        assertEquals("São Paulo", addressEntity.addressCity)
        assertEquals(AddressState.SP, addressEntity.addressState)
        assertEquals("11", addressEntity.phoneCode)
    }

    @Test
    fun `Should create AddressEntity with null optional fields`() {
        // Given & When
        val addressEntity = createValidAddressEntity(
            addressStreet = null,
            addressComplement = null,
            addressUnit = null,
            addressNeighborhood = null,
            cityCode = null,
            referenceCode = null,
            federalCode = null
        )

        // Then
        assertNotNull(addressEntity)
        assertNull(addressEntity.addressStreet)
        assertNull(addressEntity.addressComplement)
        assertNull(addressEntity.addressUnit)
        assertNull(addressEntity.addressNeighborhood)
        assertNull(addressEntity.cityCode)
        assertNull(addressEntity.referenceCode)
        assertNull(addressEntity.federalCode)
    }

    // Zipcode Number Validation Tests
    @Test
    fun `Should throw exception when zipcode number is invalid format`() {
        assertThrows<IllegalArgumentException> {
            createValidAddressEntity(zipcodeNumber = "1234567")
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["12345678", "00000000", "99999999"])
    fun `Should accept valid zipcode numbers`(zipcodeNumber: String) {
        assertDoesNotThrow {
            createValidAddressEntity(zipcodeNumber = zipcodeNumber)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["1234567", "123456789", "abcd1234", "12345-678", ""])
    fun `Should reject invalid zipcode numbers`(zipcodeNumber: String) {
        assertThrows<IllegalArgumentException> {
            createValidAddressEntity(zipcodeNumber = zipcodeNumber)
        }
    }

    // Address Street Validation Tests
    @Test
    fun `Should accept valid address street values`() {
        val validStreets = listOf(
            "Rua das Flores, 123",
            "Av. Paulista - 1000",
            "Travessa São João, 45; Bloco A",
            "Rua 123ª",
            "Street with numbers 456",
            null
        )

        validStreets.forEach { street ->
            assertDoesNotThrow {
                createValidAddressEntity(addressStreet = street)
            }
        }
    }

    @Test
    fun `Should reject address street with invalid characters`() {
        assertThrows<IllegalArgumentException> {
            createValidAddressEntity(addressStreet = "Rua @#$%^&")
        }
    }

    @Test
    fun `Should reject address street with invalid length`() {
        val tooShort = "AB"
        val tooLong = "A".repeat(251)

        assertThrows<IllegalArgumentException> {
            createValidAddressEntity(addressStreet = tooShort)
        }

        assertThrows<IllegalArgumentException> {
            createValidAddressEntity(addressStreet = tooLong)
        }
    }

    // Address Complement Validation Tests
    @Test
    fun `Should accept valid address complement values`() {
        val validComplements = listOf(
            "Apto 123",
            "Bloco A - Sala 45",
            "Casa 1ª",
            "Complemento com números 456/789",
            null
        )

        validComplements.forEach { complement ->
            assertDoesNotThrow {
                createValidAddressEntity(addressComplement = complement)
            }
        }
    }

    @Test
    fun `Should reject address complement with invalid length`() {
        val tooShort = "AB"
        val tooLong = "A".repeat(251)

        assertThrows<IllegalArgumentException> {
            createValidAddressEntity(addressComplement = tooShort)
        }

        assertThrows<IllegalArgumentException> {
            createValidAddressEntity(addressComplement = tooLong)
        }
    }

    // Address Unit Validation Tests
    @Test
    fun `Should accept valid address unit values`() {
        val validUnits = listOf(
            "A",
            "123",
            "Unit 456",
            "A".repeat(250),
            null
        )

        validUnits.forEach { unit ->
            assertDoesNotThrow {
                createValidAddressEntity(addressUnit = unit)
            }
        }
    }

    @Test
    fun `Should reject address unit with invalid length`() {
        val tooLong = "A".repeat(251)

        assertThrows<IllegalArgumentException> {
            createValidAddressEntity(addressUnit = tooLong)
        }
    }

    // Address Neighborhood Validation Tests
    @Test
    fun `Should accept valid address neighborhood values`() {
        val validNeighborhoods = listOf(
            "Centro",
            "Vila Madalena",
            "Bairro São João - Setor 1",
            "Neighborhood 123ª",
            null
        )

        validNeighborhoods.forEach { neighborhood ->
            assertDoesNotThrow {
                createValidAddressEntity(addressNeighborhood = neighborhood)
            }
        }
    }

    @Test
    fun `Should reject address neighborhood with invalid length`() {
        val tooShort = "AB"
        val tooLong = "A".repeat(251)

        assertThrows<IllegalArgumentException> {
            createValidAddressEntity(addressNeighborhood = tooShort)
        }

        assertThrows<IllegalArgumentException> {
            createValidAddressEntity(addressNeighborhood = tooLong)
        }
    }

    // Address City Validation Tests
    @Test
    fun `Should accept valid address city values`() {
        val validCities = listOf(
            "São Paulo",
            "Rio de Janeiro",
            "Belo Horizonte",
            "São José dos Campos"
        )

        validCities.forEach { city ->
            assertDoesNotThrow {
                createValidAddressEntity(addressCity = city)
            }
        }
    }

    @Test
    fun `Should reject address city with invalid characters`() {
        assertThrows<IllegalArgumentException> {
            createValidAddressEntity(addressCity = "City123")
        }
    }

    @Test
    fun `Should reject address city with invalid length`() {
        val tooShort = "AB"
        val tooLong = "A".repeat(251)

        assertThrows<IllegalArgumentException> {
            createValidAddressEntity(addressCity = tooShort)
        }

        assertThrows<IllegalArgumentException> {
            createValidAddressEntity(addressCity = tooLong)
        }
    }

    // Phone Code Validation Tests
    @ParameterizedTest
    @ValueSource(strings = ["11", "21", "85", "1", "123"])
    fun `Should accept valid phone codes`(phoneCode: String) {
        assertDoesNotThrow {
            createValidAddressEntity(phoneCode = phoneCode)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "1234", "AB", "12A"])
    fun `Should reject invalid phone codes`(phoneCode: String) {
        assertThrows<IllegalArgumentException> {
            createValidAddressEntity(phoneCode = phoneCode)
        }
    }

    // City Code Validation Tests
    @Test
    fun `Should accept valid city codes`() {
        val validCityCodes = listOf(
            "3550308",
            "1",
            "A".repeat(250),
            null
        )

        validCityCodes.forEach { cityCode ->
            assertDoesNotThrow {
                createValidAddressEntity(cityCode = cityCode)
            }
        }
    }

    @Test
    fun `Should reject city code with invalid length`() {
        val tooLong = "A".repeat(251)

        assertThrows<IllegalArgumentException> {
            createValidAddressEntity(cityCode = tooLong)
        }
    }

    // Reference Code Validation Tests
    @Test
    fun `Should accept valid reference codes`() {
        val validReferenceCodes = listOf(
            "REF123",
            "1",
            "A".repeat(250),
            null
        )

        validReferenceCodes.forEach { referenceCode ->
            assertDoesNotThrow {
                createValidAddressEntity(referenceCode = referenceCode)
            }
        }
    }

    @Test
    fun `Should reject reference code with invalid length`() {
        val tooLong = "A".repeat(251)

        assertThrows<IllegalArgumentException> {
            createValidAddressEntity(referenceCode = tooLong)
        }
    }

    // Federal Code Validation Tests
    @Test
    fun `Should accept valid federal codes`() {
        val validFederalCodes = listOf(
            "35",
            "1",
            "A".repeat(250),
            null
        )

        validFederalCodes.forEach { federalCode ->
            assertDoesNotThrow {
                createValidAddressEntity(federalCode = federalCode)
            }
        }
    }

    @Test
    fun `Should reject federal code with invalid length`() {
        val tooLong = "A".repeat(251)

        assertThrows<IllegalArgumentException> {
            createValidAddressEntity(federalCode = tooLong)
        }
    }

    // Data Class Functionality Tests
    @Test
    fun `Should support data class equality`() {
        val id = UUID.randomUUID()
        val createdAt = OffsetDateTime.now(ZoneOffset.UTC)
        val updatedAt = OffsetDateTime.now(ZoneOffset.UTC)

        val address1 = createValidAddressEntity(
            id = id,
            createdAt = createdAt,
            updatedAt = updatedAt
        )

        val address2 = createValidAddressEntity(
            id = id,
            createdAt = createdAt,
            updatedAt = updatedAt
        )

        assertEquals(address1, address2)
        assertEquals(address1.hashCode(), address2.hashCode())
    }

    @Test
    fun `Should support data class copy functionality`() {
        val originalAddress = createValidAddressEntity()
        val copiedAddress = originalAddress.copy(addressCity = "Rio de Janeiro")

        assertEquals(originalAddress.id, copiedAddress.id)
        assertEquals("São Paulo", originalAddress.addressCity)
        assertEquals("Rio de Janeiro", copiedAddress.addressCity)
    }

    @Test
    fun `Should have proper toString representation`() {
        val address = createValidAddressEntity()
        val toStringResult = address.toString()

        assertTrue(toStringResult.contains("AddressEntity"))
        assertTrue(toStringResult.contains("zipcodeNumber=12345678"))
        assertTrue(toStringResult.contains("addressCity=São Paulo"))
    }
}