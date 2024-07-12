package com.thomas.management.domain.exception

import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.NOT_FOUND
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserSearchNotFoundErrorMessage
import java.util.UUID

class UserNotFoundException(
    id: UUID
) : DetailedException(
    message = managementUserSearchNotFoundErrorMessage(id),
    type = NOT_FOUND,
)
