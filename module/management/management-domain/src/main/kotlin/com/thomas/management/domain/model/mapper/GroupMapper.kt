package com.thomas.management.domain.model.mapper

import com.thomas.contract.messaging.management.group.GroupCreatedEvent
import com.thomas.contract.messaging.management.group.GroupDeletedEvent
import com.thomas.contract.messaging.management.group.GroupUpdatedEvent
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.GroupSimpleEntity
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.UnitRoleEntity
import com.thomas.management.domain.model.request.GroupUpsertRequest
import com.thomas.management.domain.model.response.GroupDetailResponse
import com.thomas.management.domain.model.response.GroupSimpleResponse
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID
import kotlinx.coroutines.coroutineScope

suspend fun GroupSimpleEntity.toGroupSimpleResponse() = coroutineScope {
    GroupSimpleResponse(
        id = this@toGroupSimpleResponse.id,
        groupName = this@toGroupSimpleResponse.groupName,
        groupDescription = this@toGroupSimpleResponse.groupDescription,
        groupOrganization = this@toGroupSimpleResponse.groupOrganization.toOrganizationResponse(),
        isActive = this@toGroupSimpleResponse.isActive,
        createdAt = this@toGroupSimpleResponse.createdAt,
        updatedAt = this@toGroupSimpleResponse.updatedAt,
    )
}

suspend fun GroupCompleteEntity.toGroupDetailResponse() = coroutineScope {
    GroupDetailResponse(
        id = this@toGroupDetailResponse.id,
        groupName = this@toGroupDetailResponse.groupName,
        groupDescription = this@toGroupDetailResponse.groupDescription,
        groupOrganization = this@toGroupDetailResponse.groupOrganization.toOrganizationResponse(),
        organizationRoles = this@toGroupDetailResponse.organizationRoles,
        groupUnits = this@toGroupDetailResponse.groupUnits.associate { it.roleUnit.id to it.roleList },
        isActive = this@toGroupDetailResponse.isActive,
        createdAt = this@toGroupDetailResponse.createdAt,
        updatedAt = this@toGroupDetailResponse.updatedAt,
    )
}

suspend fun GroupUpsertRequest.toGroupCompleteEntity(
    organization: OrganizationEntity,
    units: Set<UnitRoleEntity>
) = coroutineScope {
    GroupCompleteEntity(
        groupName = this@toGroupCompleteEntity.groupName,
        groupDescription = this@toGroupCompleteEntity.groupDescription,
        groupOrganization = organization,
        organizationRoles = this@toGroupCompleteEntity.organizationRoles,
        isActive = this@toGroupCompleteEntity.isActive,
        groupUnits = units
    )
}

suspend fun GroupCompleteEntity.updateFromRequest(
    request: GroupUpsertRequest,
    units: Set<UnitRoleEntity>
) = coroutineScope {
    this@updateFromRequest.copy(
        groupName = request.groupName,
        groupDescription = request.groupDescription,
        organizationRoles = request.organizationRoles,
        isActive = request.isActive,
        updatedAt = now(UTC),
        groupUnits = units,
    )
}

suspend fun GroupCompleteEntity.toGroupCreatedEvent() = coroutineScope {
    GroupCreatedEvent(
        id = this@toGroupCreatedEvent.id,
        groupName = this@toGroupCreatedEvent.groupName,
        groupDescription = this@toGroupCreatedEvent.groupDescription,
        groupOrganization = this@toGroupCreatedEvent.groupOrganization.id,
        organizationRoles = this@toGroupCreatedEvent.organizationRoles,
        groupUnits = this@toGroupCreatedEvent.groupUnits.toGroupUnitsEvent(),
        isActive = this@toGroupCreatedEvent.isActive,
        createdAt = this@toGroupCreatedEvent.createdAt,
        updatedAt = this@toGroupCreatedEvent.updatedAt,
    )
}

suspend fun GroupCompleteEntity.toGroupUpdatedEvent() = coroutineScope {
    GroupUpdatedEvent(
        id = this@toGroupUpdatedEvent.id,
        groupName = this@toGroupUpdatedEvent.groupName,
        groupDescription = this@toGroupUpdatedEvent.groupDescription,
        groupOrganization = this@toGroupUpdatedEvent.groupOrganization.id,
        organizationRoles = this@toGroupUpdatedEvent.organizationRoles,
        groupUnits = this@toGroupUpdatedEvent.groupUnits.toGroupUnitsEvent(),
        isActive = this@toGroupUpdatedEvent.isActive,
        createdAt = this@toGroupUpdatedEvent.createdAt,
        updatedAt = this@toGroupUpdatedEvent.updatedAt,
    )
}

suspend fun UUID.toGroupDeletedEvent() = coroutineScope {
    GroupDeletedEvent(
        id = this@toGroupDeletedEvent,
        deletedAt = now(UTC),
    )
}

private suspend fun Set<UnitRoleEntity>.toGroupUnitsEvent() = coroutineScope {
    this@toGroupUnitsEvent.associate { it.roleUnit.id to it.roleList }
}
