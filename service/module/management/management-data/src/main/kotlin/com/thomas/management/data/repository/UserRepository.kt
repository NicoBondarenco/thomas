package com.thomas.management.data.repository

import com.thomas.core.model.pagination.PageRequestData
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.data.entity.UserEntity
import java.time.OffsetDateTime
import java.util.UUID

interface UserRepository {

    @Suppress("LongParameterList")
    fun page(
        keywordText: String? = null,
        isActive: Boolean? = null,
        createdStart: OffsetDateTime? = null,
        createdEnd: OffsetDateTime? = null,
        updatedStart: OffsetDateTime? = null,
        updatedEnd: OffsetDateTime? = null,
        pageable: PageRequestData,
    ): PageResponse<UserEntity>

    fun one(
        id: UUID,
    ): UserCompleteEntity?

    fun hasAnotherWithSameMainEmail(
        id: UUID,
        mainEmail: String,
    ): Boolean

    fun hasAnotherWithSameDocumentNumber(
        id: UUID,
        documentNumber: String,
    ): Boolean

    fun hasAnotherWithSamePhoneNumber(
        id: UUID,
        phoneNumber: String,
    ): Boolean

    fun signup(
        entity: UserEntity,
    ): UserEntity

    fun create(
        entity: UserCompleteEntity,
    ): UserCompleteEntity

    fun update(
        entity: UserCompleteEntity,
    ): UserCompleteEntity

}
