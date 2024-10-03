package com.thomas.management.domain.exception

import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.NOT_FOUND
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUserGroupsNotFound
import java.util.UUID

class GroupListNotFoundException(
    ids: Set<UUID>
) : DetailedException(
    message = managementUserValidationUserGroupsNotFound(ids.joinToString(", ") { it.toString() }),
    type = NOT_FOUND,
)
