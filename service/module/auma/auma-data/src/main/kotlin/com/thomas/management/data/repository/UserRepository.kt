package com.thomas.management.data.repository

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.UserGroupsEntity
import com.thomas.management.data.entity.UserEntity
import java.util.UUID

interface UserRepository {

    @Suppress("LongParameterList")
    suspend fun page(
        keywordText: String? = null,
        isActive: Boolean? = null,
        pageable: PageRequestPeriod = PageRequestPeriod()
    ): PageResponse<UserEntity>

    suspend fun one(
        id: UUID,
    ): UserGroupsEntity?

    suspend fun hasAnotherWithSameMainEmail(
        id: UUID,
        mainEmail: String,
    ): Boolean

    suspend fun hasAnotherWithSameDocumentNumber(
        id: UUID,
        documentNumber: String,
    ): Boolean

    suspend fun hasAnotherWithSamePhoneNumber(
        id: UUID,
        phoneNumber: String,
    ): Boolean

    suspend fun signup(
        entity: UserEntity,
    ): UserEntity

    suspend fun create(
        entity: UserGroupsEntity,
    ): UserGroupsEntity

    suspend fun update(
        entity: UserGroupsEntity,
    ): UserGroupsEntity

}
