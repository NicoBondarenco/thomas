package com.thomas.authentication.jwt.auth0.util

import com.thomas.core.model.general.Gender.CIS_MALE
import com.thomas.core.model.security.SecurityGroup
import com.thomas.core.model.security.SecurityRole.ROLE_FINANCE_DATA
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_CREATE
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_DELETE
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_READ
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_UPDATE
import com.thomas.core.model.security.SecurityRole.ROLE_USER_CREATE
import com.thomas.core.model.security.SecurityRole.ROLE_USER_READ
import com.thomas.core.model.security.SecurityRole.ROLE_USER_UPDATE
import com.thomas.core.model.security.SecurityUser
import java.time.LocalDate
import java.util.UUID.randomUUID

val activeGroupOne = SecurityGroup(
    groupId = randomUUID(),
    groupName = "Group One",
    groupRoles = listOf(ROLE_USER_CREATE, ROLE_USER_UPDATE, ROLE_USER_READ),
)

val activeGroupTwo = SecurityGroup(
    groupId = randomUUID(),
    groupName = "Group Two",
    groupRoles = listOf(ROLE_GROUP_CREATE, ROLE_GROUP_UPDATE, ROLE_GROUP_READ, ROLE_GROUP_DELETE),
)

val inactiveGroupThree = SecurityGroup(
    groupId = randomUUID(),
    groupName = "Group Three",
    groupRoles = listOf(ROLE_FINANCE_DATA),
)

val activeUser = SecurityUser(
    userId = randomUUID(),
    firstName = "Security",
    lastName = "User",
    mainEmail = "security.user@test.com",
    phoneNumber = "16988776655",
    profilePhoto = null,
    birthDate = LocalDate.now(),
    userGender = CIS_MALE,
    isActive = true,
    userRoles = listOf(ROLE_USER_CREATE, ROLE_USER_UPDATE, ROLE_USER_READ),
    userGroups = listOf(activeGroupOne, activeGroupTwo),
)

val activeUserWithoutGroups = activeUser.copy(
    userId = randomUUID(),
    userGroups = listOf()
)

val inactiveUser = activeUser.copy(
    userId = randomUUID(),
    isActive = false,
)
