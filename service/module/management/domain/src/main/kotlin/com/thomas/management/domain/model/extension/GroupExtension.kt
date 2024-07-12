package com.thomas.management.domain.model.extension

import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.GroupEntity
import com.thomas.management.domain.model.request.GroupActiveRequest
import com.thomas.management.domain.model.request.GroupCreateRequest
import com.thomas.management.domain.model.request.GroupUpdateRequest
import com.thomas.management.domain.model.response.GroupDetailResponse
import com.thomas.management.domain.model.response.GroupPageResponse
import com.thomas.management.message.event.GroupUpsertedEvent
import java.time.OffsetDateTime
import java.time.ZoneOffset.UTC

fun GroupEntity.toGroupPageResponse() = GroupPageResponse(
    id = this.id,
    groupName = this.groupName,
    groupDescription = this.groupDescription,
    isActive = this.isActive,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)

fun GroupCompleteEntity.toGroupPageResponse() = GroupPageResponse(
    id = this.id,
    groupName = this.groupName,
    groupDescription = this.groupDescription,
    isActive = this.isActive,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)

fun GroupCompleteEntity.toGroupDetailResponse() = GroupDetailResponse(
    id = this.id,
    groupName = this.groupName,
    groupDescription = this.groupDescription,
    isActive = this.isActive,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    groupRoles = this.groupRoles
)

fun GroupCompleteEntity.toGroupUpsertedEvent() = GroupUpsertedEvent(
    id = this.id,
    groupName = this.groupName,
    groupDescription = this.groupDescription,
    isActive = this.isActive,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    groupRoles = this.groupRoles
)

fun GroupCreateRequest.toGroupEntity() = GroupCompleteEntity(
    groupName = this.groupName,
    groupDescription = this.groupDescription,
    isActive = this.isActive,
    groupRoles = this.groupRoles
)

fun GroupCompleteEntity.updateFromRequest(
    request: GroupUpdateRequest
) = this.copy(
    groupName = request.groupName,
    groupDescription = request.groupDescription,
    isActive = request.isActive,
    groupRoles = request.groupRoles,
    updatedAt = OffsetDateTime.now(UTC),
)

fun GroupCompleteEntity.updateFromRequest(
    request: GroupActiveRequest
) = this.copy(
    isActive = request.isActive,
    updatedAt = OffsetDateTime.now(UTC),
)