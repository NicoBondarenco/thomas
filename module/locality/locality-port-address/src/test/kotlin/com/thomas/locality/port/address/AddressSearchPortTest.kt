package com.thomas.locality.port.address

import com.thomas.core.model.general.AddressState.SP
import com.thomas.core.util.NumberUtils.randomInteger
import com.thomas.locality.port.address.i18n.LocalityAddressPortMessageI18N.localityAddressPortErrorInvalidZipcode
import com.thomas.locality.port.address.i18n.LocalityAddressPortMessageI18N.localityAddressPortErrorNotFound
import com.thomas.locality.port.address.i18n.LocalityAddressPortMessageI18N.localityAddressPortErrorRequestError
import com.thomas.locality.port.address.model.response.AddressSearchResponse
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull

class AddressSearchPortTest {

    private val response: AddressSearchResponse
        get() = AddressSearchResponse(
            zipcodeNumber = "11233455",
            addressStreet = "Test Street",
            addressComplement = null,
            addressUnit = null,
            addressNeighborhood = "Test Neighborhood",
            addressCity = "Test City",
            addressState = SP,
            cityCode = null,
            referenceCode = null,
            phoneCode = null,
            federalCode = null,
        )

    private val port: AddressSearchPort = object : AddressSearchPort() {
        override suspend fun search(zipcode: String): AddressSearchResponse {
            if (zipcode == zipcodeNotFound) {
                throw AddressNotFoundException(zipcode)
            }
            if (zipcode == zipcodeSearchError) {
                throw IllegalArgumentException("Too many requests")
            }
            if (zipcode == zipcodeGenericError) {
                throw IllegalArgumentException()
            }
            return response.copy(zipcodeNumber = zipcode)
        }
    }

    private var zipcodeNotFound: String = randomInteger(10000000, 99999999).toString()
    private var zipcodeSearchError: String = randomInteger(10000000, 99999999).toString()
    private var zipcodeGenericError: String = randomInteger(10000000, 99999999).toString()

    @Test
    fun `Found zipcode does not throws exception`() = runTest {
        try {
            val result = port.searchByZipcode(randomInteger(10000000, 99999999).toString())
            assertNotNull(result)
        } catch (e: Exception) {
            fail(e.message, e)
        }
    }

    @Test
    fun `Not found zipcode throws AddressNotFoundException`() = runTest {
        try {
            port.searchByZipcode(zipcodeNotFound)
            fail("Should not been reached")
        } catch (e: AddressNotFoundException) {
            assertEquals(localityAddressPortErrorNotFound(zipcodeNotFound), e.message)
        } catch (e: Exception) {
            fail(e.message, e)
        }
    }

    @Test
    fun `Search error zipcode throws AddressRequestErrorException`() = runTest {
        try {
            port.searchByZipcode(zipcodeSearchError)
            fail("Should throw AddressRequestErrorException")
        } catch (e: AddressRequestErrorException) {
            assertEquals(localityAddressPortErrorRequestError(zipcodeSearchError, "Too many requests"), e.message)
        } catch (e: Exception) {
            fail(e.message, e)
        }
    }

    @Test
    fun `Search error without message throws AddressRequestErrorException`() = runTest {
        try {
            port.searchByZipcode(zipcodeGenericError)
            fail("Should throw AddressRequestErrorException")
        } catch (e: AddressRequestErrorException) {
            assertEquals(localityAddressPortErrorRequestError(zipcodeGenericError, "Unknown error"), e.message)
        } catch (e: Exception) {
            fail(e.message, e)
        }
    }

    @Test
    fun `Search invalid zipcode throws InvalidZipcodeException`() = runTest {
        val zipcode = randomInteger(100000, 999999).toString()
        try {
            port.searchByZipcode(zipcode)
            fail("Should throw InvalidZipcodeException")
        } catch (e: InvalidZipcodeException) {
            assertEquals(localityAddressPortErrorInvalidZipcode(zipcode), e.message)
        } catch (e: Exception) {
            fail(e.message, e)
        }
    }

}
