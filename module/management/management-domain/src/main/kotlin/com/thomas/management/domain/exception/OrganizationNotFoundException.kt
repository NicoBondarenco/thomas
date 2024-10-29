package com.thomas.management.domain.exception

import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.NOT_FOUND
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementOrganizationSearchNotFoundErrorMessage
import java.util.UUID

class OrganizationNotFoundException(
    id: UUID,
) : DetailedException(
    message = managementOrganizationSearchNotFoundErrorMessage(id),
    type = NOT_FOUND,
)
