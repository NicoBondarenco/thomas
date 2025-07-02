package com.thomas.locality.port.address

import com.thomas.core.exception.ApplicationException
import com.thomas.core.exception.ErrorType
import com.thomas.locality.port.address.i18n.LocalityAddressPortMessageI18N.localityAddressPortErrorInvalidZipcode

class InvalidZipcodeException(
    zipcode: String,
) : ApplicationException(
    message = localityAddressPortErrorInvalidZipcode(zipcode),
    type = ErrorType.INVALID_PARAMETER,
)