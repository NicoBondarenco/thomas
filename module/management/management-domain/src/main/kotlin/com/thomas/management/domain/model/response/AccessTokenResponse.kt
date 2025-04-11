package com.thomas.management.domain.model.response

data class AccessTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val accessDuration: Long,
    val refreshDuration: Long,
)
