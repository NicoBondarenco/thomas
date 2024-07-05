package com.thomas.management.requests

import com.thomas.core.generator.GroupGenerator
import com.thomas.management.data.entity.GroupEntity
import com.thomas.management.domain.model.request.GroupActiveRequest
import com.thomas.management.domain.model.request.GroupCreateRequest
import com.thomas.management.domain.model.request.GroupUpdateRequest

val createGroupRequest: GroupCreateRequest
    get() = GroupGenerator.generate().let {
        GroupCreateRequest(
            groupName = it.groupName,
            groupDescription = it.groupDescription,
            isActive = it.isActive,
            groupRoles = listOf(),
        )
    }

val updateGroupRequest: GroupUpdateRequest
    get() = GroupGenerator.generate().let {
        GroupUpdateRequest(
            groupName = it.groupName,
            groupDescription = it.groupDescription,
            isActive = it.isActive,
            groupRoles = listOf(),
        )
    }

val activeGroupRequest = GroupActiveRequest(
    isActive = listOf(true, false).random()
)

fun GroupEntity.toGroupUpdateRequest() = GroupUpdateRequest(
    groupName = groupName,
    groupDescription = groupDescription,
    isActive = isActive,
    groupRoles = groupRoles,
)