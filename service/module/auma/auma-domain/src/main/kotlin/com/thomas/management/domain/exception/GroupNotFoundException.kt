package com.thomas.management.domain.exception

import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.NOT_FOUND
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementGroupSearchNotFoundErrorMessage
import java.util.UUID

class GroupNotFoundException(
    id: UUID
) : DetailedException(
    message = managementGroupSearchNotFoundErrorMessage(id),
    type = NOT_FOUND,
)
