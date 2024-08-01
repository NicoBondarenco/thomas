package com.thomas.authentication.domain.exception

import com.thomas.authentication.domain.i18n.AuthenticationDomainMessageI18N.authenticationResetPasswordTokenResetInvalidToken
import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.INVALID_PARAMETER

class InvalidResetTokenException : DetailedException(
    message = authenticationResetPasswordTokenResetInvalidToken(),
    type = INVALID_PARAMETER,
)
