package com.thomas.management.data.repository

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.UnitEntity
import java.util.UUID

interface UnitRepository {

    suspend fun one(id: UUID, organizationId: UUID): UnitEntity?

    suspend fun page(organizationId: UUID, keywordText: String?, isActive: Boolean?, pageable: PageRequestPeriod): PageResponse<UnitEntity>

    suspend fun create(entity: UnitEntity): UnitEntity

    suspend fun update(entity: UnitEntity): UnitEntity

    suspend fun delete(id: UUID)

    suspend fun limitReached(id: UUID, organizationId: UUID): Boolean

    suspend fun hasAnotherWithName(id: UUID, organizationId: UUID, unitName: String): Boolean

    suspend fun hasAnotherWithDocument(id: UUID, organizationId: UUID, documentNumber: String): Boolean

    suspend fun allByIds(ids: Set<UUID>, organizationId: UUID): Set<UnitEntity>

    suspend fun allByOrganization(organizationId: UUID): Set<UnitEntity>

}
