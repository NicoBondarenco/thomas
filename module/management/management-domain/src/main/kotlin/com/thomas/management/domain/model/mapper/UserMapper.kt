package com.thomas.management.domain.model.mapper

import com.thomas.core.model.security.SecurityOrganizationRole.ORGANIZATION_ALL
import com.thomas.hasher.Hasher
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.UserEntity
import com.thomas.management.domain.model.request.SignupUserRequest
import com.thomas.management.domain.model.response.SignupUserResponse
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
