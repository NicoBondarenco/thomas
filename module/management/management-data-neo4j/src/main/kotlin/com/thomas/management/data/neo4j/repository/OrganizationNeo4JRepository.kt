package com.thomas.management.data.neo4j.repository

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.repository.OrganizationRepository
import java.util.UUID
import org.neo4j.ogm.session.SessionFactory

class OrganizationNeo4JRepository(
    private val sessionFactory: SessionFactory
): OrganizationRepository {

    override suspend fun hasAnotherWithName(id: UUID, organizationName: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun hasAnotherWithRegistration(id: UUID, registrationNumber: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun one(id: UUID): OrganizationEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun page(keywordText: String?, isActive: Boolean?, pageable: PageRequestPeriod): PageResponse<OrganizationEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun create(entity: OrganizationEntity): OrganizationEntity {
        TODO("Not yet implemented")
    }

    override suspend fun update(entity: OrganizationEntity): OrganizationEntity {
        TODO("Not yet implemented")
    }

}