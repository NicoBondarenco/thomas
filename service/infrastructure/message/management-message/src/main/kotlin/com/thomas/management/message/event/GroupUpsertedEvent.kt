package com.thomas.management.message.event

import com.thomas.core.model.security.SecurityRole
import java.time.OffsetDateTime
import java.util.UUID

data class GroupUpsertedEvent(
    val id: UUID,
    val groupName: String,
    val groupDescription: String?,
    val isActive: Boolean,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val groupRoles: List<SecurityRole>,
)
