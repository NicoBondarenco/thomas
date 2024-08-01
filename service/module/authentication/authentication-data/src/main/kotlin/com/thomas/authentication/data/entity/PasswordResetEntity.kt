package com.thomas.authentication.data.entity

import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID

data class PasswordResetEntity(
    override val id: UUID,
    override val resetToken: String,
    override val validUntil: OffsetDateTime,
    override val createdAt: OffsetDateTime = now(UTC),
) : PasswordResetBaseEntity()
