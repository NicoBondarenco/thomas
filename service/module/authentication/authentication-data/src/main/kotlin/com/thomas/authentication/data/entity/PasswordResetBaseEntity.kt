package com.thomas.authentication.data.entity

import com.thomas.core.model.entity.BaseEntity
import java.time.OffsetDateTime

abstract class PasswordResetBaseEntity : BaseEntity<PasswordResetBaseEntity>() {

    abstract val resetToken: String
    abstract val validUntil: OffsetDateTime
    abstract val createdAt: OffsetDateTime

}
