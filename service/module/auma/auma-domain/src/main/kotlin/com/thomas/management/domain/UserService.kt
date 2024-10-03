package com.thomas.management.domain

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.domain.model.request.UserActiveRequest
import com.thomas.management.domain.model.request.UserCreateRequest
import com.thomas.management.domain.model.request.UserSignupRequest
import com.thomas.management.domain.model.request.UserUpdateRequest
import com.thomas.management.domain.model.response.UserDetailResponse
import com.thomas.management.domain.model.response.UserPageResponse
import java.util.UUID

interface UserService {

    @Suppress("LongParameterList")
    suspend fun page(
        keywordText: String? = null,
        isActive: Boolean? = null,
        pageable: PageRequestPeriod = PageRequestPeriod()
    ): PageResponse<UserPageResponse>

    suspend fun one(
        id: UUID,
    ): UserDetailResponse

    suspend fun signup(
        request: UserSignupRequest
    ): UserPageResponse

    suspend fun create(
        request: UserCreateRequest,
    ): UserDetailResponse

    suspend fun update(
        id: UUID,
        request: UserUpdateRequest,
    ): UserDetailResponse

    suspend fun active(
        id: UUID,
        request: UserActiveRequest,
    ): UserPageResponse

}
