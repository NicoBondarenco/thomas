package com.thomas.management.domain.properties

data class PasswordProperties(
    val tokenValidityMinutes: Long,
    val resetEmailSubject: String,
    val resetEmailModel: String,
    val tokenValidityPattern: String,
)
