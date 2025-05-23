package com.thomas.management.domain.exception

import com.thomas.core.exception.ApplicationException
import com.thomas.core.exception.ErrorType.UNAUTHORIZED_ACTION
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementAuthenticationRefreshRefreshTokenInvalidToken

class InvalidRefreshTokenException(
    cause: Throwable? = null,
) : ApplicationException(
    message = managementAuthenticationRefreshRefreshTokenInvalidToken(),
    type = UNAUTHORIZED_ACTION,
    cause = cause,
)
