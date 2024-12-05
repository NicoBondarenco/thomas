package com.thomas.management.domain.model.request

import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.core.model.security.SecurityUnitRole
import java.util.UUID

data class GroupUpsertRequest(
    val groupName: String,
    val groupDescription: String?,
    val organizationRoles: Set<SecurityOrganizationRole>,
    val groupUnits: Map<UUID, Set<SecurityUnitRole>>,
    val isActive: Boolean,
)
