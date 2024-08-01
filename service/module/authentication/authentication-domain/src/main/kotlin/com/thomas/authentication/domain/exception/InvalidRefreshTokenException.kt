package com.thomas.authentication.domain.exception

import com.thomas.authentication.domain.i18n.AuthenticationDomainMessageI18N.authenticationUserAuthenticationInvalidCredentialsRefreshToken
import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.UNAUTHORIZED_ACTION

class InvalidRefreshTokenException : DetailedException(
    message = authenticationUserAuthenticationInvalidCredentialsRefreshToken(),
    type = UNAUTHORIZED_ACTION,
)
