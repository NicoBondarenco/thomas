package com.thomas.authentication.domain.model.request

data class LoginRequest(
    val username: String,
    val password: String,
)
