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
import org.bson.Document

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

internal val usersDocuments: List<Document> = listOf(
    foundUser.toTestDocument()
)

fun SecurityUser.toTestDocument(): Document = Document().apply {
    this["id"] = this@toTestDocument.userId.toString()
    this["firstName"] = this@toTestDocument.firstName
    this["lastName"] = this@toTestDocument.lastName
    this["mainEmail"] = this@toTestDocument.mainEmail
    this["phoneNumber"] = this@toTestDocument.phoneNumber
    this["profilePhoto"] = this@toTestDocument.profilePhoto
    this["birthDate"] = this@toTestDocument.birthDate?.atTime(OffsetTime.MIN)?.let { Date(it.toInstant().toEpochMilli()) }
    this["userGender"] = this@toTestDocument.userGender?.name
    this["userProfile"] = this@toTestDocument.userProfile.name
    this["isActive"] = this@toTestDocument.isActive
    this["userRoles"] = this@toTestDocument.userRoles.map { it.name }
    this["groupList"] = this@toTestDocument.userGroups.map { it.toTestDocument() }
}

fun SecurityGroup.toTestDocument(): Document = Document().apply {
    this["id"] = this@toTestDocument.groupId.toString()
    this["isActive"] = activeGroups[this@toTestDocument.groupId] ?: false
    this["groupName"] = this@toTestDocument.groupName
    this["groupRoles"] = this@toTestDocument.groupRoles.map { it.name }
}