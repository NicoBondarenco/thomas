package com.thomas.authentication.domain.properties

data class AuthenticationProperties(
    val accessDurationSeconds: Long,
    val refreshDurationSeconds: Long,
    val resetDurationSeconds: Long,
    val minimumPasswordLength: Int,
)
