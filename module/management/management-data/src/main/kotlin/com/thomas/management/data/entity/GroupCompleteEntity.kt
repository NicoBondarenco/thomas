package com.thomas.management.data.entity

import com.thomas.core.model.security.SecurityOrganizationRole
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID
import java.util.UUID.randomUUID

data class GroupCompleteEntity(
    override val id: UUID = randomUUID(),
    override val groupName: String,
    override val groupDescription: String?,
    override val groupOrganization: OrganizationEntity,
    override val organizationRoles: Set<SecurityOrganizationRole>,
    override val isActive: Boolean = true,
    override val createdAt: OffsetDateTime = now(UTC),
    override val updatedAt: OffsetDateTime = now(UTC),
    val groupUnits: Set<UnitRoleEntity> = emptySet()
) : GroupEntity() {

    init {
        validate()
    }

}
