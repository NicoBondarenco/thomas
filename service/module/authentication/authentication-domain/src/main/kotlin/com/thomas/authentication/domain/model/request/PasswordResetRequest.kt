package com.thomas.authentication.domain.model.request

data class PasswordResetRequest(
    val newPassword: String,
    val resetToken: String,
)