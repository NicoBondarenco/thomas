package com.thomas.management.domain.util

import com.thomas.core.util.StringUtils.randomString
import com.thomas.management.data.entity.GroupEntity

internal val groupEntity: GroupEntity
    get() = GroupEntity(
        groupName = randomString(),
        groupDescription = null,
        groupOrganization = organizationEntity,
        organizationRoles = setOf(),
    )
