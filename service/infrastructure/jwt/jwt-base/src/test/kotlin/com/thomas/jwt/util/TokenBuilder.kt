package com.thomas.jwt.util

import com.thomas.core.model.general.Gender
import com.thomas.core.model.general.UserProfile
import com.thomas.core.model.security.SecurityUser
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.UUID.randomUUID


val user = SecurityUser(
    randomUUID(),
    "Security",
    "User",
    "security.user@test.com",
    "16988776655",
    null,
    LocalDate.now(ZoneOffset.UTC),
    Gender.CIS_MALE,
    UserProfile.ADMINISTRATOR,
    true,
    listOf(),
    listOf(),
)

val userToken = randomUUID().toString() to user
val inactiveToken = randomUUID().toString() to user.copy(isActive = false)
val invalidToken = randomUUID().toString() to user.copy()
