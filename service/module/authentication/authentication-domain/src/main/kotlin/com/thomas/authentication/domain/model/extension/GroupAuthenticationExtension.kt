package com.thomas.authentication.domain.model.extension

import com.thomas.authentication.data.entity.GroupAuthenticationEntity
import com.thomas.core.model.security.SecurityGroup
import com.thomas.management.message.event.GroupUpsertedEvent

fun GroupAuthenticationEntity.updatedFromEvent(
    event: GroupUpsertedEvent
) = this.copy(
    groupName = event.groupName,
    groupDescription = event.groupDescription,
    isActive = event.isActive,
    createdAt = event.createdAt,
    updatedAt = event.updatedAt,
    groupRoles = event.groupRoles,
)

fun GroupUpsertedEvent.toGroupAuthenticationEntity() = GroupAuthenticationEntity(
    id = this.id,
    groupName = this.groupName,
    groupDescription = this.groupDescription,
    isActive = this.isActive,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    groupRoles = this.groupRoles,
)

fun GroupAuthenticationEntity.toSecurityGroup() = SecurityGroup(
    groupId = this.id,
    groupName = this.groupName,
    groupRoles = this.groupRoles.toList(),
)
