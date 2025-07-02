package com.thomas.locality.port.address.viacep.exception

import com.thomas.locality.port.address.viacep.i18n.LocalityAddressViacepPortMessageI18N.localityAddressViacepPortErrorEmptyBody

class ViacepEmptyBodyException(
    zipcode: String,
    status: Int,
    response: String?
) : RuntimeException(message = localityAddressViacepPortErrorEmptyBody(zipcode, status.toString(), response ?: "<null>"))