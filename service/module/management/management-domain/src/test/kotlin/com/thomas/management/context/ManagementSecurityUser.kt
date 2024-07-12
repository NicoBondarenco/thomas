package com.thomas.management.context

import com.thomas.core.model.general.Gender.CIS_MALE
import com.thomas.core.model.security.SecurityRole.ROLE_USER_CREATE
import com.thomas.core.model.security.SecurityRole.ROLE_USER_READ
import com.thomas.core.model.security.SecurityRole.ROLE_USER_UPDATE
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_CREATE
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_READ
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_UPDATE
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_DELETE
import com.thomas.core.model.security.SecurityUser
import java.time.LocalDate
import java.util.UUID.randomUUID

val userWithoutRole: SecurityUser
    get() = SecurityUser(
        userId = randomUUID(),
        firstName = "Security",
        lastName = "User",
        mainEmail = "security.user@test.com",
        phoneNumber = "16988776655",
        profilePhoto = null,
        birthDate = LocalDate.now(),
        userGender = CIS_MALE,
        isActive = true,
        userRoles = listOf(),
        userGroups = listOf(),
    )

val userWithUserReadRole: SecurityUser
    get() = userWithoutRole.copy(
        userRoles = listOf(ROLE_USER_READ),
    )

val userWithUserCreateRole: SecurityUser
    get() = userWithoutRole.copy(
        userRoles = listOf(ROLE_USER_CREATE),
    )

val userWithUserUpdateRole: SecurityUser
    get() = userWithoutRole.copy(
        userRoles = listOf(ROLE_USER_UPDATE),
    )

val userWithGroupReadRole: SecurityUser
    get() = userWithoutRole.copy(
        userRoles = listOf(ROLE_GROUP_READ),
    )

val userWithGroupCreateRole: SecurityUser
    get() = userWithoutRole.copy(
        userRoles = listOf(ROLE_GROUP_CREATE),
    )

val userWithGroupUpdateRole: SecurityUser
    get() = userWithoutRole.copy(
        userRoles = listOf(ROLE_GROUP_UPDATE),
    )

val userWithGroupDeleteRole: SecurityUser
    get() = userWithoutRole.copy(
        userRoles = listOf(ROLE_GROUP_DELETE),
    )
