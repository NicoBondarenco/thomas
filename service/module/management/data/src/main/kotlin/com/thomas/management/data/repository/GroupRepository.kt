package com.thomas.management.data.repository

import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.GroupEntity
import java.time.OffsetDateTime
import java.util.UUID

interface GroupRepository {

    fun page(
        keywordText: String? = null,
        isActive: Boolean? = null,
        createdStart: OffsetDateTime? = null,
        createdEnd: OffsetDateTime? = null,
        updatedStart: OffsetDateTime? = null,
        updatedEnd: OffsetDateTime? = null,
        pageable: PageRequest
    ): PageResponse<GroupEntity>

    fun one(
        id: UUID
    ): GroupCompleteEntity?

    fun allByIds(
        ids: Set<UUID>
    ): Set<GroupEntity>

    fun create(
        entity: GroupCompleteEntity
    ): GroupCompleteEntity

    fun update(
        entity: GroupCompleteEntity
    ): GroupCompleteEntity

    fun delete(
        id: UUID
    )

    fun hasAnotherWithSameGroupName(
        id: UUID,
        groupName: String
    ): Boolean

}
