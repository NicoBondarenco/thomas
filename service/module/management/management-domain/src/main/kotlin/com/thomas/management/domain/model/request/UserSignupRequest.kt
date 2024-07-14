package com.thomas.management.domain.model.request

import com.thomas.core.model.general.Gender
import java.time.LocalDate

data class UserSignupRequest(
    val firstName: String,
    val lastName: String,
    val mainEmail: String,
    val documentNumber: String,
    val phoneNumber: String?,
    val birthDate: LocalDate?,
    val userGender: Gender?,
)
