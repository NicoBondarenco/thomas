package com.thomas.authentication.jwt.auth0.util

import com.thomas.authentication.jwt.auth0.data.GroupAuthentication
import com.thomas.authentication.jwt.auth0.data.UserAuthentication

internal val activeGroupAuthenticationOne = GroupAuthentication(
    id = activeGroupOne.groupId,
    groupName = activeGroupOne.groupName,
    groupDescription = "Description One",
    isActive = true,
    groupRoles = activeGroupOne.groupRoles,
)

internal val activeGroupAuthenticationTwo = GroupAuthentication(
    id = activeGroupTwo.groupId,
    groupName = activeGroupTwo.groupName,
    groupDescription = "Description Two",
    isActive = true,
    groupRoles = activeGroupTwo.groupRoles,
)

internal val inactiveGroupAuthenticationThree = GroupAuthentication(
    id = inactiveGroupThree.groupId,
    groupName = inactiveGroupThree.groupName,
    groupDescription = "Description Three",
    isActive = false,
    groupRoles = inactiveGroupThree.groupRoles,
)

internal val activeUserAuthentication = UserAuthentication(
    id = activeUser.userId,
    firstName = activeUser.firstName,
    lastName = activeUser.lastName,
    mainEmail = activeUser.mainEmail,
    phoneNumber = activeUser.phoneNumber,
    profilePhoto = activeUser.profilePhoto,
    birthDate = activeUser.birthDate,
    userGender = activeUser.userGender,
    userProfile = activeUser.userProfile,
    isActive = activeUser.isActive,
    userRoles = activeUser.userRoles,
    userGroups = listOf(
        activeGroupAuthenticationOne,
        activeGroupAuthenticationTwo,
        inactiveGroupAuthenticationThree,
    ),
)

internal val activeUserAuthenticationWithoutGroups = UserAuthentication(
    id = activeUserWithoutGroups.userId,
    firstName = activeUserWithoutGroups.firstName,
    lastName = activeUserWithoutGroups.lastName,
    mainEmail = activeUserWithoutGroups.mainEmail,
    phoneNumber = activeUserWithoutGroups.phoneNumber,
    profilePhoto = activeUserWithoutGroups.profilePhoto,
    birthDate = activeUserWithoutGroups.birthDate,
    userGender = activeUserWithoutGroups.userGender,
    userProfile = activeUserWithoutGroups.userProfile,
    isActive = activeUserWithoutGroups.isActive,
    userRoles = activeUserWithoutGroups.userRoles,
    userGroups = listOf(),
)

internal val inactiveUserAuthentication = UserAuthentication(
    id = inactiveUser.userId,
    firstName = inactiveUser.firstName,
    lastName = inactiveUser.lastName,
    mainEmail = inactiveUser.mainEmail,
    phoneNumber = inactiveUser.phoneNumber,
    profilePhoto = inactiveUser.profilePhoto,
    birthDate = inactiveUser.birthDate,
    userGender = inactiveUser.userGender,
    userProfile = inactiveUser.userProfile,
    isActive = inactiveUser.isActive,
    userRoles = inactiveUser.userRoles,
    userGroups = listOf(
        activeGroupAuthenticationOne,
        activeGroupAuthenticationTwo,
        inactiveGroupAuthenticationThree
    ),
)

internal val authenticationUsers = listOf(
    activeUserAuthentication,
    activeUserAuthenticationWithoutGroups,
    inactiveUserAuthentication,
)
