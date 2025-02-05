package com.thomas.management.domain.properties

data class AuthenticationProperties(
    val accessDurationSeconds: Long,
    val refreshDurationSeconds: Long,
)
