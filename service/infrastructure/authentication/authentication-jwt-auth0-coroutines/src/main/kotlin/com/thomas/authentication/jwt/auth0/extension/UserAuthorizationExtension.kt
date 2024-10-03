package com.thomas.authentication.jwt.auth0.extension

import com.thomas.authentication.jwt.auth0.data.GroupAuthentication
import com.thomas.authentication.jwt.auth0.data.UserAuthentication
import com.thomas.core.model.security.SecurityGroup
import com.thomas.core.model.security.SecurityUser

internal fun UserAuthentication.toSecurityUser() = SecurityUser(
    userId = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    mainEmail = this.mainEmail,
    phoneNumber = this.phoneNumber,
    profilePhoto = this.profilePhoto,
    birthDate = this.birthDate,
    userGender = this.userGender,
    isActive = this.isActive,
    userRoles = this.userRoles,
    userGroups = this.userGroups.toSecurityGroups(),
)

private fun List<GroupAuthentication>.toSecurityGroups() = this.filter {
    it.isActive
}.map {
    it.toSecurityGroup()
}

private fun GroupAuthentication.toSecurityGroup() = SecurityGroup(
    groupId = this.id,
    groupName = this.groupName,
    groupRoles = this.groupRoles,
)
