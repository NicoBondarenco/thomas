package com.thomas.management.domain.util

import com.thomas.core.util.StringUtils.randomString
import com.thomas.management.domain.model.request.GroupUpsertRequest

internal val groupUpsertRequest: GroupUpsertRequest
    get() = GroupUpsertRequest(
        groupName = randomString(),
        groupDescription = listOf(randomString(), null).random(),
        organizationRoles = setOf(),
        groupUnits = mapOf(),
        isActive = listOf(true, false).random(),
    )
