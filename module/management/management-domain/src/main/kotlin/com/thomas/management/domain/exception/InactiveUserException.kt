package com.thomas.management.domain.exception

import com.thomas.core.exception.ApplicationException
import com.thomas.core.exception.ErrorType.UNAUTHORIZED_ACTION
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementAuthenticationLoginUserDataInactiveUser

class InactiveUserException : ApplicationException(
    message = managementAuthenticationLoginUserDataInactiveUser(),
    type = UNAUTHORIZED_ACTION,
)
