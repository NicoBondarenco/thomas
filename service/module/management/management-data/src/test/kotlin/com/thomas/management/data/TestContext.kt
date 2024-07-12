package com.thomas.management.data

import com.thomas.core.model.general.Gender.CIS_MALE
import com.thomas.core.model.security.SecurityUser
import java.time.LocalDate.now
import java.util.UUID.randomUUID

val securityUser: SecurityUser
    get() = SecurityUser(
        randomUUID(),
        "Security",
        "User",
        "security.user@test.com",
        "16988776655",
        null,
        now(),
        CIS_MALE,
        true,
        listOf(),
        listOf(),
    )
