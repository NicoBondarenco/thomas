package com.thomas.authentication.domain.model.response

data class AccessTokenResponse(
    val idToken: String,
    val refreshToken: String,
    val expiresIn: Long,
)
