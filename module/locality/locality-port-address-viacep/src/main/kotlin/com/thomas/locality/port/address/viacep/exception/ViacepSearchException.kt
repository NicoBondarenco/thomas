package com.thomas.locality.port.address.viacep.exception

import com.thomas.locality.port.address.viacep.i18n.LocalityAddressViacepPortMessageI18N.localityAddressViacepPortErrorRequestError

class ViacepSearchException(
    zipcode: String,
    status: Int,
    response: String?
) : RuntimeException(message = localityAddressViacepPortErrorRequestError(zipcode, status.toString(), response ?: "<null>"))