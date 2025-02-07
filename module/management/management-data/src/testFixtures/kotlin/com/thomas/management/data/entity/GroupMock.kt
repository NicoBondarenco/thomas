package com.thomas.management.data.entity

import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.core.util.StringUtils.randomString

val groupEntity: GroupEntity
    get() = GroupEntity(
        groupName = randomString(),
        groupDescription = null,
        groupOrganization = organizationEntity,
        organizationRoles = setOf(),
    )

val groupCompleteEntity: GroupCompleteEntity
    get() = GroupCompleteEntity(
        groupData = groupEntity,
        groupUnits = mapOf(),
    )

val groupFullEntity: GroupCompleteEntity
    get() = GroupCompleteEntity(
        groupData = groupEntity,
        groupUnits = (1..3).associate {
            unitEntity to SecurityUnitRole.entries.shuffled().subList(0, 3).toSet()
        },
    )