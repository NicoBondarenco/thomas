package com.thomas.management.domain.model.request

import com.thomas.core.model.general.Gender
import com.thomas.core.model.security.SecurityRole
import java.time.LocalDate
import java.util.UUID

data class UserUpdateRequest(
    val firstName: String,
    val lastName: String,
    val documentNumber: String,
    val phoneNumber: String?,
    val birthDate: LocalDate?,
    val userGender: Gender?,
    val isActive: Boolean,
    val userRoles: List<SecurityRole>,
    val userGroups: List<UUID>,
)
