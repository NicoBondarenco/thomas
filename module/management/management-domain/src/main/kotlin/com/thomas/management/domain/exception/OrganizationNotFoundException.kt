package com.thomas.management.domain.exception

import com.thomas.core.exception.ApplicationException
import com.thomas.core.exception.ErrorType.NOT_FOUND
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementOrganizationSearchNotFoundErrorMessage
import java.util.UUID

class OrganizationNotFoundException(
    id: UUID,
) : ApplicationException(
    message = managementOrganizationSearchNotFoundErrorMessage(id),
    type = NOT_FOUND,
)
