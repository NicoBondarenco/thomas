package com.thomas.management.domain.util

import com.thomas.core.util.StringUtils.randomDocumentNumber
import com.thomas.core.util.StringUtils.randomEmail
import com.thomas.core.util.StringUtils.randomPhone
import com.thomas.core.util.StringUtils.randomString
import com.thomas.management.data.entity.UserEntity
import com.thomas.management.domain.model.request.SignupUserRequest

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
