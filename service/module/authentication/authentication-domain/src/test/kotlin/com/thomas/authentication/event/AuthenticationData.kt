package com.thomas.authentication.event

import com.thomas.authentication.data.entity.PasswordResetCompleteEntity
import com.thomas.authentication.data.entity.RefreshTokenCompleteEntity
import com.thomas.authentication.domain.model.request.LoginRequest
import com.thomas.core.random.randomString
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID.randomUUID

val loginRequest: LoginRequest
    get() = LoginRequest(
        username = randomString(15),
        password = randomString(15),
    )

val refreshTokenCompleteEntity: RefreshTokenCompleteEntity
    get() = randomUUID().let {
        RefreshTokenCompleteEntity(
            id = it,
            refreshToken = randomString(15),
            validUntil = now(UTC).plusDays(1),
            createdAt = now(UTC),
            userAuthentication = userCompleteAuthentication.copy(id = it),
        )
    }

val resetCompleteEntity: PasswordResetCompleteEntity
    get() = randomUUID().let {
        PasswordResetCompleteEntity(
            id = it,
            resetToken = randomString(15),
            validUntil = now(UTC).plusDays(1),
            createdAt = now(UTC),
            userAuthentication = userAuthentication.copy(id = it),
        )
    }
