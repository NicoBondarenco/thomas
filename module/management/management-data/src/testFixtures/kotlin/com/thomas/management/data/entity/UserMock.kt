package com.thomas.management.data.entity

import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.core.util.StringUtils.randomDocumentNumber
import com.thomas.core.util.StringUtils.randomEmail
import com.thomas.core.util.StringUtils.randomPhone
import com.thomas.core.util.StringUtils.randomString

val userEntity: UserSimpleEntity
    get() = UserSimpleEntity(
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

val userRolesEntity: UserSimpleEntity
    get() = userEntity.copy(
        organizationRoles = SecurityOrganizationRole.entries.shuffled().subList(0, 3).toSet(),
    )

val userCompleteEntity: UserCompleteEntity
    get() = UserCompleteEntity(
        firstName = randomString(numbers = false),
        lastName = randomString(numbers = false),
        documentNumber = randomDocumentNumber(),
        passwordSalt = randomString(),
        passwordHash = randomString(),
        userOrganization = organizationEntity,
        organizationRoles = setOf(),
        mainEmail = randomEmail(),
        mainPhone = randomPhone(),
        userGroups = setOf(),
        userUnits = setOf(),
    )

val userFullEntity: UserCompleteEntity
    get() = UserCompleteEntity(
        firstName = randomString(numbers = false),
        lastName = randomString(numbers = false),
        documentNumber = randomDocumentNumber(),
        passwordSalt = randomString(),
        passwordHash = randomString(),
        userOrganization = organizationEntity,
        organizationRoles = setOf(),
        mainEmail = randomEmail(),
        mainPhone = randomPhone(),
        userGroups = (1..3).map { groupFullEntity }.toSet(),
        userUnits = (1..3).map {
            UnitRoleEntity(
                roleUnit = unitEntity,
                roleList = SecurityUnitRole.entries.shuffled().subList(0, 3).toSet()
            )
        }.toSet(),
    )