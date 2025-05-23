package com.thomas.management.domain.exception

import com.thomas.core.exception.ApplicationException
import com.thomas.core.exception.ErrorType.NOT_FOUND
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserSearchNotFoundErrorMessage
import java.util.UUID

class UserNotFoundException(
    id: UUID,
) : ApplicationException(
    message = managementUserSearchNotFoundErrorMessage(id),
    type = NOT_FOUND,
)
