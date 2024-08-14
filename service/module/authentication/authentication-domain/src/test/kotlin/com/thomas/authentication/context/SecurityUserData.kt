package com.thomas.authentication.context

import com.thomas.core.generator.PersonGenerator
import com.thomas.core.model.security.SecurityUser
import com.thomas.core.random.randomSecurityRoles
import java.util.UUID.randomUUID

val securityUser: SecurityUser
    get() = PersonGenerator.generate().let {
        SecurityUser(
            userId = randomUUID(),
            firstName = it.firstName,
            lastName = it.lastName,
            mainEmail = it.mainEmail,
            phoneNumber = it.phoneNumber,
            profilePhoto = null,
            birthDate = it.birthDate,
            userGender = it.userGender,
            isActive = true,
            userRoles = randomSecurityRoles(),
            userGroups = listOf(),
        )
    }