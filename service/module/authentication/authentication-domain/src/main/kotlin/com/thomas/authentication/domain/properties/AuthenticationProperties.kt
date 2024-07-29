package com.thomas.authentication.domain.properties

data class AuthenticationProperties(
    val accessDurationSeconds: Long = 0,
    val refreshDurationSeconds: Long = 0,
)
