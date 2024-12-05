package com.thomas.management.domain.util

import com.thomas.core.util.StringUtils.randomString
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.GroupEntity
import com.thomas.management.domain.model.request.GroupUpsertRequest

internal val groupEntity: GroupEntity
    get() = GroupEntity(
        groupName = randomString(),
        groupDescription = null,
        groupOrganization = organizationEntity,
        organizationRoles = setOf(),
    )

internal val groupCompleteEntity: GroupCompleteEntity
    get() = GroupCompleteEntity(
        groupData = groupEntity,
        groupUnits = mapOf(),
    )

internal val groupUpsertRequest: GroupUpsertRequest
    get() = GroupUpsertRequest(
        groupName = randomString(),
        groupDescription = listOf(randomString(), null).random(),
        organizationRoles = setOf(),
        groupUnits = mapOf(),
        isActive = listOf(true, false).random(),
    )
