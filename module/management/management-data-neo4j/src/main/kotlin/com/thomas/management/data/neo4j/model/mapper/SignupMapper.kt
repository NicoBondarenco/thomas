package com.thomas.management.data.neo4j.model.mapper

import com.thomas.management.data.entity.SignupEntity
import com.thomas.management.data.neo4j.model.node.UserNode
import com.thomas.management.data.neo4j.model.node.UserOrganizationNode

fun SignupEntity.toUserNode() = UserNode(
    id = this.userData.id,
    firstName = this.userData.firstName,
    lastName = this.userData.lastName,
    documentNumber = this.userData.documentNumber,
    profilePhoto = this.userData.profilePhoto,
    userGender = this.userData.userGender,
    birthDate = this.userData.birthDate,
    passwordSalt = this.userData.passwordSalt,
    passwordHash = this.userData.passwordHash,
    mainEmail = this.userData.mainEmail,
    mainPhone = this.userData.mainPhone,
    isActive = this.userData.isActive,
    createdAt = this.userData.createdAt.toZonedDateTime(),
    updatedAt = this.userData.updatedAt.toZonedDateTime(),
    userOrganization = UserOrganizationNode(
        id = this.userData.id,
        userId = this.userData.id,
        organizationId = this.organizationData.id,
        userRoles = this.userData.organizationRoles,
        organizationNode = this.organizationData.toOrganizationNode(),
    ),
    userUnits = setOf(),
    userGroups = setOf(),
).apply {
    this.userOrganization.userNode = this
}
