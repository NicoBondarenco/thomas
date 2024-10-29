package com.thomas.management.domain.util

import com.thomas.core.model.general.Gender
import com.thomas.core.model.security.SecurityOrganization
import com.thomas.core.model.security.SecurityUser
import com.thomas.core.util.StringUtils.randomEmail
import com.thomas.core.util.StringUtils.randomPhone
import com.thomas.core.util.StringUtils.randomString
import java.time.LocalDate
import java.util.UUID.randomUUID

internal val securityUser: SecurityUser
    get() = SecurityUser(
        userId = randomUUID(),
        firstName = randomString(numbers = false),
        lastName = randomString(numbers = false),
        mainEmail = randomEmail(),
        phoneNumber = randomPhone(),
        profilePhoto = null,
        birthDate = LocalDate.now(),
        userGender = Gender.entries.random(),
        isActive = true,
        userOrganization = securityOrganization,
        userGroups = setOf(),
        userUnits = setOf(),
    )

internal val securityOrganization: SecurityOrganization
    get() = SecurityOrganization(
        organizationId = randomUUID(),
        organizationName = randomString(),
        organizationRoles = setOf(),
    )