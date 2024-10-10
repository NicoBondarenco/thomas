package com.thomas.core.generator

import com.thomas.core.generator.GroupGenerator.generateSecurityGroupSet
import com.thomas.core.generator.OrganizationHubGenerator.generateSecurityHubSet
import com.thomas.core.generator.OrganizationHubGenerator.generateSecurityOrganization
import com.thomas.core.generator.PersonGenerator.generatePerson
import com.thomas.core.model.security.SecurityUser

object UserGenerator {

    fun generateSecurityUser(): SecurityUser = generatePerson().let {
        val organization = generateSecurityOrganization()
        SecurityUser(
            userId = it.id,
            firstName = it.firstName,
            lastName = it.lastName,
            mainEmail = it.mainEmail,
            phoneNumber = it.phoneNumber,
            profilePhoto = null,
            birthDate = it.birthDate,
            userGender = it.userGender,
            isActive = listOf(true, false).random(),
            userOrganization = organization,
            userGroups = generateSecurityGroupSet().map { group ->
                group.copy(
                    groupOrganization = organization
                )
            }.toSet(),
            userHubs = generateSecurityHubSet(),
        )
    }

}