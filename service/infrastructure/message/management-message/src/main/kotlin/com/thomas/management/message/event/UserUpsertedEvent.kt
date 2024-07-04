package com.thomas.management.message.event

import com.thomas.core.model.general.Gender
import com.thomas.core.model.security.SecurityRole
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

data class UserUpsertedEvent(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val mainEmail: String,
    val documentNumber: String,
    val phoneNumber: String?,
    val profilePhoto: String?,
    val birthDate: LocalDate?,
    val userGender: Gender?,
    val isActive: Boolean,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val userRoles: List<SecurityRole>,
    val userGroups: List<UUID>,
)
