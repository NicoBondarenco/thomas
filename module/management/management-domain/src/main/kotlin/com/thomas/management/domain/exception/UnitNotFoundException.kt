package com.thomas.management.domain.exception

import com.thomas.core.exception.ApplicationException
import com.thomas.core.exception.ErrorType.NOT_FOUND
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUnitSearchNotFoundErrorMessage
import java.util.UUID

class UnitNotFoundException(
    id: UUID,
) : ApplicationException(
    message = managementUnitSearchNotFoundErrorMessage(id),
    type = NOT_FOUND,
)
