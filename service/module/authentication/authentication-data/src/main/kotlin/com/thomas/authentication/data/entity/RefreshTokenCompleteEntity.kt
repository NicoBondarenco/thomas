package com.thomas.authentication.data.entity

import java.time.OffsetDateTime
import java.util.UUID

data class RefreshTokenCompleteEntity(
    override val id: UUID,
    override val refreshToken: String,
    override val validUntil: OffsetDateTime,
    override val createdAt: OffsetDateTime,
    val userAuthentication: UserAuthenticationCompleteEntity
) : RefreshTokenBaseEntity()
