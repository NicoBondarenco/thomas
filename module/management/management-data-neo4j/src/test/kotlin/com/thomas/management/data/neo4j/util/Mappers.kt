package com.thomas.management.data.neo4j.util

import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.GroupSimpleEntity

fun GroupCompleteEntity.toGroupSimpleEntity() = GroupSimpleEntity(
    id = this.id,
    groupName = this.groupName,
    groupDescription = this.groupDescription,
    groupOrganization = this.groupOrganization,
    organizationRoles = this.organizationRoles,
    isActive = this.isActive,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)