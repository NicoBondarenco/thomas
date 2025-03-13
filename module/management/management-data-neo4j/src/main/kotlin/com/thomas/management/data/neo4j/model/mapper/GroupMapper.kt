package com.thomas.management.data.neo4j.model.mapper

import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.GroupEntity
import com.thomas.management.data.entity.UnitEntity
import com.thomas.management.data.neo4j.model.node.GroupNode
import com.thomas.management.data.neo4j.model.node.GroupOrganizationNode
import com.thomas.management.data.neo4j.model.node.GroupUnitNode

fun GroupNode.toGroupEntity(): GroupEntity = GroupEntity(
    id = this.id,
    groupName = this.groupName,
    groupDescription = this.groupDescription,
    groupOrganization = this.groupOrganization.organizationNode.toOrganizationEntity(),
    organizationRoles = this.groupOrganization.groupRoles,
    isActive = this.isActive,
    createdAt = this.createdAt.toOffsetDateTime(),
    updatedAt = this.updatedAt.toOffsetDateTime(),
)

fun GroupCompleteEntity.toGroupNode(): GroupNode = GroupNode(
    id = this.id,
    groupName = this.groupData.groupName,
    groupDescription = this.groupData.groupDescription,
    groupOrganization = GroupOrganizationNode(
        id = "${this.id}-${this.groupData.groupOrganization.id}",
        groupId = this.id,
        organizationId = this.groupData.groupOrganization.id,
        groupRoles = this.groupData.organizationRoles,
        organizationNode = this.groupData.groupOrganization.toOrganizationNode(),
    ),
    isActive = this.groupData.isActive,
    createdAt = this.groupData.createdAt.toZonedDateTime(),
    updatedAt = this.groupData.updatedAt.toZonedDateTime(),
    groupUnits = this.groupUnits.map { (unit, roles) ->
        GroupUnitNode(
            id = "${this.id}-${unit.id}",
            groupId = this.id,
            unitId = unit.id,
            groupRoles = roles,
            unitNode = unit.toUnitNode()
        )
    }
).apply {
    this.groupOrganization.groupNode = this
    this.groupUnits?.forEach { it.groupNode = this }
}

fun GroupNode.toGroupCompleteEntity(): GroupCompleteEntity = GroupCompleteEntity(
    groupData = this.toGroupEntity(),
    groupUnits = this.groupUnits?.let { gu -> mapOf(*gu.map { it.toGroupUnitEntity() }.toTypedArray()) } ?: mapOf(),
)

fun GroupUnitNode.toGroupUnitEntity(): Pair<UnitEntity, Set<SecurityUnitRole>> = Pair(
    first = this.unitNode.toUnitEntity(),
    second = this.groupRoles,
)
