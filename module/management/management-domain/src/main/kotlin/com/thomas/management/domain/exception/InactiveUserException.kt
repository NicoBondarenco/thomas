package com.thomas.management.domain.exception

import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.UNAUTHORIZED_ACTION
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementAuthenticationLoginUserDataInactiveUser

class InactiveUserException : DetailedException(
    message = managementAuthenticationLoginUserDataInactiveUser(),
    type = UNAUTHORIZED_ACTION,
)
