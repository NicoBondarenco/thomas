package com.thomas.authentication.data.entity

import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.security.SecurityRole
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID
import java.util.UUID.randomUUID

data class GroupAuthenticationEntity(
    override val id: UUID = randomUUID(),
    val groupName: String,
    val groupDescription: String? = null,
    val isActive: Boolean = true,
    val creatorId: UUID = currentUser.userId,
    val createdAt: OffsetDateTime = now(UTC),
    val updatedAt: OffsetDateTime = now(UTC),
    val groupRoles: Set<SecurityRole>,
) : BaseEntity<GroupAuthenticationEntity>()
