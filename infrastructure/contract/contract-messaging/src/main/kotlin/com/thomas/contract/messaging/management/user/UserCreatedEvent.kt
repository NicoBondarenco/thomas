package com.thomas.contract.messaging.management.user

import com.thomas.core.aspect.MaskField
import com.thomas.core.model.general.Gender
import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.core.model.security.SecurityUnitRole
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

data class UserCreatedEvent(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    @MaskField val documentNumber: String,
    val profilePhoto: String?,
    val userGender: Gender?,
    val birthDate: LocalDate?,
    val userOrganization: UUID,
    val organizationRoles: Set<SecurityOrganizationRole>,
    val mainEmail: String,
    val mainPhone: String,
    val isActive: Boolean,
    val userGroups: Set<UUID>,
    val userUnits: Map<UUID, Set<SecurityUnitRole>>,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
