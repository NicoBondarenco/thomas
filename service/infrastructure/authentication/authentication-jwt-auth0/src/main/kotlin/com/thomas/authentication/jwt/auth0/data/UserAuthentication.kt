package com.thomas.authentication.jwt.auth0.data

import com.thomas.core.model.general.Gender
import com.thomas.core.model.general.UserProfile
import com.thomas.core.model.security.SecurityRole
import java.time.LocalDate
import java.util.UUID

data class UserAuthentication(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val mainEmail: String,
    val phoneNumber: String? = null,
    val profilePhoto: String? = null,
    val birthDate: LocalDate? = null,
    val userGender: Gender? = null,
    val userProfile: UserProfile,
    val isActive: Boolean,
    val userRoles: List<SecurityRole> = listOf(),
    val userGroups: List<GroupAuthentication> = listOf()
)
