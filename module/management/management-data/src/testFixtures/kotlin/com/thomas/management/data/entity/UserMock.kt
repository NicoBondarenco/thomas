package com.thomas.management.data.entity

import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.core.util.StringUtils.randomDocumentNumber
import com.thomas.core.util.StringUtils.randomEmail
import com.thomas.core.util.StringUtils.randomPhone
import com.thomas.core.util.StringUtils.randomString

val userEntity: UserEntity
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

val userRolesEntity: UserEntity
    get() = userEntity.copy(
        organizationRoles = SecurityOrganizationRole.entries.shuffled().subList(0, 3).toSet(),
    )

val userCompleteEntity: UserCompleteEntity
    get() = UserCompleteEntity(
        userData = userEntity,
        userGroups = setOf(),
        userUnits = mapOf(),
    )

val userFullEntity: UserCompleteEntity
    get() = UserCompleteEntity(
        userData = userRolesEntity,
        userGroups = (1..3).map { groupFullEntity }.toSet(),
        userUnits = (1..3).associate {
            unitEntity to SecurityUnitRole.entries.shuffled().subList(0, 3).toSet()
        },
    )