package com.thomas.spring.util

import org.bson.Document

private const val ID_PROPERTY = "_id"

internal val activeGroupDocumentOne = Document().also {
    it[ID_PROPERTY] = activeGroupOne.groupId
    it["id"] = activeGroupOne.groupId
    it["groupName"] = activeGroupOne.groupName
    it["groupDescription"] = activeGroupOne.groupName
    it["isActive"] = true
    it["groupRoles"] = activeGroupOne.groupRoles
}

internal val activeGroupDocumentTwo = Document().also {
    it[ID_PROPERTY] = activeGroupTwo.groupId
    it["id"] = activeGroupTwo.groupId
    it["groupName"] = activeGroupTwo.groupName
    it["groupDescription"] = activeGroupTwo.groupName
    it["isActive"] = true
    it["groupRoles"] = activeGroupTwo.groupRoles
}

internal val inactiveGroupDocumentThree = Document().also {
    it[ID_PROPERTY] = inactiveGroupThree.groupId
    it["id"] = inactiveGroupThree.groupId
    it["groupName"] = inactiveGroupThree.groupName
    it["groupDescription"] = inactiveGroupThree.groupName
    it["isActive"] = false
    it["groupRoles"] = inactiveGroupThree.groupRoles
}

internal val activeUserDocument = Document().also {
    it[ID_PROPERTY] = activeUser.userId
    it["id"] = activeUser.userId
    it["firstName"] = activeUser.firstName
    it["lastName"] = activeUser.lastName
    it["mainEmail"] = activeUser.mainEmail
    it["phoneNumber"] = activeUser.phoneNumber
    it["profilePhoto"] = activeUser.profilePhoto
    it["birthDate"] = activeUser.birthDate
    it["userGender"] = activeUser.userGender
    it["isActive"] = activeUser.isActive
    it["userRoles"] = activeUser.userRoles
    it["groupsIds"] = activeUser.userGroups.map { g -> g.groupId }
}

internal val activeUserWithoutGroupsDocument = Document().also {
    it[ID_PROPERTY] = activeUserWithoutGroups.userId
    it["id"] = activeUserWithoutGroups.userId
    it["firstName"] = activeUserWithoutGroups.firstName
    it["lastName"] = activeUserWithoutGroups.lastName
    it["mainEmail"] = activeUserWithoutGroups.mainEmail
    it["phoneNumber"] = activeUserWithoutGroups.phoneNumber
    it["profilePhoto"] = activeUserWithoutGroups.profilePhoto
    it["birthDate"] = activeUserWithoutGroups.birthDate
    it["userGender"] = activeUserWithoutGroups.userGender
    it["isActive"] = activeUserWithoutGroups.isActive
    it["userRoles"] = activeUserWithoutGroups.userRoles
    it["groupsIds"] = activeUserWithoutGroups.userGroups.map { g -> g.groupId }
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
