package com.thomas.locality.port.address

import com.thomas.core.exception.ApplicationException
import com.thomas.core.exception.ErrorType.APPLICATION_ERROR
import com.thomas.locality.port.address.i18n.LocalityAddressPortMessageI18N.localityAddressPortErrorRequestError

class AddressRequestErrorException(
    zipcode: String,
    message: String,
) : ApplicationException(
    message = localityAddressPortErrorRequestError(zipcode, message),
    type = APPLICATION_ERROR,
)