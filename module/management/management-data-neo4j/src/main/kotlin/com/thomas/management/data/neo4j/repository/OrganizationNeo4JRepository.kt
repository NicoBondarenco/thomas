package com.thomas.management.data.neo4j.repository

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.neo4j.filter.equalUnaccentedLower
import com.thomas.management.data.neo4j.filter.notEqual
import com.thomas.management.data.neo4j.model.mapper.toOrganizationEntity
import com.thomas.management.data.neo4j.model.mapper.toOrganizationNode
import com.thomas.management.data.neo4j.model.node.OrganizationNode
import com.thomas.management.data.repository.OrganizationRepository
import java.util.UUID
import org.neo4j.ogm.session.SessionFactory

class OrganizationNeo4JRepository(
    sessionFactory: SessionFactory
) : Neo4JRepository(sessionFactory), OrganizationRepository {

    override suspend fun hasAnotherWithName(
        id: UUID,
        organizationName: String
    ): Boolean {
        val idFilter = notEqual("id", id.toString())
        val nameFilter = equalUnaccentedLower("organization_name", organizationName)
        val total = sessionFactory.openSession().loadAll(OrganizationNode::class.java, idFilter.and(nameFilter))
        return total.isNotEmpty()
    }

    override suspend fun hasAnotherWithRegistration(id: UUID, registrationNumber: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun one(
        id: UUID
    ): OrganizationEntity? = sessionFactory
        .openSession()
        .load(OrganizationNode::class.java, id.toString())
        ?.toOrganizationEntity()

    override suspend fun page(keywordText: String?, isActive: Boolean?, pageable: PageRequestPeriod): PageResponse<OrganizationEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun create(
        entity: OrganizationEntity
    ): OrganizationEntity = save(entity)

    override suspend fun update(
        entity: OrganizationEntity
    ): OrganizationEntity = save(entity)

    private suspend fun save(
        entity: OrganizationEntity
    ): OrganizationEntity = transaction { session ->
        entity.apply {
            session.save(this.toOrganizationNode())
        }
    }

}
