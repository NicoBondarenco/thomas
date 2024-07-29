package com.thomas.authentication.domain.model.extension

import com.thomas.authentication.data.entity.UserAuthenticationCompleteEntity
import com.thomas.authentication.data.entity.UserAuthenticationEntity
import com.thomas.core.model.security.SecurityUser
import com.thomas.management.message.event.UserUpsertedEvent
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC

private const val DEFAULT_PASSWORD_PREFIX = "Pass@"

fun UserAuthenticationEntity.updatedFromEvent(
    event: UserUpsertedEvent
) = this.copy(
    firstName = event.firstName,
    lastName = event.lastName,
    mainEmail = event.mainEmail,
    documentNumber = event.documentNumber,
    phoneNumber = event.phoneNumber,
    profilePhoto = event.profilePhoto,
    birthDate = event.birthDate,
    userGender = event.userGender,
    isActive = event.isActive,
    userRoles = event.userRoles,
    createdAt = event.createdAt,
    updatedAt = event.updatedAt,
    groupsIds = event.userGroups,
)

fun UserUpsertedEvent.toUserAuthenticationEntity(
    passwordHash: String,
    passwordSalt: String,
) = UserAuthenticationEntity(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    mainEmail = this.mainEmail,
    documentNumber = this.documentNumber,
    phoneNumber = this.phoneNumber,
    profilePhoto = this.profilePhoto,
    birthDate = this.birthDate,
    userGender = this.userGender,
    isActive = this.isActive,
    passwordHash = passwordHash,
    passwordSalt = passwordSalt,
    userRoles = this.userRoles,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    groupsIds = this.userGroups,
)

fun UserUpsertedEvent.defaultPassword() = "$DEFAULT_PASSWORD_PREFIX${this.documentNumber.substring(0, 6)}"

fun UserAuthenticationCompleteEntity.toSecurityUser() = SecurityUser(
    userId = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    mainEmail = this.mainEmail,
    phoneNumber = this.phoneNumber,
    profilePhoto = this.profilePhoto,
    birthDate = this.birthDate,
    userGender = this.userGender,
    isActive = this.isActive,
    userRoles = this.userRoles.toList(),
    userGroups = this.userGroups.map { it.toSecurityGroup() },
)

fun UserAuthenticationEntity.changePassword(
    passwordHash: String
) = this.copy(
    passwordHash = passwordHash,
    updatedAt = now(UTC),
)