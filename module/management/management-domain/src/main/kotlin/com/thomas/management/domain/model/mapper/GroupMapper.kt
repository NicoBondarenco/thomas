package com.thomas.management.domain.model.mapper

import com.thomas.contract.messaging.management.group.GroupCreatedEvent
import com.thomas.contract.messaging.management.group.GroupDeletedEvent
import com.thomas.contract.messaging.management.group.GroupUpdatedEvent
import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.UnitEntity
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import kotlinx.coroutines.coroutineScope

suspend fun GroupCompleteEntity.toGroupCreatedEvent() = coroutineScope {
    GroupCreatedEvent(
        id = this@toGroupCreatedEvent.id,
        groupName = this@toGroupCreatedEvent.groupData.groupName,
        groupDescription = this@toGroupCreatedEvent.groupData.groupDescription,
        groupOrganization = this@toGroupCreatedEvent.groupData.groupOrganization.id,
        organizationRoles = this@toGroupCreatedEvent.groupData.organizationRoles,
        groupUnits = this@toGroupCreatedEvent.groupUnits.toGroupUnitsEvent(),
        isActive = this@toGroupCreatedEvent.groupData.isActive,
        createdAt = this@toGroupCreatedEvent.groupData.createdAt,
        updatedAt = this@toGroupCreatedEvent.groupData.updatedAt,
    )
}

suspend fun GroupCompleteEntity.toGroupUpdatedEvent() = coroutineScope {
    GroupUpdatedEvent(
        id = this@toGroupUpdatedEvent.id,
        groupName = this@toGroupUpdatedEvent.groupData.groupName,
        groupDescription = this@toGroupUpdatedEvent.groupData.groupDescription,
        groupOrganization = this@toGroupUpdatedEvent.groupData.groupOrganization.id,
        organizationRoles = this@toGroupUpdatedEvent.groupData.organizationRoles,
        groupUnits = this@toGroupUpdatedEvent.groupUnits.toGroupUnitsEvent(),
        isActive = this@toGroupUpdatedEvent.groupData.isActive,
        createdAt = this@toGroupUpdatedEvent.groupData.createdAt,
        updatedAt = this@toGroupUpdatedEvent.groupData.updatedAt,
    )
}

suspend fun GroupCompleteEntity.toGroupDeletedEvent() = coroutineScope {
    GroupDeletedEvent(
        id = this@toGroupDeletedEvent.id,
        deletedAt = now(UTC),
    )
}

private suspend fun Map<UnitEntity, Set<SecurityUnitRole>>.toGroupUnitsEvent() = coroutineScope {
    this@toGroupUnitsEvent.mapKeys { it.key.id }
}
