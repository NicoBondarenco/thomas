package com.thomas.management.data.repository

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.GroupEntity
import java.util.UUID

interface GroupRepository {

    suspend fun page(keywordText: String?, isActive: Boolean?, organizationId: UUID, pageable: PageRequestPeriod): PageResponse<GroupEntity>

    suspend fun one(id: UUID, organizationId: UUID): GroupCompleteEntity?

    suspend fun create(entity: GroupCompleteEntity): GroupCompleteEntity

    suspend fun update(entity: GroupCompleteEntity): GroupCompleteEntity

    suspend fun delete(id: UUID)

    suspend fun allByIds(ids: Set<UUID>, organizationId: UUID): Set<GroupEntity>

    suspend fun hasAnotherWithName(id: UUID, organizationId: UUID, groupName: String): Boolean

}