package com.thomas.core.generator

import com.thomas.core.generator.GroupGenerator.generateSecurityGroupSet
import com.thomas.core.generator.OrganizationUnitGenerator.generateSecurityUnitSet
import com.thomas.core.generator.OrganizationUnitGenerator.generateSecurityOrganization
import com.thomas.core.generator.PersonGenerator.generatePerson
import com.thomas.core.generator.RoleGenerator.generateOrganizationRoles
import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.core.model.security.SecurityOrganizationRole
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
                    groupOrganization = organization.copy(
                        organizationRoles = generateOrganizationRoles()
                    )
                )
            }.toSet(),
            userUnits = generateSecurityUnitSet(),
        )
    }

    fun generateSecurityUserWithRoles(
        userOrganizationRoles: Set<SecurityOrganizationRole> = setOf(),
        userUnitRoles: Set<SecurityUnitRole> = setOf(),
        groupOrganizationRoles: Set<SecurityOrganizationRole> = setOf(),
        groupUnitRoles: Set<SecurityUnitRole> = setOf(),
    ): SecurityUser = generateSecurityUser().let { user ->
        user.copy(
            userOrganization = user.userOrganization.copy(
                organizationRoles = userOrganizationRoles
            ),
            userUnits = user.userUnits.map { unit ->
                unit.copy(
                    unitRoles = userUnitRoles
                )
            }.toSet(),
            userGroups = user.userGroups.map { group ->
                group.copy(
                    groupOrganization = group.groupOrganization.copy(
                        organizationRoles = groupOrganizationRoles
                    ),
                    groupUnits = group.groupUnits.map { unit ->
                        unit.copy(
                            unitRoles = groupUnitRoles
                        )
                    }.toSet()
                )
            }.toSet(),
        )
    }

}