package com.thomas.management.data.repository

import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.GroupEntity
import com.thomas.management.data.entity.UserEntity
import com.thomas.management.data.entity.UserGroupsEntity
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
        pageable: PageRequest,
    ): PageResponse<UserEntity>

    fun findById(
        id: UUID,
    ): UserEntity?

    fun findByIdWithGroups(
        id: UUID,
    ): UserGroupsEntity?

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

    fun create(
        entity: UserEntity,
        groups: List<GroupEntity>,
    ): UserGroupsEntity

    fun update(
        entity: UserEntity,
        groups: List<GroupEntity>,
    ): UserGroupsEntity

    fun update(
        entity: UserEntity,
    ): UserEntity

}
