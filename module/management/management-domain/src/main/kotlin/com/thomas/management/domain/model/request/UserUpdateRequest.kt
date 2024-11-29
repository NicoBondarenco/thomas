package com.thomas.management.domain.model.request

import com.thomas.core.model.general.Gender
import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.core.model.security.SecurityUnitRole
import java.time.LocalDate
import java.util.UUID

data class UserUpdateRequest(
    val firstName: String,
    val lastName: String,
    val documentNumber: String,
    val userGender: Gender?,
    val birthDate: LocalDate?,
    val mainPhone: String,
    val isActive: Boolean,
    val organizationRoles: Set<SecurityOrganizationRole>,
    override val userGroups: Set<UUID>,
    override val userUnits: Map<UUID, Set<SecurityUnitRole>>,
): UserRequest
