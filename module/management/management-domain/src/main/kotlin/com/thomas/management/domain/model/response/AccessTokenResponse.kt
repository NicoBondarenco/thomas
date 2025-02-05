package com.thomas.management.domain.model.response

data class AccessTokenResponse(
    val idToken: String,
    val refreshToken: String,
    val durationSeconds: Long,
)
