package com.thomas.management.domain

import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.domain.model.request.GroupActiveRequest
import com.thomas.management.domain.model.request.GroupCreateRequest
import com.thomas.management.domain.model.request.GroupUpdateRequest
import com.thomas.management.domain.model.response.GroupDetailResponse
import com.thomas.management.domain.model.response.GroupPageResponse
import java.time.OffsetDateTime
import java.util.UUID

interface GroupService {

    fun page(
        keywordText: String? = null,
        isActive: Boolean? = null,
        createdStart: OffsetDateTime? = null,
        createdEnd: OffsetDateTime? = null,
        updatedStart: OffsetDateTime? = null,
        updatedEnd: OffsetDateTime? = null,
        pageable: PageRequest
    ): PageResponse<GroupPageResponse>

    fun one(
        id: UUID
    ): GroupDetailResponse

    fun create(
        request: GroupCreateRequest
    ): GroupDetailResponse

    fun update(
        id: UUID,
        request: GroupUpdateRequest
    ): GroupDetailResponse

    fun delete(
        id: UUID
    )

    fun active(
        id: UUID,
        request: GroupActiveRequest
    ): GroupPageResponse

}