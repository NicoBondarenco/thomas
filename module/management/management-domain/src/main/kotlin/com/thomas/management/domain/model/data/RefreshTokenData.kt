package com.thomas.management.domain.model.data

import java.time.OffsetDateTime
import java.util.UUID

data class RefreshTokenData(
    val securityUsername: String,
    val organizationId: UUID,
    val refreshDuration: Long,
    val validUntil: OffsetDateTime,
)
