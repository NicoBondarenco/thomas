package com.thomas.management.domain

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.domain.model.request.UserCreateRequest
import com.thomas.management.domain.model.request.UserUpdateRequest
import com.thomas.management.domain.model.response.UserDetailResponse
import com.thomas.management.domain.model.response.UserSimpleResponse
import java.util.UUID

interface UserService {

    suspend fun page(keywordText: String?, isActive: Boolean?, pageable: PageRequestPeriod): PageResponse<UserSimpleResponse>

    suspend fun one(id: UUID): UserDetailResponse

    suspend fun create(request: UserCreateRequest): UserDetailResponse

    suspend fun update(id: UUID, request: UserUpdateRequest): UserDetailResponse

}
