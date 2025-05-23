package com.thomas.management.domain.model.request

import com.thomas.core.aspect.MaskField
import com.thomas.core.model.general.Gender
import com.thomas.core.model.general.Race
import java.time.LocalDate

data class SignupUserRequest(
    val firstName: String,
    val lastName: String,
    val documentNumber: String,
    val userGender: Gender?,
    val userRace: Race?,
    val birthDate: LocalDate?,
    @MaskField val userPassword: String,
    val mainEmail: String,
    val mainPhone: String,
)
