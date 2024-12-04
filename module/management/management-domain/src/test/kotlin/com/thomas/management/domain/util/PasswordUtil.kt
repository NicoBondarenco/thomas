package com.thomas.management.domain.util

import com.thomas.core.util.StringUtils.randomEmail
import com.thomas.core.util.StringUtils.randomPassword
import com.thomas.core.util.StringUtils.randomString
import com.thomas.management.data.entity.PasswordResetEntity
import com.thomas.management.domain.model.request.ChangePasswordRequest
import com.thomas.management.domain.model.request.ForgotPasswordRequest
import com.thomas.management.domain.model.request.ResetPasswordRequest
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID.randomUUID

internal val passwordResetEntity: PasswordResetEntity
    get() = PasswordResetEntity(
        id = randomUUID(),
        userId = randomUUID(),
        resetToken = randomString(length = 30, spaces = false),
        expiresOn = now(UTC).plusHours(1),
    )

internal val changePasswordRequest: ChangePasswordRequest
    get() = ChangePasswordRequest(
        newPassword = randomPassword()
    )

internal val resetPasswordRequest: ResetPasswordRequest
    get() = ResetPasswordRequest(
        newPassword = randomPassword(),
        resetToken = randomString(length = 30, spaces = false),
    )

internal val forgotPasswordRequest: ForgotPasswordRequest
    get() = ForgotPasswordRequest(
        mainEmail = randomEmail()
    )
