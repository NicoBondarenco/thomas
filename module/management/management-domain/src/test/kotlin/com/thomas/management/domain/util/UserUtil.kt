package com.thomas.management.domain.util

import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.core.util.StringUtils.randomDocumentNumber
import com.thomas.core.util.StringUtils.randomEmail
import com.thomas.core.util.StringUtils.randomPhone
import com.thomas.core.util.StringUtils.randomString
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.data.entity.UserEntity
import com.thomas.management.domain.model.request.SignupUserRequest
import com.thomas.management.domain.model.request.UserCreateRequest
import com.thomas.management.domain.model.request.UserUpdateRequest

internal val userEntity: UserEntity
    get() = UserEntity(
        firstName = randomString(numbers = false),
        lastName = randomString(numbers = false),
        documentNumber = randomDocumentNumber(),
        passwordSalt = randomString(),
        passwordHash = randomString(),
        userOrganization = organizationEntity,
        organizationRoles = setOf(),
        mainEmail = randomEmail(),
        mainPhone = randomPhone(),
    )

internal val userRolesEntity: UserEntity
    get() = userEntity.copy(
        organizationRoles = SecurityOrganizationRole.entries.shuffled().subList(0, 3).toSet(),
    )

internal val userCompleteEntity: UserCompleteEntity
    get() = UserCompleteEntity(
        userData = userEntity,
        userGroups = setOf(),
        userUnits = mapOf(),
    )

internal val userFullEntity: UserCompleteEntity
    get() = UserCompleteEntity(
        userData = userRolesEntity,
        userGroups = (1..3).map { groupFullEntity }.toSet(),
        userUnits = (1..3).associate {
            unitEntity to SecurityUnitRole.entries.shuffled().subList(0, 3).toSet()
        },
    )

internal val signupUserRequest: SignupUserRequest
    get() = SignupUserRequest(
        firstName = randomString(numbers = false),
        lastName = randomString(numbers = false),
        documentNumber = randomDocumentNumber(),
        userPassword = randomString(),
        mainEmail = randomEmail(),
        mainPhone = randomPhone(),
        userGender = null,
        birthDate = null,
    )

internal val userCreateRequest: UserCreateRequest
    get() = UserCreateRequest(
        firstName = randomString(numbers = false),
        lastName = randomString(numbers = false),
        documentNumber = randomDocumentNumber(),
        userGender = null,
        birthDate = null,
        mainEmail = randomEmail(),
        mainPhone = randomPhone(),
        isActive = true,
        organizationRoles = setOf(),
        userGroups = setOf(),
        userUnits = mapOf(),
    )

internal val userUpdateRequest: UserUpdateRequest
    get() = UserUpdateRequest(
        firstName = randomString(numbers = false),
        lastName = randomString(numbers = false),
        documentNumber = randomDocumentNumber(),
        userGender = null,
        birthDate = null,
        mainPhone = randomPhone(),
        isActive = true,
        organizationRoles = setOf(),
        userGroups = setOf(),
        userUnits = mapOf(),
    )
