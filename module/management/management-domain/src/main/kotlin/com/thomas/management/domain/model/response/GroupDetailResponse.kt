package com.thomas.management.domain.model.response

import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.core.model.security.SecurityUnitRole
import java.time.OffsetDateTime
import java.util.UUID

data class GroupDetailResponse(
    val id: UUID,
    val groupName: String,
    val groupDescription: String?,
    val groupOrganization: OrganizationResponse,
    val organizationRoles: Set<SecurityOrganizationRole>,
    val groupUnits: Map<UUID, Set<SecurityUnitRole>>,
    val isActive: Boolean,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
