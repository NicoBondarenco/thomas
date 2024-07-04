package com.thomas.management.domain.model.extension

import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.management.data.entity.UserEntity
import com.thomas.management.data.entity.UserGroupsEntity
import com.thomas.management.domain.model.request.UserActiveRequest
import com.thomas.management.domain.model.request.UserCreateRequest
import com.thomas.management.domain.model.request.UserUpdateRequest
import com.thomas.management.domain.model.response.UserDetailResponse
import com.thomas.management.domain.model.response.UserPageResponse
import com.thomas.management.message.event.UserUpsertedEvent
import java.time.OffsetDateTime
import java.time.ZoneOffset.UTC

fun UserEntity.toUserPageResponse() = UserPageResponse(
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
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)

fun UserGroupsEntity.toUserDetailResponse() = UserDetailResponse(
    id = this.user.id,
    firstName = this.user.firstName,
    lastName = this.user.lastName,
    mainEmail = this.user.mainEmail,
    documentNumber = this.user.documentNumber,
    phoneNumber = this.user.phoneNumber,
    profilePhoto = this.user.profilePhoto,
    birthDate = this.user.birthDate,
    userGender = this.user.userGender,
    isActive = this.user.isActive,
    createdAt = this.user.createdAt,
    updatedAt = this.user.updatedAt,
    userRoles = this.user.userRoles,
    userGroups = this.groups.map { it.id },
)

fun UserCreateRequest.toUserEntity() = UserEntity(
    firstName = this.firstName,
    lastName = this.lastName,
    mainEmail = this.mainEmail,
    documentNumber = this.documentNumber,
    phoneNumber = this.phoneNumber,
    birthDate = this.birthDate,
    userGender = this.userGender,
    isActive = this.isActive,
    creatorId = currentUser.userId,
    userRoles = this.userRoles,
)

fun UserEntity.updateFromRequest(
    request: UserUpdateRequest,
) = this.copy(
    firstName = request.firstName,
    lastName = request.lastName,
    documentNumber = request.documentNumber,
    phoneNumber = request.phoneNumber,
    birthDate = request.birthDate,
    userGender = request.userGender,
    isActive = request.isActive,
    updatedAt = OffsetDateTime.now(UTC),
    userRoles = request.userRoles,
)

fun UserGroupsEntity.updateActive(
    request: UserActiveRequest,
) = this.copy(
    user = this.user.copy(
        isActive = request.isActive,
        updatedAt = OffsetDateTime.now(UTC),
    )
)

fun UserGroupsEntity.toUserUpsertedEvent() = UserUpsertedEvent(
    id = this.user.id,
    firstName = this.user.firstName,
    lastName = this.user.lastName,
    mainEmail = this.user.mainEmail,
    documentNumber = this.user.documentNumber,
    phoneNumber = this.user.phoneNumber,
    profilePhoto = this.user.profilePhoto,
    birthDate = this.user.birthDate,
    userGender = this.user.userGender,
    isActive = this.user.isActive,
    createdAt = this.user.createdAt,
    updatedAt = this.user.updatedAt,
    userRoles = this.user.userRoles,
    userGroups = this.groups.map { it.id },
)
