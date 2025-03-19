package com.thomas.management.data.neo4j.util

import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.GroupSimpleEntity
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.data.entity.UserSimpleEntity

fun GroupCompleteEntity.toGroupSimpleEntity() = GroupSimpleEntity(
    id = this.id,
    groupName = this.groupName,
    groupDescription = this.groupDescription,
    groupOrganization = this.groupOrganization,
    organizationRoles = this.organizationRoles,
    isActive = this.isActive,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)

fun UserCompleteEntity.toUserSimpleEntity() = UserSimpleEntity(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    documentNumber = this.documentNumber,
    profilePhoto = this.profilePhoto,
    userGender = this.userGender,
    birthDate = this.birthDate,
    passwordSalt = this.passwordSalt,
    passwordHash = this.passwordHash,
    userOrganization = this.userOrganization,
    organizationRoles = this.organizationRoles,
    mainEmail = this.mainEmail,
    mainPhone = this.mainPhone,
    isActive = this.isActive,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)
