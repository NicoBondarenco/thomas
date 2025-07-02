package com.thomas.locality.port.address.viacep.i18n

import com.thomas.core.i18n.BundleResolver

object LocalityAddressViacepPortMessageI18N : BundleResolver("strings/locality-address-viacep-port") {

    fun localityAddressViacepPortErrorRequestError(
        zipcode: String,
        status: String,
        response: String,
    ): String = formattedMessage("locality.address.viacep.port.error.request-error", zipcode, status, response)

    fun localityAddressViacepPortErrorEmptyBody(
        zipcode: String,
        status: String,
        response: String,
    ): String = formattedMessage("locality.address.viacep.port.error.empty-body", zipcode, status, response)

}
