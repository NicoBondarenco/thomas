package com.thomas.management.domain.model.response

import java.time.OffsetDateTime
import java.util.UUID

data class GroupPageResponse(
    val id: UUID,
    val groupName: String,
    val groupDescription: String?,
    val isActive: Boolean,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
