package com.thomas.management.data.neo4j.model.mapper

import com.thomas.core.model.security.SecurityOrganizationRole.MASTER_ROLE
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.UnitRoleEntity
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.data.entity.UserSimpleEntity
import com.thomas.management.data.neo4j.model.node.UserGroupNode
import com.thomas.management.data.neo4j.model.node.UserNode
import com.thomas.management.data.neo4j.model.node.UserOrganizationNode
import com.thomas.management.data.neo4j.model.node.UserUnitNode
import java.util.UUID.randomUUID

fun UserNode.toUserSimpleEntity(): UserSimpleEntity = UserSimpleEntity(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    documentNumber = this.documentNumber,
    profilePhoto = this.profilePhoto,
    userGender = this.userGender,
    userRace = this.userRace,
    userType = this.userType,
    birthDate = this.birthDate,
    passwordSalt = this.passwordSalt,
    passwordHash = this.passwordHash,
    userOrganization = this.userOrganization.organizationNode.toOrganizationEntity(),
    organizationRoles = this.userOrganization.userRoles,
    mainEmail = this.mainEmail,
    mainPhone = this.mainPhone,
    isActive = this.isActive,
    createdAt = this.createdAt.toOffsetDateTime(),
    updatedAt = this.updatedAt.toOffsetDateTime(),
)

fun UserNode.toUserCompleteEntity(): UserCompleteEntity = UserCompleteEntity(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    documentNumber = this.documentNumber,
    profilePhoto = this.profilePhoto,
    userGender = this.userGender,
    userRace = this.userRace,
    userType = this.userType,
    birthDate = this.birthDate,
    passwordSalt = this.passwordSalt,
    passwordHash = this.passwordHash,
    userOrganization = this.userOrganization.organizationNode.toOrganizationEntity(),
    organizationRoles = this.userOrganization.userRoles,
    mainEmail = this.mainEmail,
    mainPhone = this.mainPhone,
    isActive = this.isActive,
    createdAt = this.createdAt.toOffsetDateTime(),
    updatedAt = this.updatedAt.toOffsetDateTime(),
    userGroups = this.userGroups?.map { it.groupNode.toGroupCompleteEntity() }?.toSet() ?: emptySet(),
    userUnits = this.userUnits?.map { it.toUnitRoleEntity() }?.toSet() ?: emptySet(),
)

fun UserUnitNode.toUnitRoleEntity(): UnitRoleEntity = UnitRoleEntity(
    id = this.id,
    roleUnit = this.unitNode.toUnitEntity(),
    roleList = this.userRoles,
)

fun UserCompleteEntity.toUserNode(): UserNode = UserNode(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    documentNumber = this.documentNumber,
    profilePhoto = this.profilePhoto,
    userGender = this.userGender,
    userRace = this.userRace,
    userType = this.userType,
    birthDate = this.birthDate,
    passwordSalt = this.passwordSalt,
    passwordHash = this.passwordHash,
    mainEmail = this.mainEmail,
    mainPhone = this.mainPhone,
    isActive = this.isActive,
    isMaster = this.isMaster(),
    createdAt = this.createdAt.toZonedDateTime(),
    updatedAt = this.updatedAt.toZonedDateTime(),
    userOrganization = UserOrganizationNode(
        id = this.id,
        userId = this.id,
        organizationId = this.userOrganization.id,
        userRoles = this.organizationRoles,
        organizationNode = this.userOrganization.toOrganizationNode(),
    ),
    userUnits = this.userUnits.map {
        UserUnitNode(
            id = it.id,
            userId = this.id,
            unitId = it.roleUnit.id,
            userRoles = it.roleList,
            unitNode = it.roleUnit.toUnitNode()
        )
    }.toSet(),
    userGroups = this.userGroups.map {
        UserGroupNode(
            id = randomUUID(),
            userId = this.id,
            groupId = it.id,
            groupNode = it.toGroupNode()
        )
    }.toSet(),
).apply {
    userOrganization.userNode = this
    userUnits?.forEach { it.userNode = this }
    userGroups?.forEach { it.userNode = this }
}

fun UserNode.updateFrom(entity: UserSimpleEntity) {
    this.id = entity.id
    this.firstName = entity.firstName
    this.lastName = entity.lastName
    this.documentNumber = entity.documentNumber
    this.profilePhoto = entity.profilePhoto
    this.userGender = entity.userGender
    this.userRace = entity.userRace
    this.userType = entity.userType
    this.birthDate = entity.birthDate
    this.passwordSalt = entity.passwordSalt
    this.passwordHash = entity.passwordHash
    this.userOrganization.userNode = this
    this.mainEmail = entity.mainEmail
    this.mainPhone = entity.mainPhone
    this.isActive = entity.isActive
    this.createdAt = entity.createdAt.toZonedDateTime()
    this.updatedAt = entity.updatedAt.toZonedDateTime()
}

private fun UserCompleteEntity.isMaster() = this.organizationRoles.contains(MASTER_ROLE) || this.userGroups.hasMaster()

private fun Set<GroupCompleteEntity>.hasMaster() = this.any { group -> group.isMaster() }

private fun GroupCompleteEntity.isMaster() = this.organizationRoles.contains(MASTER_ROLE)