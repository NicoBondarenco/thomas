package com.thomas.management.domain

import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.domain.model.request.UserActiveRequest
import com.thomas.management.domain.model.request.UserCreateRequest
import com.thomas.management.domain.model.request.UserUpdateRequest
import com.thomas.management.domain.model.response.UserDetailResponse
import com.thomas.management.domain.model.response.UserPageResponse
import java.time.OffsetDateTime
import java.util.UUID

interface UserService {

    @Suppress("LongParameterList")
    fun page(
        keywordText: String? = null,
        isActive: Boolean? = null,
        createdStart: OffsetDateTime? = null,
        createdEnd: OffsetDateTime? = null,
        updatedStart: OffsetDateTime? = null,
        updatedEnd: OffsetDateTime? = null,
        pageable: PageRequest = PageRequest(),
    ): PageResponse<UserPageResponse>

    fun one(
        id: UUID,
    ): UserDetailResponse

    fun create(
        request: UserCreateRequest,
    ): UserDetailResponse

    fun update(
        id: UUID,
        request: UserUpdateRequest,
    ): UserDetailResponse

    fun active(
        id: UUID,
        request: UserActiveRequest,
    ): UserPageResponse

}
