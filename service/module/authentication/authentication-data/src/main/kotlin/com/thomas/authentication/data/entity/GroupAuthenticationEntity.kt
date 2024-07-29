package com.thomas.authentication.data.entity

import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.security.SecurityRole
import java.time.OffsetDateTime
import java.util.UUID

data class GroupAuthenticationEntity(
    override val id: UUID,
    val groupName: String,
    val groupDescription: String?,
    val isActive: Boolean,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val groupRoles: Set<SecurityRole>,
) : BaseEntity<GroupAuthenticationEntity>()
