package com.thomas.management.domain.model.response

import com.thomas.core.model.security.SecurityRole
import java.time.OffsetDateTime
import java.util.UUID

data class GroupDetailResponse(
    val id: UUID,
    val groupName: String,
    val groupDescription: String?,
    val isActive: Boolean,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val groupRoles: Set<SecurityRole>,
)
