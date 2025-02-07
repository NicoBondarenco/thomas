package com.thomas.management.data.entity

import com.thomas.core.util.StringUtils.randomString
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID.randomUUID

val passwordResetEntity: PasswordResetEntity
    get() = PasswordResetEntity(
        id = randomUUID(),
        userId = randomUUID(),
        resetToken = randomString(length = 30, spaces = false),
        expiresOn = now(UTC).plusHours(1),
    )