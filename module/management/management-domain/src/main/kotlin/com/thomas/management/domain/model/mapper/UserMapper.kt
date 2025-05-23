package com.thomas.management.domain.model.mapper

import com.thomas.contract.messaging.ApplicationEventType
import com.thomas.contract.messaging.management.user.UserDataEvent
import com.thomas.contract.messaging.management.user.UserManagementEvent
import com.thomas.core.model.general.UserType
import com.thomas.core.model.general.UserType.COMMON
import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.UnitRoleEntity
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.data.entity.UserSimpleEntity
import com.thomas.management.domain.crypt.Hasher
import com.thomas.management.domain.model.request.SignupUserRequest
import com.thomas.management.domain.model.request.UserCreateRequest
import com.thomas.management.domain.model.request.UserUpdateRequest
import com.thomas.management.domain.model.response.SignupUserResponse
import com.thomas.management.domain.model.response.UserDetailResponse
import com.thomas.management.domain.model.response.UserSimpleResponse
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import kotlinx.coroutines.coroutineScope

suspend fun SignupUserRequest.toUserEntity(
    userOrganization: OrganizationEntity,
    hasher: Hasher,
    defaultType: UserType,
    defaultRoles: Set<SecurityOrganizationRole>,
): UserSimpleEntity = coroutineScope {
    hasher.let {
        val salt = it.generateSalt()
        val password = it.hash(this@toUserEntity.userPassword, salt)
        UserSimpleEntity(
            firstName = this@toUserEntity.firstName,
            lastName = this@toUserEntity.lastName,
            documentNumber = this@toUserEntity.documentNumber,
            userGender = this@toUserEntity.userGender,
            userRace = this@toUserEntity.userRace,
            userType = defaultType,
            birthDate = this@toUserEntity.birthDate,
            passwordSalt = salt,
            passwordHash = password,
            userOrganization = userOrganization,
            organizationRoles = defaultRoles,
            mainEmail = this@toUserEntity.mainEmail,
            mainPhone = this@toUserEntity.mainPhone,
        )
    }
}

suspend fun UserSimpleEntity.toSignupUserResponse() = coroutineScope {
    SignupUserResponse(
        id = this@toSignupUserResponse.id,
        firstName = this@toSignupUserResponse.firstName,
        lastName = this@toSignupUserResponse.lastName,
        documentNumber = this@toSignupUserResponse.documentNumber,
        profilePhoto = this@toSignupUserResponse.profilePhoto,
        userGender = this@toSignupUserResponse.userGender,
        userRace = this@toSignupUserResponse.userRace,
        birthDate = this@toSignupUserResponse.birthDate,
        mainEmail = this@toSignupUserResponse.mainEmail,
        mainPhone = this@toSignupUserResponse.mainPhone,
        isActive = this@toSignupUserResponse.isActive,
        createdAt = this@toSignupUserResponse.createdAt,
        updatedAt = this@toSignupUserResponse.updatedAt,
    )

}

suspend fun UserCreateRequest.toUserCompleteEntity(
    userOrganization: OrganizationEntity,
    passwordSalt: String,
    passwordHash: String,
    userGroups: Set<GroupCompleteEntity>,
    userUnits: Set<UnitRoleEntity>,
) = coroutineScope {
    UserCompleteEntity(
        firstName = this@toUserCompleteEntity.firstName,
        lastName = this@toUserCompleteEntity.lastName,
        documentNumber = this@toUserCompleteEntity.documentNumber,
        userGender = this@toUserCompleteEntity.userGender,
        userRace = this@toUserCompleteEntity.userRace,
        userType = COMMON,
        birthDate = this@toUserCompleteEntity.birthDate,
        passwordSalt = passwordSalt,
        passwordHash = passwordHash,
        userOrganization = userOrganization,
        organizationRoles = this@toUserCompleteEntity.organizationRoles,
        mainEmail = this@toUserCompleteEntity.mainEmail,
        mainPhone = this@toUserCompleteEntity.mainPhone,
        isActive = this@toUserCompleteEntity.isActive,
        userGroups = userGroups,
        userUnits = userUnits,
    )
}

suspend fun UserCompleteEntity.updateFromRequest(
    request: UserUpdateRequest,
    userGroups: Set<GroupCompleteEntity>,
    userUnits: Set<UnitRoleEntity>,
) = coroutineScope {
    this@updateFromRequest.copy(
        firstName = request.firstName,
        lastName = request.lastName,
        documentNumber = request.documentNumber,
        userGender = request.userGender,
        userRace = request.userRace,
        birthDate = request.birthDate,
        mainPhone = request.mainPhone,
        isActive = request.isActive,
        organizationRoles = request.organizationRoles,
        userGroups = userGroups,
        userUnits = userUnits,
        updatedAt = now(UTC),
    )
}

suspend fun UserSimpleEntity.toUserSimpleResponse() = coroutineScope {
    UserSimpleResponse(
        id = this@toUserSimpleResponse.id,
        firstName = this@toUserSimpleResponse.firstName,
        lastName = this@toUserSimpleResponse.lastName,
        documentNumber = this@toUserSimpleResponse.documentNumber,
        profilePhoto = this@toUserSimpleResponse.profilePhoto,
        userGender = this@toUserSimpleResponse.userGender,
        userRace = this@toUserSimpleResponse.userRace,
        birthDate = this@toUserSimpleResponse.birthDate,
        userOrganization = this@toUserSimpleResponse.userOrganization.toOrganizationResponse(),
        mainEmail = this@toUserSimpleResponse.mainEmail,
        mainPhone = this@toUserSimpleResponse.mainPhone,
        isActive = this@toUserSimpleResponse.isActive,
        createdAt = this@toUserSimpleResponse.createdAt,
        updatedAt = this@toUserSimpleResponse.updatedAt,
    )
}

suspend fun UserCompleteEntity.toUserDetailResponse() = coroutineScope {
    UserDetailResponse(
        id = this@toUserDetailResponse.id,
        firstName = this@toUserDetailResponse.firstName,
        lastName = this@toUserDetailResponse.lastName,
        documentNumber = this@toUserDetailResponse.documentNumber,
        profilePhoto = this@toUserDetailResponse.profilePhoto,
        userGender = this@toUserDetailResponse.userGender,
        userRace = this@toUserDetailResponse.userRace,
        birthDate = this@toUserDetailResponse.birthDate,
        userOrganization = this@toUserDetailResponse.userOrganization.toOrganizationResponse(),
        organizationRoles = this@toUserDetailResponse.organizationRoles,
        mainEmail = this@toUserDetailResponse.mainEmail,
        mainPhone = this@toUserDetailResponse.mainPhone,
        isActive = this@toUserDetailResponse.isActive,
        createdAt = this@toUserDetailResponse.createdAt,
        updatedAt = this@toUserDetailResponse.updatedAt,
        userGroups = this@toUserDetailResponse.userGroups.map { it.id }.toSet(),
        userUnits = this@toUserDetailResponse.userUnits.associate { it.roleUnit.id to it.roleList },
    )
}

suspend fun UserSimpleEntity.toUserManagementEvent(type: ApplicationEventType) = coroutineScope {
    UserManagementEvent(
        eventType = type,
        eventTimestamp = now(UTC),
        eventKey = this@toUserManagementEvent.id,
        eventData = this@toUserManagementEvent.toUserDataEvent(),
    )
}

suspend fun UserSimpleEntity.toUserDataEvent() = coroutineScope {
    UserDataEvent(
        id = this@toUserDataEvent.id,
        firstName = this@toUserDataEvent.firstName,
        lastName = this@toUserDataEvent.lastName,
        documentNumber = this@toUserDataEvent.documentNumber,
        profilePhoto = this@toUserDataEvent.profilePhoto,
        userGender = this@toUserDataEvent.userGender,
        userRace = this@toUserDataEvent.userRace,
        userType = this@toUserDataEvent.userType,
        birthDate = this@toUserDataEvent.birthDate,
        userOrganization = this@toUserDataEvent.userOrganization.id,
        organizationRoles = this@toUserDataEvent.organizationRoles,
        mainEmail = this@toUserDataEvent.mainEmail,
        mainPhone = this@toUserDataEvent.mainPhone,
        isActive = this@toUserDataEvent.isActive,
        userGroups = setOf(),
        userUnits = mapOf(),
        createdAt = this@toUserDataEvent.createdAt,
        updatedAt = this@toUserDataEvent.updatedAt,
    )
}

suspend fun UserCompleteEntity.toUserManagementEvent(type: ApplicationEventType) = coroutineScope {
    UserManagementEvent(
        eventType = type,
        eventTimestamp = now(UTC),
        eventKey = this@toUserManagementEvent.id,
        eventData = this@toUserManagementEvent.toUserDataEvent(),
    )
}

suspend fun UserCompleteEntity.toUserDataEvent() = coroutineScope {
    UserDataEvent(
        id = this@toUserDataEvent.id,
        firstName = this@toUserDataEvent.firstName,
        lastName = this@toUserDataEvent.lastName,
        documentNumber = this@toUserDataEvent.documentNumber,
        profilePhoto = this@toUserDataEvent.profilePhoto,
        userGender = this@toUserDataEvent.userGender,
        userRace = this@toUserDataEvent.userRace,
        userType = this@toUserDataEvent.userType,
        birthDate = this@toUserDataEvent.birthDate,
        userOrganization = this@toUserDataEvent.userOrganization.id,
        organizationRoles = this@toUserDataEvent.organizationRoles,
        mainEmail = this@toUserDataEvent.mainEmail,
        mainPhone = this@toUserDataEvent.mainPhone,
        isActive = this@toUserDataEvent.isActive,
        userGroups = this@toUserDataEvent.userGroups.toUserGroupsEvent(),
        userUnits = this@toUserDataEvent.userUnits.toUserUnitsEvent(),
        createdAt = this@toUserDataEvent.createdAt,
        updatedAt = this@toUserDataEvent.updatedAt,
    )
}

private suspend fun Set<GroupCompleteEntity>.toUserGroupsEvent() = coroutineScope {
    this@toUserGroupsEvent.map { it.id }.toSet()
}

private suspend fun Set<UnitRoleEntity>.toUserUnitsEvent() = coroutineScope {
    this@toUserUnitsEvent.associate { it.roleUnit.id to it.roleList }
}
