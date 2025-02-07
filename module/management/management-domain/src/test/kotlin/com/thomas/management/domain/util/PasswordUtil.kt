package com.thomas.management.domain.util

import com.thomas.core.util.StringUtils.randomEmail
import com.thomas.core.util.StringUtils.randomPassword
import com.thomas.core.util.StringUtils.randomString
import com.thomas.management.domain.model.request.ChangePasswordRequest
import com.thomas.management.domain.model.request.ForgotPasswordRequest
import com.thomas.management.domain.model.request.ResetPasswordRequest

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
