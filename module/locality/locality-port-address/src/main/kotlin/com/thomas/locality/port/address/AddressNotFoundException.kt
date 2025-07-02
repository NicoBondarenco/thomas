package com.thomas.locality.port.address

import com.thomas.core.exception.ApplicationException
import com.thomas.core.exception.ErrorType.NOT_FOUND
import com.thomas.locality.port.address.i18n.LocalityAddressPortMessageI18N.localityAddressPortErrorNotFound

class AddressNotFoundException(
    zipcode: String,
) : ApplicationException(
    message = localityAddressPortErrorNotFound(zipcode),
    type = NOT_FOUND,
)