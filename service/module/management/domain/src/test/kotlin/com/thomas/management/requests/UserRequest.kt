package com.thomas.management.requests

import com.thomas.core.generator.PersonGenerator
import com.thomas.management.data.entity.UserCompleteEntity
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
            userRoles = setOf(),
            userGroups = setOf(),
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
            userRoles = setOf(),
            userGroups = setOf(),
        )
    }

val activeUserRequest: UserActiveRequest
    get() = UserActiveRequest(
        isActive = listOf(true, false).random(),
    )

fun UserCompleteEntity.toUserUpdateRequest() = UserUpdateRequest(
    firstName = this.firstName,
    lastName = this.lastName,
    documentNumber = this.documentNumber,
    phoneNumber = this.phoneNumber,
    birthDate = this.birthDate,
    userGender = this.userGender,
    isActive = this.isActive,
    userRoles = this.userRoles,
    userGroups = setOf(),
)
