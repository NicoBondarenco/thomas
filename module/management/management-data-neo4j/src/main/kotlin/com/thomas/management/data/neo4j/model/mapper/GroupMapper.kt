package com.thomas.management.data.neo4j.model.mapper

import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.GroupSimpleEntity
import com.thomas.management.data.entity.UnitRoleEntity
import com.thomas.management.data.neo4j.model.node.GroupNode
import com.thomas.management.data.neo4j.model.node.GroupOrganizationNode
import com.thomas.management.data.neo4j.model.node.GroupUnitNode

fun GroupNode.toGroupSimpleEntity(): GroupSimpleEntity = GroupSimpleEntity(
    id = this.id,
    groupName = this.groupName,
    groupDescription = this.groupDescription,
    groupOrganization = this.groupOrganization.organizationNode.toOrganizationEntity(),
    organizationRoles = this.groupOrganization.groupRoles,
    isActive = this.isActive,
    createdAt = this.createdAt.toOffsetDateTime(),
    updatedAt = this.updatedAt.toOffsetDateTime(),
)

fun GroupNode.toGroupCompleteEntity(): GroupCompleteEntity = GroupCompleteEntity(
    id = this.id,
    groupName = this.groupName,
    groupDescription = this.groupDescription,
    groupOrganization = this.groupOrganization.organizationNode.toOrganizationEntity(),
    organizationRoles = this.groupOrganization.groupRoles,
    isActive = this.isActive,
    createdAt = this.createdAt.toOffsetDateTime(),
    updatedAt = this.updatedAt.toOffsetDateTime(),
    groupUnits = this.groupUnits?.map { it.toUnitRoleEntity() }?.toSet() ?: setOf()
)

fun GroupUnitNode.toUnitRoleEntity(): UnitRoleEntity = UnitRoleEntity(
    id = this.id,
    roleUnit = this.unitNode.toUnitEntity(),
    roleList = this.groupRoles,
)

fun GroupCompleteEntity.toGroupNode(): GroupNode = GroupNode(
    id = this.id,
    groupName = this.groupName,
    groupDescription = this.groupDescription,
    groupOrganization = GroupOrganizationNode(
        id = this.id,
        groupId = this.id,
        organizationId = this.groupOrganization.id,
        groupRoles = this.organizationRoles,
        organizationNode = this.groupOrganization.toOrganizationNode(),
    ),
    isActive = this.isActive,
    createdAt = this.createdAt.toZonedDateTime(),
    updatedAt = this.updatedAt.toZonedDateTime(),
    groupUnits = this.groupUnits.map {
        GroupUnitNode(
            id = it.id,
            groupId = this.id,
            unitId = it.roleUnit.id,
            groupRoles = it.roleList,
            unitNode = it.roleUnit.toUnitNode()
        )
    }
).apply {
    this.groupOrganization.groupNode = this
    this.groupUnits?.forEach { it.groupNode = this }
}