package com.thomas.authentication.jwt.auth0.util

import org.bson.Document

private const val ID_PROPERTY = "_id"

internal val activeGroupDocumentOne = Document().also {
    it[ID_PROPERTY] = activeGroupAuthenticationOne.id
    it["id"] = activeGroupAuthenticationOne.id
    it["groupName"] = activeGroupAuthenticationOne.groupName
    it["groupDescription"] = activeGroupAuthenticationOne.groupDescription
    it["isActive"] = activeGroupAuthenticationOne.isActive
    it["groupRoles"] = activeGroupAuthenticationOne.groupRoles
}

internal val activeGroupDocumentTwo = Document().also {
    it[ID_PROPERTY] = activeGroupAuthenticationTwo.id
    it["id"] = activeGroupAuthenticationTwo.id
    it["groupName"] = activeGroupAuthenticationTwo.groupName
    it["groupDescription"] = activeGroupAuthenticationTwo.groupDescription
    it["isActive"] = activeGroupAuthenticationTwo.isActive
    it["groupRoles"] = activeGroupAuthenticationTwo.groupRoles
}

internal val inactiveGroupDocumentThree = Document().also {
    it[ID_PROPERTY] = inactiveGroupAuthenticationThree.id
    it["id"] = inactiveGroupAuthenticationThree.id
    it["groupName"] = inactiveGroupAuthenticationThree.groupName
    it["groupDescription"] = inactiveGroupAuthenticationThree.groupDescription
    it["isActive"] = inactiveGroupAuthenticationThree.isActive
    it["groupRoles"] = inactiveGroupAuthenticationThree.groupRoles
}

internal val activeUserDocument = Document().also {
    it[ID_PROPERTY] = activeUserAuthentication.id
    it["id"] = activeUserAuthentication.id
    it["firstName"] = activeUserAuthentication.firstName
    it["lastName"] = activeUserAuthentication.lastName
    it["mainEmail"] = activeUserAuthentication.mainEmail
    it["phoneNumber"] = activeUserAuthentication.phoneNumber
    it["profilePhoto"] = activeUserAuthentication.profilePhoto
    it["birthDate"] = activeUserAuthentication.birthDate
    it["userGender"] = activeUserAuthentication.userGender
    it["userProfile"] = activeUserAuthentication.userProfile
    it["isActive"] = activeUserAuthentication.isActive
    it["userRoles"] = activeUserAuthentication.userRoles
    it["groupsIds"] = activeUserAuthentication.userGroups.map { g -> g.id }
}

internal val activeUserWithoutGroupsDocument = Document().also {
    it[ID_PROPERTY] = activeUserAuthenticationWithoutGroups.id
    it["id"] = activeUserAuthenticationWithoutGroups.id
    it["firstName"] = activeUserAuthenticationWithoutGroups.firstName
    it["lastName"] = activeUserAuthenticationWithoutGroups.lastName
    it["mainEmail"] = activeUserAuthenticationWithoutGroups.mainEmail
    it["phoneNumber"] = activeUserAuthenticationWithoutGroups.phoneNumber
    it["profilePhoto"] = activeUserAuthenticationWithoutGroups.profilePhoto
    it["birthDate"] = activeUserAuthenticationWithoutGroups.birthDate
    it["userGender"] = activeUserAuthenticationWithoutGroups.userGender
    it["userProfile"] = activeUserAuthenticationWithoutGroups.userProfile
    it["isActive"] = activeUserAuthenticationWithoutGroups.isActive
    it["userRoles"] = activeUserAuthenticationWithoutGroups.userRoles
    it["groupsIds"] = activeUserAuthenticationWithoutGroups.userGroups.map { g -> g.id }
}

internal val groupDocuments = listOf(
    activeGroupDocumentOne,
    activeGroupDocumentTwo,
    inactiveGroupDocumentThree,
)

internal val userDocuments = listOf(
    activeUserDocument,
    activeUserWithoutGroupsDocument
)
