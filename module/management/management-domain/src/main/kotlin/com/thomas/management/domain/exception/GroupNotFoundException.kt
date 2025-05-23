package com.thomas.management.domain.exception

import com.thomas.core.exception.ApplicationException
import com.thomas.core.exception.ErrorType.NOT_FOUND
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementGroupSearchNotFoundErrorMessage
import java.util.UUID

class GroupNotFoundException(
    id: UUID,
) : ApplicationException(
    message = managementGroupSearchNotFoundErrorMessage(id),
    type = NOT_FOUND,
)
