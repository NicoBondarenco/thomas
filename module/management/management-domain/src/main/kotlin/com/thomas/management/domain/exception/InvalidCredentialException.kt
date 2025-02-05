package com.thomas.management.domain.exception

import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.UNAUTHORIZED_ACTION
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementAuthenticationLoginCredentialDataInvalidCredentials

class InvalidCredentialException : DetailedException(
    message = managementAuthenticationLoginCredentialDataInvalidCredentials(),
    type = UNAUTHORIZED_ACTION,
)
