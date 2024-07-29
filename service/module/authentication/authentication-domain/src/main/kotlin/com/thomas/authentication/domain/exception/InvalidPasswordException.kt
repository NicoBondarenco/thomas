package com.thomas.authentication.domain.exception

import com.thomas.authentication.domain.i18n.AuthenticationDomainMessageI18N.authenticationUserAuthenticationInvalidCredentialsUsernamePassword
import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.NOT_FOUND

class InvalidPasswordException : DetailedException(
    message = authenticationUserAuthenticationInvalidCredentialsUsernamePassword(),
    type = NOT_FOUND,
)
