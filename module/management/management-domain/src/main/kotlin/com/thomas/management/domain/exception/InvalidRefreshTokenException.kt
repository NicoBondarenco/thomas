package com.thomas.management.domain.exception

import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.UNAUTHORIZED_ACTION
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementAuthenticationRefreshRefreshTokenInvalidToken

class InvalidRefreshTokenException(
    cause: Throwable? = null,
) : DetailedException(
    message = managementAuthenticationRefreshRefreshTokenInvalidToken(),
    type = UNAUTHORIZED_ACTION,
    cause = cause,
)
