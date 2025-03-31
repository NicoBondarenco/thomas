package com.thomas.management.domain.model.mapper

import com.thomas.contract.messaging.management.user.UserCreatedEvent
import com.thomas.contract.messaging.management.user.UserUpdatedEvent
import com.thomas.core.model.security.SecurityOrganizationRole.ORGANIZATION_ALL
import com.thomas.hasher.Hasher
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.UnitRoleEntity
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.data.entity.UserSimpleEntity
import com.thomas.management.domain.model.request.SignupUserRequest
import com.thomas.management.domain.model.request.UserCreateRequest
import com.thomas.management.domain.model.request.UserUpdateRequest
import com.thomas.management.domain.model.response.SignupUserResponse
import com.thomas.management.domain.model.response.UserDetailResponse
import com.thomas.management.domain.model.response.UserSimpleResponse
import java.time.OffsetDateTime
import java.time.ZoneOffset.UTC
import kotlinx.coroutines.coroutineScope

suspend fun SignupUserRequest.toUserEntity(
    userOrganization: OrganizationEntity,
    hasher: Hasher,
): UserSimpleEntity = coroutineScope {
    hasher.let {
        val salt = it.generateSalt()
        val password = it.hash(this@toUserEntity.userPassword, salt)
        UserSimpleEntity(
            firstName = this@toUserEntity.firstName,
            lastName = this@toUserEntity.lastName,
            documentNumber = this@toUserEntity.documentNumber,
            userGender = this@toUserEntity.userGender,
            birthDate = this@toUserEntity.birthDate,
            passwordSalt = salt,
            passwordHash = password,
            userOrganization = userOrganization,
            organizationRoles = setOf(ORGANIZATION_ALL),
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
        birthDate = this@toSignupUserResponse.birthDate,
        passwordSalt = this@toSignupUserResponse.passwordSalt,
        passwordHash = this@toSignupUserResponse.passwordHash,
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
        birthDate = request.birthDate,
        mainPhone = request.mainPhone,
        isActive = request.isActive,
        organizationRoles = request.organizationRoles,
        userGroups = userGroups,
        userUnits = userUnits,
        updatedAt = OffsetDateTime.now(UTC),
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

suspend fun UserSimpleEntity.toUserCreatedEvent() = coroutineScope {
    UserCreatedEvent(
        id = this@toUserCreatedEvent.id,
        firstName = this@toUserCreatedEvent.firstName,
        lastName = this@toUserCreatedEvent.lastName,
        documentNumber = this@toUserCreatedEvent.documentNumber,
        profilePhoto = this@toUserCreatedEvent.profilePhoto,
        userGender = this@toUserCreatedEvent.userGender,
        birthDate = this@toUserCreatedEvent.birthDate,
        userOrganization = this@toUserCreatedEvent.userOrganization.id,
        organizationRoles = this@toUserCreatedEvent.organizationRoles,
        mainEmail = this@toUserCreatedEvent.mainEmail,
        mainPhone = this@toUserCreatedEvent.mainPhone,
        isActive = this@toUserCreatedEvent.isActive,
        userGroups = setOf(),
        userUnits = mapOf(),
        createdAt = this@toUserCreatedEvent.createdAt,
        updatedAt = this@toUserCreatedEvent.updatedAt,
    )
}

suspend fun UserCompleteEntity.toUserCreatedEvent() = coroutineScope {
    UserCreatedEvent(
        id = this@toUserCreatedEvent.id,
        firstName = this@toUserCreatedEvent.firstName,
        lastName = this@toUserCreatedEvent.lastName,
        documentNumber = this@toUserCreatedEvent.documentNumber,
        profilePhoto = this@toUserCreatedEvent.profilePhoto,
        userGender = this@toUserCreatedEvent.userGender,
        birthDate = this@toUserCreatedEvent.birthDate,
        userOrganization = this@toUserCreatedEvent.userOrganization.id,
        organizationRoles = this@toUserCreatedEvent.organizationRoles,
        mainEmail = this@toUserCreatedEvent.mainEmail,
        mainPhone = this@toUserCreatedEvent.mainPhone,
        isActive = this@toUserCreatedEvent.isActive,
        userGroups = this@toUserCreatedEvent.userGroups.toUserGroupsEvent(),
        userUnits = this@toUserCreatedEvent.userUnits.toUserUnitsEvent(),
        createdAt = this@toUserCreatedEvent.createdAt,
        updatedAt = this@toUserCreatedEvent.updatedAt,
    )
}

suspend fun UserCompleteEntity.toUserUpdatedEvent() = coroutineScope {
    UserUpdatedEvent(
        id = this@toUserUpdatedEvent.id,
        firstName = this@toUserUpdatedEvent.firstName,
        lastName = this@toUserUpdatedEvent.lastName,
        documentNumber = this@toUserUpdatedEvent.documentNumber,
        profilePhoto = this@toUserUpdatedEvent.profilePhoto,
        userGender = this@toUserUpdatedEvent.userGender,
        birthDate = this@toUserUpdatedEvent.birthDate,
        userOrganization = this@toUserUpdatedEvent.userOrganization.id,
        organizationRoles = this@toUserUpdatedEvent.organizationRoles,
        mainEmail = this@toUserUpdatedEvent.mainEmail,
        mainPhone = this@toUserUpdatedEvent.mainPhone,
        isActive = this@toUserUpdatedEvent.isActive,
        userGroups = this@toUserUpdatedEvent.userGroups.toUserGroupsEvent(),
        userUnits = this@toUserUpdatedEvent.userUnits.toUserUnitsEvent(),
        createdAt = this@toUserUpdatedEvent.createdAt,
        updatedAt = this@toUserUpdatedEvent.updatedAt,
    )
}

private suspend fun Set<GroupCompleteEntity>.toUserGroupsEvent() = coroutineScope {
    this@toUserGroupsEvent.map { it.id }.toSet()
}

private suspend fun Set<UnitRoleEntity>.toUserUnitsEvent() = coroutineScope {
    this@toUserUnitsEvent.associate { it.roleUnit.id to it.roleList }
}
