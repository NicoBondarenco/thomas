package com.thomas.management.domain.exception

import com.thomas.core.exception.ApplicationException
import com.thomas.core.exception.ErrorType.UNAUTHORIZED_ACTION
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementAuthenticationLoginCredentialDataInvalidCredentials

class InvalidCredentialException : ApplicationException(
    message = managementAuthenticationLoginCredentialDataInvalidCredentials(),
    type = UNAUTHORIZED_ACTION,
)
