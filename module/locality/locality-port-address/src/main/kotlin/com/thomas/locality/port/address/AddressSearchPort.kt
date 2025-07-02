package com.thomas.locality.port.address

import com.thomas.locality.port.address.i18n.LocalityAddressPortMessageI18N.localityAddressPortErrorUnknownError
import com.thomas.locality.port.address.model.response.AddressSearchResponse
import io.github.oshai.kotlinlogging.KotlinLogging

abstract class AddressSearchPort {

    private val logger = KotlinLogging.logger {}

    companion object {
        private val ZIPCODE_REGEX = Regex("^[0-9]{8}\$")
    }

    suspend fun searchByZipcode(zipcode: String): AddressSearchResponse {
        val parameter = zipcode.takeIf { ZIPCODE_REGEX.matches(it) } ?: throw InvalidZipcodeException(zipcode)
        try {
            return search(parameter)
        } catch (e: AddressNotFoundException) {
            logger.warn { e.message }
            throw e
        } catch (e: Exception) {
            val exception = AddressRequestErrorException(zipcode, e.message ?: localityAddressPortErrorUnknownError(zipcode))
            logger.error(exception) { exception.message }
            throw exception
        }
    }

    protected abstract suspend fun search(zipcode: String): AddressSearchResponse

}