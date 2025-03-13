package com.thomas.management.data.entity

import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.core.util.StringUtils.randomString

val groupEntity: GroupSimpleEntity
    get() = GroupSimpleEntity(
        groupName = randomString(),
        groupDescription = null,
        groupOrganization = organizationEntity,
        organizationRoles = setOf(),
    )

val groupCompleteEntity: GroupCompleteEntity
    get() = GroupCompleteEntity(
        groupName = randomString(),
        groupDescription = null,
        groupOrganization = organizationEntity,
        organizationRoles = setOf(),
    )

val groupFullEntity: GroupCompleteEntity
    get() = GroupCompleteEntity(
        groupName = randomString(),
        groupDescription = null,
        groupOrganization = organizationEntity,
        organizationRoles = setOf(),
        groupUnits = (1..3).map {
            GroupUnitEntity(
                groupUnit = unitEntity,
                groupRoles = SecurityUnitRole.entries.shuffled().subList(0, 3).toSet()
            )
        }.toSet(),
    )