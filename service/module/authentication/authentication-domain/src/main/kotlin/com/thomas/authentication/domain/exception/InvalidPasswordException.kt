package com.thomas.authentication.domain.exception

import com.thomas.authentication.domain.i18n.AuthenticationDomainMessageI18N.authenticationUserAuthenticationInvalidPasswordMinimumRequirements
import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.NOT_FOUND

class InvalidPasswordException : DetailedException(
    message = authenticationUserAuthenticationInvalidPasswordMinimumRequirements(),
    type = NOT_FOUND,
)
