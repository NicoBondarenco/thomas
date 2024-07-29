package com.thomas.authentication.domain.exception

import com.thomas.authentication.domain.i18n.AuthenticationDomainMessageI18N.authenticationUserAuthenticationNotFoundErrorMessage
import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.NOT_FOUND
import java.util.UUID

class UserAuthenticationNotFoundException(
    id: UUID
) : DetailedException(
    message = authenticationUserAuthenticationNotFoundErrorMessage(id),
    type = NOT_FOUND,
)
