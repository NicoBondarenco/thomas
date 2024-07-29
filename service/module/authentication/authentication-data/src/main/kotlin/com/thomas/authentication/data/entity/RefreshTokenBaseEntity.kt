package com.thomas.authentication.data.entity

import com.thomas.core.model.entity.BaseEntity
import java.time.OffsetDateTime

abstract class RefreshTokenBaseEntity : BaseEntity<RefreshTokenBaseEntity>() {

    abstract val refreshToken: String
    abstract val validUntil: OffsetDateTime
    abstract val createdAt: OffsetDateTime

}
