package com.thomas.management.domain.model.mapper

import com.thomas.contract.messaging.management.group.GroupCreatedEvent
import com.thomas.contract.messaging.management.group.GroupDeletedEvent
import com.thomas.contract.messaging.management.group.GroupUpdatedEvent
import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.GroupSimpleEntity
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.UnitEntity
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
        id = this@toGroupDetailResponse.groupData.id,
        groupName = this@toGroupDetailResponse.groupData.groupName,
        groupDescription = this@toGroupDetailResponse.groupData.groupDescription,
        groupOrganization = this@toGroupDetailResponse.groupData.groupOrganization.toOrganizationResponse(),
        organizationRoles = this@toGroupDetailResponse.groupData.organizationRoles,
        groupUnits = this@toGroupDetailResponse.groupUnits.mapKeys { it.key.id },
        isActive = this@toGroupDetailResponse.groupData.isActive,
        createdAt = this@toGroupDetailResponse.groupData.createdAt,
        updatedAt = this@toGroupDetailResponse.groupData.updatedAt,
    )
}

suspend fun GroupUpsertRequest.toGroupEntity(
    organization: OrganizationEntity
) = coroutineScope {
    GroupSimpleEntity(
        groupName = this@toGroupEntity.groupName,
        groupDescription = this@toGroupEntity.groupDescription,
        groupOrganization = organization,
        organizationRoles = this@toGroupEntity.organizationRoles,
        isActive = this@toGroupEntity.isActive,
    )
}

suspend fun GroupSimpleEntity.updateFromRequest(
    request: GroupUpsertRequest
) = coroutineScope {
    this@updateFromRequest.copy(
        groupName = request.groupName,
        groupDescription = request.groupDescription,
        organizationRoles = request.organizationRoles,
        isActive = request.isActive,
        updatedAt = now(UTC),
    )
}

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

suspend fun UUID.toGroupDeletedEvent() = coroutineScope {
    GroupDeletedEvent(
        id = this@toGroupDeletedEvent,
        deletedAt = now(UTC),
    )
}

private suspend fun Map<UnitEntity, Set<SecurityUnitRole>>.toGroupUnitsEvent() = coroutineScope {
    this@toGroupUnitsEvent.mapKeys { it.key.id }
}
