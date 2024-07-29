package com.thomas.authentication.data.entity

import com.thomas.core.model.general.Gender
import com.thomas.core.model.security.SecurityRole
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

data class UserAuthenticationEntity(
    override val id: UUID,
    override val firstName: String,
    override val lastName: String,
    override val mainEmail: String,
    override val documentNumber: String,
    override val phoneNumber: String?,
    override val profilePhoto: String?,
    override val birthDate: LocalDate?,
    override val userGender: Gender?,
    override val isActive: Boolean,
    override val passwordHash: String,
    override val passwordSalt: String,
    override val userRoles: Set<SecurityRole>,
    override val createdAt: OffsetDateTime,
    override val updatedAt: OffsetDateTime,
    val groupsIds: Set<UUID>,
) : UserAuthenticationBaseEntity()
