package com.thomas.management.domain.model.mapper

import com.thomas.contract.messaging.ApplicationEventType
import com.thomas.contract.messaging.ApplicationEventType.DELETE
import com.thomas.contract.messaging.management.group.GroupDataEvent
import com.thomas.contract.messaging.management.group.GroupManagementEvent
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

suspend fun GroupCompleteEntity.toGroupManagementEvent(type: ApplicationEventType) = coroutineScope {
    GroupManagementEvent(
        eventType = type,
        eventTimestamp = now(UTC),
        eventKey = this@toGroupManagementEvent.id,
        eventData = this@toGroupManagementEvent.toGroupDataEvent(),
    )
}

suspend fun GroupCompleteEntity.toGroupDataEvent() = coroutineScope {
    GroupDataEvent(
        id = this@toGroupDataEvent.id,
        groupName = this@toGroupDataEvent.groupName,
        groupDescription = this@toGroupDataEvent.groupDescription,
        groupOrganization = this@toGroupDataEvent.groupOrganization.id,
        organizationRoles = this@toGroupDataEvent.organizationRoles,
        groupUnits = this@toGroupDataEvent.groupUnits.toGroupUnitsEvent(),
        isActive = this@toGroupDataEvent.isActive,
        createdAt = this@toGroupDataEvent.createdAt,
        updatedAt = this@toGroupDataEvent.updatedAt,
    )
}


suspend fun UUID.toGroupManagementEvent() = coroutineScope {
    GroupManagementEvent(
        eventType = DELETE,
        eventTimestamp = now(UTC),
        eventKey = this@toGroupManagementEvent,
        eventData = null,
    )
}

private suspend fun Set<UnitRoleEntity>.toGroupUnitsEvent() = coroutineScope {
    this@toGroupUnitsEvent.associate { it.roleUnit.id to it.roleList }
}
