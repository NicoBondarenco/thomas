package com.thomas.authentication.domain.properties

data class AuthenticationProperties(
    val accessDurationSeconds: Long = 0,
    val refreshDurationSeconds: Long = 0,
    val resetDurationSeconds: Long = 0,
    val minimumPasswordLength: Int = 0,
)
