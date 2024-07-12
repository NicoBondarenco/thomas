package com.thomas.management.data.exposed.extension

import com.thomas.management.data.entity.UserBaseEntity
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.data.entity.UserEntity
import com.thomas.management.data.exposed.mapping.UserExposedEntity
import com.thomas.management.data.exposed.mapping.UserTable
import org.jetbrains.exposed.dao.id.EntityID

fun UserExposedEntity.toUserEntity() = UserEntity(
    id = this.id.value,
    firstName = this.firstName,
    lastName = this.lastName,
    mainEmail = this.mainEmail,
    documentNumber = this.documentNumber,
    phoneNumber = this.phoneNumber,
    profilePhoto = this.profilePhoto,
    birthDate = this.birthDate,
    userGender = this.userGender,
    isActive = this.isActive,
    creatorId = this.creatorId.value,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)

fun UserExposedEntity.updateFromUserEntity(
    entity: UserBaseEntity
) = this.apply {
    this.firstName = entity.firstName
    this.lastName = entity.lastName
    this.mainEmail = entity.mainEmail
    this.documentNumber = entity.documentNumber
    this.phoneNumber = entity.phoneNumber
    this.profilePhoto = entity.profilePhoto
    this.birthDate = entity.birthDate
    this.userGender = entity.userGender
    this.isActive = entity.isActive
    this.creatorId = EntityID(entity.creatorId, UserTable)
    this.createdAt = entity.createdAt
    this.updatedAt = entity.updatedAt
}

fun UserExposedEntity.toUserCompleteEntity() = UserCompleteEntity(
    id = this.id.value,
    firstName = this.firstName,
    lastName = this.lastName,
    mainEmail = this.mainEmail,
    documentNumber = this.documentNumber,
    phoneNumber = this.phoneNumber,
    profilePhoto = this.profilePhoto,
    birthDate = this.birthDate,
    userGender = this.userGender,
    isActive = this.isActive,
    creatorId = this.creatorId.value,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    userRoles = this.roleList.map { it.roleAuthority }.toSet(),
    userGroups = this.userGroups.map { it.toGroupEntity() }.toSet()
)
