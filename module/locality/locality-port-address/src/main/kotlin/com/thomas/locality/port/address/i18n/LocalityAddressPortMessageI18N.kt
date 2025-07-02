package com.thomas.locality.port.address.i18n

import com.thomas.core.i18n.BundleResolver

object LocalityAddressPortMessageI18N : BundleResolver("strings/locality-address-port") {

    fun localityAddressPortErrorNotFound(zipcode: String): String = formattedMessage("locality.address.port.error.not-found", zipcode)

    fun localityAddressPortErrorInvalidZipcode(zipcode: String): String = formattedMessage("locality.address.port.error.invalid-zipcode", zipcode)

    fun localityAddressPortErrorRequestError(zipcode: String, message: String): String = formattedMessage("locality.address.port.error.request-error", zipcode, message)

    fun localityAddressPortErrorUnknownError(zipcode: String): String = formattedMessage("locality.address.port.error.unknown-error", zipcode)

}
