package com.thomas.management.data.repository

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.OrganizationEntity
import java.util.UUID

interface OrganizationRepository {

    suspend fun hasAnotherWithName(id: UUID, organizationName: String): Boolean

    suspend fun hasAnotherWithRegistration(id: UUID, registrationNumber: String): Boolean

    suspend fun one(id: UUID): OrganizationEntity?

    suspend fun page(keywordText: String?, isActive: Boolean?, pageable: PageRequestPeriod): PageResponse<OrganizationEntity>

    suspend fun create(entity: OrganizationEntity): OrganizationEntity

    suspend fun update(entity: OrganizationEntity): OrganizationEntity

}
