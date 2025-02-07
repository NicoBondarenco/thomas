package com.thomas.management.domain.util

import com.thomas.core.util.StringUtils.randomDocumentNumber
import com.thomas.core.util.StringUtils.randomEmail
import com.thomas.core.util.StringUtils.randomPhone
import com.thomas.core.util.StringUtils.randomString
import com.thomas.management.domain.model.request.SignupUserRequest
import com.thomas.management.domain.model.request.UserCreateRequest
import com.thomas.management.domain.model.request.UserUpdateRequest

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
