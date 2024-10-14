package com.thomas.core.generator

import com.thomas.core.generator.GroupGenerator.generateSecurityGroupSet
import com.thomas.core.generator.OrganizationMemberGenerator.generateSecurityMemberSet
import com.thomas.core.generator.OrganizationMemberGenerator.generateSecurityOrganization
import com.thomas.core.generator.PersonGenerator.generatePerson
import com.thomas.core.generator.RoleGenerator.generateOrganizationRoles
import com.thomas.core.model.security.SecurityMemberRole
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
            userMembers = generateSecurityMemberSet(),
        )
    }

    fun generateSecurityUserWithRoles(
        userOrganizationRoles: Set<SecurityOrganizationRole> = setOf(),
        userMemberRoles: Set<SecurityMemberRole> = setOf(),
        groupOrganizationRoles: Set<SecurityOrganizationRole> = setOf(),
        groupMemberRoles: Set<SecurityMemberRole> = setOf(),
    ): SecurityUser = generateSecurityUser().let { user ->
        user.copy(
            userOrganization = user.userOrganization.copy(
                organizationRoles = userOrganizationRoles
            ),
            userMembers = user.userMembers.map { member ->
                member.copy(
                    memberRoles = userMemberRoles
                )
            }.toSet(),
            userGroups = user.userGroups.map { group ->
                group.copy(
                    groupOrganization = group.groupOrganization.copy(
                        organizationRoles = groupOrganizationRoles
                    ),
                    groupMembers = group.groupMembers.map { member ->
                        member.copy(
                            memberRoles = groupMemberRoles
                        )
                    }.toSet()
                )
            }.toSet(),
        )
    }

}