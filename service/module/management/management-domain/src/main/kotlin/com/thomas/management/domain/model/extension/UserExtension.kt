package com.thomas.management.domain.model.extension

import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.management.data.entity.GroupEntity
import com.thomas.management.data.entity.UserBaseEntity
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.domain.model.request.UserActiveRequest
import com.thomas.management.domain.model.request.UserCreateRequest
import com.thomas.management.domain.model.request.UserUpdateRequest
import com.thomas.management.domain.model.response.UserDetailResponse
import com.thomas.management.domain.model.response.UserPageResponse
import com.thomas.management.message.event.UserUpsertedEvent
import java.time.OffsetDateTime
import java.time.ZoneOffset.UTC

fun UserBaseEntity.toUserPageResponse() = UserPageResponse(
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

fun UserCompleteEntity.toUserDetailResponse() = UserDetailResponse(
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
    userRoles = this.userRoles,
    userGroups = this.userGroups.map { it.id }.toSet(),
)

fun UserCreateRequest.toUserEntity(userGroups: Set<GroupEntity>) = UserCompleteEntity(
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
    userGroups = userGroups,
)

fun UserCompleteEntity.updateFromRequest(
    request: UserUpdateRequest,
    groups: Set<GroupEntity>
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
    userGroups = groups
)

fun UserCompleteEntity.updateActive(
    request: UserActiveRequest,
) = this.copy(
    isActive = request.isActive,
    updatedAt = OffsetDateTime.now(UTC),
)

fun UserCompleteEntity.toUserUpsertedEvent() = UserUpsertedEvent(
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
    userRoles = this.userRoles,
    userGroups = this.userGroups.map { it.id }.toSet(),
)
