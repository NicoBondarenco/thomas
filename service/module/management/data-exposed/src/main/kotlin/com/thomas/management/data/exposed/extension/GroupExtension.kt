package com.thomas.management.data.exposed.extension

import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.GroupEntity
import com.thomas.management.data.exposed.mapping.GroupExposedEntity
import com.thomas.management.data.exposed.mapping.UserTable
import org.jetbrains.exposed.dao.id.EntityID

fun GroupExposedEntity.toGroupEntity() = GroupEntity(
    id = this.id.value,
    groupName = this.groupName,
    groupDescription = this.groupDescription,
    isActive = this.isActive,
    creatorId = this.creatorId.value,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)

fun GroupExposedEntity.toGroupCompleteEntity() = GroupCompleteEntity(
    id = this.id.value,
    groupName = this.groupName,
    groupDescription = this.groupDescription,
    isActive = this.isActive,
    creatorId = this.creatorId.value,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    groupRoles = this.roleList.map { it.roleAuthority }.toSet()
)

fun GroupExposedEntity.updateFromGroupEntity(
    entity: GroupCompleteEntity
) = this.apply {
    this.groupName = entity.groupName
    this.groupDescription = entity.groupDescription
    this.isActive = entity.isActive
    this.creatorId = EntityID(entity.creatorId, UserTable)
    this.createdAt = entity.createdAt
    this.updatedAt = entity.updatedAt
}
