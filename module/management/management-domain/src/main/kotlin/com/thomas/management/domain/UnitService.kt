package com.thomas.management.domain

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.domain.model.request.UnitUpsertRequest
import com.thomas.management.domain.model.response.UnitResponse
import java.util.UUID

interface UnitService {

    suspend fun page(keywordText: String?, isActive: Boolean?, pageable: PageRequestPeriod): PageResponse<UnitResponse>

    suspend fun one(id: UUID): UnitResponse

    suspend fun create(request: UnitUpsertRequest): UnitResponse

    suspend fun update(id: UUID, request: UnitUpsertRequest): UnitResponse

    suspend fun delete(id: UUID)

}
