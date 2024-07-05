package com.thomas.management.data.repository

import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
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

    fun findById(
        id: UUID
    ): GroupEntity?

    fun findByIds(
        ids: List<UUID>
    ): List<GroupEntity>

    fun create(
        entity: GroupEntity
    ): GroupEntity

    fun update(
        entity: GroupEntity
    ): GroupEntity

    fun delete(
        id: UUID
    )

    fun hasAnotherWithSameGroupName(
        id: UUID,
        groupName: String
    ): Boolean

}
