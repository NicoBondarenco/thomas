package com.thomas.management.domain

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.domain.model.request.GroupUpsertRequest
import com.thomas.management.domain.model.response.GroupDetailResponse
import com.thomas.management.domain.model.response.GroupSimpleResponse
import java.util.UUID

interface GroupService {

    suspend fun page(keywordText: String?, isActive: Boolean?, pageable: PageRequestPeriod): PageResponse<GroupSimpleResponse>

    suspend fun one(id: UUID): GroupDetailResponse

    suspend fun create(request: GroupUpsertRequest): GroupDetailResponse

    suspend fun update(id: UUID, request: GroupUpsertRequest): GroupDetailResponse

    suspend fun delete(id: UUID)

}