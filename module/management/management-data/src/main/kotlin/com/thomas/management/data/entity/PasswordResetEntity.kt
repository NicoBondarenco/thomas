package com.thomas.management.data.entity

import com.thomas.core.model.entity.BaseEntity
import com.thomas.management.data.entity.info.CreationInfo
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID
import java.util.UUID.randomUUID

data class PasswordResetEntity(
    override val id: UUID = randomUUID(),
    val userId: UUID,
    val resetToken: String?,
    val validUntil: OffsetDateTime,
    override val createdAt: OffsetDateTime = now(UTC),
    override val updatedAt: OffsetDateTime = now(UTC),
) : BaseEntity<PasswordResetEntity>(), CreationInfo
