package com.thomas.management.data.neo4j.repository

import com.thomas.core.extension.isHigher
import com.thomas.core.extension.unaccentedLower
import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.database.neo4j.filter.count
import com.thomas.database.neo4j.filter.equalsUnaccentedLower
import com.thomas.database.neo4j.filter.greaterThanEquals
import com.thomas.database.neo4j.filter.isNotEquals
import com.thomas.database.neo4j.filter.isTrue
import com.thomas.database.neo4j.filter.lessThanEquals
import com.thomas.database.neo4j.filter.likeUnaccentedLower
import com.thomas.database.neo4j.filter.or
import com.thomas.database.neo4j.filter.page
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.neo4j.model.mapper.toOrganizationEntity
import com.thomas.management.data.neo4j.model.mapper.toOrganizationNode
import com.thomas.management.data.neo4j.model.node.OrganizationNode
import com.thomas.management.data.repository.OrganizationRepository
import java.util.UUID
import org.neo4j.ogm.session.SessionFactory

class OrganizationNeo4JRepository(
    sessionFactory: SessionFactory
) : ManagementNeo4JRepository(sessionFactory), OrganizationRepository {

    override suspend fun hasAnotherWithName(
        id: UUID,
        organizationName: String
    ): Boolean = sessionFactory.count<OrganizationNode>(
        listOf(
            isNotEquals(OrganizationNode::id, id),
            equalsUnaccentedLower(OrganizationNode::organizationName, organizationName),
        )
    ).isHigher(0)

    override suspend fun hasAnotherWithRegistration(
        id: UUID,
        registrationNumber: String
    ): Boolean = sessionFactory.count<OrganizationNode>(
        listOf(
            isNotEquals(OrganizationNode::id, id),
            equalsUnaccentedLower(OrganizationNode::registrationNumber, registrationNumber),
        )
    ).isHigher(0)

    override suspend fun one(
        id: UUID
    ): OrganizationEntity? = sessionFactory
        .openSession()
        .load(OrganizationNode::class.java, id)
        ?.toOrganizationEntity()

    override suspend fun page(
        keywordText: String?,
        isActive: Boolean?,
        pageable: PageRequestPeriod
    ): PageResponse<OrganizationEntity> = sessionFactory.page<OrganizationNode>(
        listOfNotNull(
            keywordText?.let {
                or(
                    likeUnaccentedLower(OrganizationNode::organizationName, it.unaccentedLower()),
                    likeUnaccentedLower(OrganizationNode::fantasyName, it.unaccentedLower()),
                    likeUnaccentedLower(OrganizationNode::registrationNumber, it),
                    likeUnaccentedLower(OrganizationNode::mainEmail, it.unaccentedLower()),
                )
            },
            isActive?.let {
                isTrue(OrganizationNode::isActive)
            },
            pageable.createdStart?.let {
                greaterThanEquals(OrganizationNode::createdAt, it.toZonedDateTime())
            },
            pageable.createdEnd?.let {
                lessThanEquals(OrganizationNode::createdAt, it.toZonedDateTime())
            },
            pageable.updatedStart?.let {
                greaterThanEquals(OrganizationNode::updatedAt, it.toZonedDateTime())
            },
            pageable.updatedEnd?.let {
                lessThanEquals(OrganizationNode::updatedAt, it.toZonedDateTime())
            },
        ),
        pageable
    ).map {
        it.toOrganizationEntity()
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
