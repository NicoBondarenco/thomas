package com.thomas.jwt.auth0.util

import com.thomas.core.model.general.UserProfile.COMMON
import com.thomas.core.model.security.SecurityGroup
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_CREATE
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_READ
import com.thomas.core.model.security.SecurityRole.ROLE_USER_CREATE
import com.thomas.core.model.security.SecurityRole.ROLE_USER_READ
import com.thomas.core.model.security.SecurityUser
import java.time.OffsetTime
import java.util.Date
import java.util.UUID

internal val activeGroups = mutableMapOf<UUID, Boolean>()

internal val foundUser = SecurityUser(
    userId = UUID.randomUUID(),
    firstName = "Security",
    lastName = "User",
    mainEmail = "security.user@example.com",
    userProfile = COMMON,
    isActive = true,
    userRoles = listOf(ROLE_USER_READ, ROLE_GROUP_READ),
    userGroups = listOf(
        SecurityGroup(
            groupId = UUID.randomUUID(),
            groupName = "Group Active",
            groupRoles = listOf(ROLE_USER_READ, ROLE_USER_CREATE),
        ).apply { activeGroups[this.groupId] = true },
        SecurityGroup(
            groupId = UUID.randomUUID(),
            groupName = "Group Inactive",
            groupRoles = listOf(ROLE_GROUP_CREATE),
        ).apply { activeGroups[this.groupId] = false },
    ),
)

internal val validUser = foundUser.copy(
    userId = UUID.randomUUID()
)

internal val users: List<SecurityUser> = listOf(
    foundUser,
    validUser,
)
