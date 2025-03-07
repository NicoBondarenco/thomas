package com.thomas.management.data.neo4j.repository

import com.thomas.core.extension.isHigher
import com.thomas.core.extension.unaccentedLower
import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.database.neo4j.filter.count
import com.thomas.database.neo4j.filter.equals
import com.thomas.database.neo4j.filter.equalsUnaccentedLower
import com.thomas.database.neo4j.filter.greaterThanEquals
import com.thomas.database.neo4j.filter.inValues
import com.thomas.database.neo4j.filter.isTrue
import com.thomas.database.neo4j.filter.lessThanEquals
import com.thomas.database.neo4j.filter.likeUnaccentedLower
import com.thomas.database.neo4j.filter.notEquals
import com.thomas.database.neo4j.filter.or
import com.thomas.database.neo4j.filter.page
import com.thomas.database.neo4j.repository.Neo4JRepository
import com.thomas.management.data.entity.UnitEntity
import com.thomas.management.data.neo4j.model.mapper.toUnitEntity
import com.thomas.management.data.neo4j.model.mapper.toUnitNode
import com.thomas.management.data.neo4j.model.node.OrganizationNode
import com.thomas.management.data.neo4j.model.node.UnitNode
import com.thomas.management.data.repository.UnitRepository
import java.util.UUID
import org.neo4j.ogm.cypher.Filters
import org.neo4j.ogm.session.SessionFactory

class UnitNeo4JRepository(
    sessionFactory: SessionFactory
) : Neo4JRepository(sessionFactory), UnitRepository {

    override suspend fun one(
        id: UUID,
        organizationId: UUID
    ): UnitEntity? = sessionFactory.openSession().loadAll(
        UnitNode::class.java,
        equals(UnitNode::id, id).and(equals(OrganizationNode::id, UnitNode::unitOrganization, organizationId))
    ).firstOrNull()?.toUnitEntity()

    override suspend fun page(
        organizationId: UUID,
        keywordText: String?,
        isActive: Boolean?,
        pageable: PageRequestPeriod
    ): PageResponse<UnitEntity> = sessionFactory.page<UnitNode>(
        listOfNotNull(
            equals(OrganizationNode::id, UnitNode::unitOrganization, organizationId),
            keywordText?.let {
                or(
                    likeUnaccentedLower(UnitNode::unitName, it.unaccentedLower()),
                    likeUnaccentedLower(UnitNode::fantasyName, it.unaccentedLower()),
                    likeUnaccentedLower(UnitNode::documentNumber, it),
                    likeUnaccentedLower(UnitNode::mainEmail, it.unaccentedLower()),
                )
            },
            isActive?.let {
                isTrue(UnitNode::isActive)
            },
            pageable.createdStart?.let {
                greaterThanEquals(UnitNode::createdAt, it.toZonedDateTime())
            },
            pageable.createdEnd?.let {
                lessThanEquals(UnitNode::createdAt, it.toZonedDateTime())
            },
            pageable.updatedStart?.let {
                greaterThanEquals(UnitNode::updatedAt, it.toZonedDateTime())
            },
            pageable.updatedEnd?.let {
                lessThanEquals(UnitNode::updatedAt, it.toZonedDateTime())
            },
        ),
        pageable
    ).map {
        it.toUnitEntity()
    }

    override suspend fun create(
        entity: UnitEntity
    ): UnitEntity = save(entity)

    override suspend fun update(
        entity: UnitEntity
    ): UnitEntity = save(entity)

    override suspend fun delete(
        id: UUID
    ): Unit = transaction {
        sessionFactory.openSession().delete(
            UnitNode::class.java,
            Filters(equals(UnitNode::id, id)),
            false
        )
    }

    override suspend fun limitReached(
        id: UUID,
        organizationId: UUID
    ): Boolean = sessionFactory.openSession().let {
        val result = it.query(
            """
                MATCH(u: Unit) WHERE NOT(u.id = ${"$"}`unit_id`)
                MATCH(o: Organization) WHERE o.id = ${"$"}`organization_id`
                MATCH(u)-[:`UNIT_BELONGS_TO_ORGANIZATION`]->(o)
                WITH o.maximum_units as max_units, COUNT(u) as total_units
                RETURN (max_units - total_units) as available
            """.trimIndent(),
            mapOf(
                "unit_id" to id.toString(),
                "organization_id" to organizationId.toString(),
            )
        )
        val available: Long = (result.queryResults().firstOrNull()?.get("available") ?: 0L) as Long
        available < 1L
    }

    override suspend fun hasAnotherWithName(
        id: UUID,
        organizationId: UUID,
        unitName: String
    ): Boolean = sessionFactory.count<UnitNode>(
        listOf(
            notEquals(UnitNode::id, id),
            equals(OrganizationNode::id, UnitNode::unitOrganization, organizationId),
            equalsUnaccentedLower(UnitNode::unitName, unitName),
        )
    ).isHigher(0)

    override suspend fun hasAnotherWithDocument(
        id: UUID,
        organizationId: UUID,
        documentNumber: String
    ): Boolean = sessionFactory.count<UnitNode>(
        listOf(
            notEquals(UnitNode::id, id),
            equals(OrganizationNode::id, UnitNode::unitOrganization, organizationId),
            equalsUnaccentedLower(UnitNode::documentNumber, documentNumber),
        )
    ).isHigher(0)

    override suspend fun allByIds(
        ids: Set<UUID>,
        organizationId: UUID
    ): Set<UnitEntity> = sessionFactory.openSession()
        .loadAll(
            UnitNode::class.java,
            inValues(UnitNode::id, ids).and(equals(OrganizationNode::id, UnitNode::unitOrganization, organizationId))
        ).map { it.toUnitEntity() }.toSet()

    private suspend fun save(
        entity: UnitEntity
    ): UnitEntity = transaction { session ->
        entity.apply {
            session.save(this.toUnitNode())
        }
    }

}