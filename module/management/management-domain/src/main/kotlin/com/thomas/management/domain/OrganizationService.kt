package com.thomas.management.domain

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.domain.model.request.OrganizationUpsertRequest
import com.thomas.management.domain.model.response.OrganizationResponse
import java.util.UUID

interface OrganizationService {

    suspend fun page(keywordText: String?, isActive: Boolean?, pageable: PageRequestPeriod): PageResponse<OrganizationResponse>

    suspend fun one(id: UUID): OrganizationResponse

    suspend fun create(request: OrganizationUpsertRequest): OrganizationResponse

    suspend fun update(id: UUID, request: OrganizationUpsertRequest): OrganizationResponse

}
