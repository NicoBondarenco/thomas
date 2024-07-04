package com.thomas.management.requests

import com.thomas.core.generator.PersonGenerator
import com.thomas.management.domain.model.request.UserActiveRequest
import com.thomas.management.domain.model.request.UserCreateRequest
import com.thomas.management.domain.model.request.UserUpdateRequest

val createUserRequest: UserCreateRequest
    get() = PersonGenerator.generate().let {
        UserCreateRequest(
            firstName = it.firstName,
            lastName = it.lastName,
            mainEmail = it.mainEmail,
            documentNumber = it.documentNumber,
            phoneNumber = it.phoneNumber,
            birthDate = it.birthDate,
            userGender = it.userGender,
            isActive = listOf(true, false).random(),
            userRoles = listOf(),
            userGroups = listOf(),
        )
    }

val updateUserRequest: UserUpdateRequest
    get() = PersonGenerator.generate().let {
        UserUpdateRequest(
            firstName = it.firstName,
            lastName = it.lastName,
            documentNumber = it.documentNumber,
            phoneNumber = it.phoneNumber,
            birthDate = it.birthDate,
            userGender = it.userGender,
            isActive = listOf(true, false).random(),
            userRoles = listOf(),
            userGroups = listOf(),
        )
    }

val activeUserRequest: UserActiveRequest
    get() = UserActiveRequest(
        isActive = listOf(true, false).random(),
    )
