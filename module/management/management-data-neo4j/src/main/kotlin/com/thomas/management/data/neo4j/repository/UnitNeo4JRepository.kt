package com.thomas.management.data.neo4j.repository

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.database.neo4j.repository.Neo4JRepository
import com.thomas.management.data.entity.UnitEntity
import com.thomas.management.data.neo4j.model.mapper.toUnitEntity
import com.thomas.management.data.neo4j.model.node.OrganizationNode
import com.thomas.management.data.neo4j.model.node.UnitNode
import com.thomas.management.data.repository.UnitRepository
import java.util.UUID
import org.neo4j.ogm.cypher.ComparisonOperator.EQUALS
import org.neo4j.ogm.cypher.Filter
import org.neo4j.ogm.session.SessionFactory

class UnitNeo4JRepository(
    sessionFactory: SessionFactory
) : Neo4JRepository(sessionFactory), UnitRepository {

    override suspend fun one(id: UUID, organizationId: UUID): UnitEntity? {
        val idFilter = Filter("id", EQUALS, id.toString())
        val organizationFilter = Filter("id", EQUALS, organizationId.toString())
        organizationFilter.nestedPropertyType = OrganizationNode::class.java
        organizationFilter.nestedPropertyName = "unitOrganization"
        return sessionFactory.openSession()
            .loadAll(UnitNode::class.java, idFilter.and(organizationFilter))
            .firstOrNull()
            ?.toUnitEntity()
    }

    override suspend fun page(organizationId: UUID, keywordText: String?, isActive: Boolean?, pageable: PageRequestPeriod): PageResponse<UnitEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun create(entity: UnitEntity): UnitEntity {
        TODO("Not yet implemented")
    }

    override suspend fun update(entity: UnitEntity): UnitEntity {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: UUID) {
        TODO("Not yet implemented")
    }

    override suspend fun limitReached(id: UUID, organizationId: UUID): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun hasAnotherWithName(id: UUID, organizationId: UUID, unitName: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun hasAnotherWithDocument(id: UUID, organizationId: UUID, documentNumber: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun allByIds(ids: Set<UUID>, organizationId: UUID): Set<UnitEntity> {
        TODO("Not yet implemented")
    }

}