package com.thomas.management.data.repository

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.data.entity.UserEntity
import java.util.UUID

interface UserRepository {

    suspend fun page(keywordText: String? = null, isActive: Boolean? = null, organizationId: UUID, pageable: PageRequestPeriod): PageResponse<UserEntity>

    suspend fun one(id: UUID, organizationId: UUID): UserCompleteEntity?

    suspend fun create(entity: UserCompleteEntity): UserCompleteEntity

    suspend fun update(entity: UserCompleteEntity): UserCompleteEntity

    suspend fun updateSimple(entity: UserEntity): UserEntity

    suspend fun simpleByEmail(email: String): UserEntity?

    suspend fun hasAnotherWithDocument(id: UUID, organizationId: UUID, documentNumber: String): Boolean

    suspend fun hasAnotherWithEmail(id: UUID, mainEmail: String): Boolean

    suspend fun limitReached(id: UUID, organizationId: UUID): Boolean

    suspend fun byId(id: UUID): UserEntity?

    suspend fun findByUsername(username: String): UserCompleteEntity?

}
