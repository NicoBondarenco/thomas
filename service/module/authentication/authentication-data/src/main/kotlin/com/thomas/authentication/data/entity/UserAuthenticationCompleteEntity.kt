package com.thomas.authentication.data.entity

import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.model.general.Gender
import com.thomas.core.model.security.SecurityRole
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID
import java.util.UUID.randomUUID

data class UserAuthenticationCompleteEntity(
    override val id: UUID = randomUUID(),
    override val firstName: String,
    override val lastName: String,
    override val mainEmail: String,
    override val documentNumber: String,
    override val phoneNumber: String? = null,
    override val profilePhoto: String? = null,
    override val birthDate: LocalDate? = null,
    override val userGender: Gender? = null,
    override val isActive: Boolean = true,
    override val creatorId: UUID = currentUser.userId,
    override val passwordHash: String,
    override val passwordSalt: String,
    override val userRoles: Set<SecurityRole>,
    override val createdAt: OffsetDateTime = now(UTC),
    override val updatedAt: OffsetDateTime = now(UTC),
    val userGroups: Set<GroupAuthenticationEntity>,
) : UserAuthenticationBaseEntity()
