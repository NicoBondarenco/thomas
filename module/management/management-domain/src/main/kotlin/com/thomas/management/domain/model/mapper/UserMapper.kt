package com.thomas.management.domain.model.mapper

import com.thomas.contract.messaging.management.user.UserCreatedEvent
import com.thomas.contract.messaging.management.user.UserUpdatedEvent
import com.thomas.core.model.security.SecurityOrganizationRole.ORGANIZATION_ALL
import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.hasher.Hasher
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.GroupEntity
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.UnitEntity
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.data.entity.UserEntity
import com.thomas.management.domain.model.request.SignupUserRequest
import com.thomas.management.domain.model.request.UserCreateRequest
import com.thomas.management.domain.model.request.UserUpdateRequest
import com.thomas.management.domain.model.response.SignupUserResponse
import com.thomas.management.domain.model.response.UserDetailResponse
import com.thomas.management.domain.model.response.UserSimpleResponse
import kotlinx.coroutines.coroutineScope

suspend fun SignupUserRequest.toUserEntity(
    userOrganization: OrganizationEntity,
    hasher: Hasher,
): UserEntity = coroutineScope {
    hasher.let {
        val salt = it.generateSalt()
        val password = it.hash(this@toUserEntity.userPassword, salt)
        UserEntity(
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

suspend fun UserEntity.toSignupUserResponse() = coroutineScope {
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

suspend fun UserCreateRequest.toUserEntity(
    userOrganization: OrganizationEntity,
    passwordSalt: String,
    passwordHash: String,
) = coroutineScope {
    UserEntity(
        firstName = this@toUserEntity.firstName,
        lastName = this@toUserEntity.lastName,
        documentNumber = this@toUserEntity.documentNumber,
        userGender = this@toUserEntity.userGender,
        birthDate = this@toUserEntity.birthDate,
        passwordSalt = passwordSalt,
        passwordHash = passwordHash,
        userOrganization = userOrganization,
        organizationRoles = this@toUserEntity.organizationRoles,
        mainEmail = this@toUserEntity.mainEmail,
        mainPhone = this@toUserEntity.mainPhone,
        isActive = this@toUserEntity.isActive,
    )
}

suspend fun UserEntity.updateFromRequest(
    request: UserUpdateRequest,
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
    )
}

suspend fun UserEntity.toUserSimpleResponse() = coroutineScope {
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
    val user = this@toUserDetailResponse.userData
    val groups = this@toUserDetailResponse.userGroups
    val units = this@toUserDetailResponse.userUnits

    UserDetailResponse(
        id = user.id,
        firstName = user.firstName,
        lastName = user.lastName,
        documentNumber = user.documentNumber,
        profilePhoto = user.profilePhoto,
        userGender = user.userGender,
        birthDate = user.birthDate,
        userOrganization = user.userOrganization.toOrganizationResponse(),
        organizationRoles = user.organizationRoles,
        mainEmail = user.mainEmail,
        mainPhone = user.mainPhone,
        isActive = user.isActive,
        createdAt = user.createdAt,
        updatedAt = user.updatedAt,
        userGroups = groups.map { it.id }.toSet(),
        userUnits = units.mapKeys { it.key.id },
    )
}

suspend fun UserEntity.toUserCreatedEvent() = coroutineScope {
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
        firstName = this@toUserCreatedEvent.userData.firstName,
        lastName = this@toUserCreatedEvent.userData.lastName,
        documentNumber = this@toUserCreatedEvent.userData.documentNumber,
        profilePhoto = this@toUserCreatedEvent.userData.profilePhoto,
        userGender = this@toUserCreatedEvent.userData.userGender,
        birthDate = this@toUserCreatedEvent.userData.birthDate,
        userOrganization = this@toUserCreatedEvent.userData.userOrganization.id,
        organizationRoles = this@toUserCreatedEvent.userData.organizationRoles,
        mainEmail = this@toUserCreatedEvent.userData.mainEmail,
        mainPhone = this@toUserCreatedEvent.userData.mainPhone,
        isActive = this@toUserCreatedEvent.userData.isActive,
        userGroups = this@toUserCreatedEvent.userGroups.toUserGroupsEvent(),
        userUnits = this@toUserCreatedEvent.userUnits.toUserUnitsEvent(),
        createdAt = this@toUserCreatedEvent.userData.createdAt,
        updatedAt = this@toUserCreatedEvent.userData.updatedAt,
    )
}

suspend fun UserCompleteEntity.toUserUpdatedEvent() = coroutineScope {
    UserUpdatedEvent(
        id = this@toUserUpdatedEvent.id,
        firstName = this@toUserUpdatedEvent.userData.firstName,
        lastName = this@toUserUpdatedEvent.userData.lastName,
        documentNumber = this@toUserUpdatedEvent.userData.documentNumber,
        profilePhoto = this@toUserUpdatedEvent.userData.profilePhoto,
        userGender = this@toUserUpdatedEvent.userData.userGender,
        birthDate = this@toUserUpdatedEvent.userData.birthDate,
        userOrganization = this@toUserUpdatedEvent.userData.userOrganization.id,
        organizationRoles = this@toUserUpdatedEvent.userData.organizationRoles,
        mainEmail = this@toUserUpdatedEvent.userData.mainEmail,
        mainPhone = this@toUserUpdatedEvent.userData.mainPhone,
        isActive = this@toUserUpdatedEvent.userData.isActive,
        userGroups = this@toUserUpdatedEvent.userGroups.toUserGroupsEvent(),
        userUnits = this@toUserUpdatedEvent.userUnits.toUserUnitsEvent(),
        createdAt = this@toUserUpdatedEvent.userData.createdAt,
        updatedAt = this@toUserUpdatedEvent.userData.updatedAt,
    )
}

private suspend fun Set<GroupCompleteEntity>.toUserGroupsEvent() = coroutineScope {
    this@toUserGroupsEvent.map { it.id }.toSet()
}

private suspend fun Map<UnitEntity, Set<SecurityUnitRole>>.toUserUnitsEvent() = coroutineScope {
    this@toUserUnitsEvent.mapKeys { it.key.id }
}
