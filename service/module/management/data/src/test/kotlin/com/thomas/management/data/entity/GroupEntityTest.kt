package com.thomas.management.data.entity

import java.time.OffsetDateTime
import java.util.UUID

@Suppress("UnusedDataClassCopyResult")
class GroupEntityTest : GroupEntityBaseTest<GroupEntity>() {

    override fun entity(
        id: UUID,
        groupName: String,
        groupDescription: String?,
        isActive: Boolean,
        creatorId: UUID,
        createdAt: OffsetDateTime,
        updatedAt: OffsetDateTime,
    ) = GroupEntity(
        id = id,
        groupName = groupName,
        groupDescription = groupDescription,
        isActive = isActive,
        creatorId = creatorId,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

}
