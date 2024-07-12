package com.thomas.management.data.exposed.extension

import com.thomas.core.generator.GroupGenerator
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_CREATE
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_READ
import com.thomas.core.model.security.SecurityRole.ROLE_USER_CREATE
import com.thomas.core.model.security.SecurityRole.ROLE_USER_READ
import com.thomas.management.data.entity.GroupCompleteEntity
import java.util.UUID.fromString

val groupUpsert: GroupCompleteEntity
    get() = GroupGenerator.generate().let {
        GroupCompleteEntity(
            groupName = it.groupName,
            groupDescription = it.groupDescription,
            isActive = it.isActive,
            creatorId = fromString("436d12f3-d23e-4346-b847-37188521a968"),
            groupRoles = setOf(
                ROLE_USER_READ,
                ROLE_USER_CREATE,
                ROLE_GROUP_READ,
                ROLE_GROUP_CREATE,
            )
        )
    }
