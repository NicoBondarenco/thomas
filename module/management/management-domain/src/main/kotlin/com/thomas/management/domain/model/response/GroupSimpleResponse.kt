package com.thomas.management.domain.model.response

import java.time.OffsetDateTime
import java.util.UUID

data class GroupSimpleResponse(
    val id: UUID,
    val groupName: String,
    val groupDescription: String?,
    val groupOrganization: OrganizationResponse,
    val isActive: Boolean,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
