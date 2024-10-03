package com.thomas.core.generator

import com.thomas.core.model.security.SecurityUser
import com.thomas.core.random.randomSecurityGroups
import com.thomas.core.random.randomSecurityRoles
import java.util.UUID.randomUUID

object SecurityUserGenerator {

    fun generate() = PersonGenerator.generate().let {
        SecurityUser(
            userId = randomUUID(),
            firstName = it.firstName,
            lastName = it.lastName,
            mainEmail = it.mainEmail,
            phoneNumber = it.phoneNumber,
            profilePhoto = null,
            birthDate = it.birthDate,
            userGender = it.userGender,
            isActive = listOf(true, false).random(),
            userRoles = randomSecurityRoles(),
            userGroups = randomSecurityGroups(),
        )
    }

}