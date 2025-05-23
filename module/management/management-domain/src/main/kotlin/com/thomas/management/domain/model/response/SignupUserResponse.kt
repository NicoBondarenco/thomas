package com.thomas.management.domain.model.response

import com.thomas.core.model.general.Gender
import com.thomas.core.model.general.Race
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

data class SignupUserResponse(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val documentNumber: String,
    val profilePhoto: String?,
    val userGender: Gender?,
    val userRace: Race?,
    val birthDate: LocalDate?,
    val mainEmail: String,
    val mainPhone: String,
    val isActive: Boolean,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
