package com.thomas.management.domain.model.request

data class ResetPasswordRequest(
    val newPassword: String,
    val resetToken: String,
)
