package com.thomas.contract.messaging.management.group

import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.core.model.security.SecurityUnitRole
import java.time.OffsetDateTime
import java.util.UUID

data class GroupCreatedEvent(
    val id: UUID,
    val groupName: String,
    val groupDescription: String?,
    val groupOrganization: UUID,
    val organizationRoles: Set<SecurityOrganizationRole>,
    val groupUnits: Map<UUID, Set<SecurityUnitRole>>,
    val isActive: Boolean,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
