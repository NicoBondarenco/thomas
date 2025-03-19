package com.thomas.management.data.neo4j.repository

import com.thomas.core.extension.isHigher
import com.thomas.core.extension.unaccentedLower
import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.database.neo4j.filter.count
import com.thomas.database.neo4j.filter.equalsUnaccentedLower
import com.thomas.database.neo4j.filter.greaterThanEquals
import com.thomas.database.neo4j.filter.isEquals
import com.thomas.database.neo4j.filter.isNotEquals
import com.thomas.database.neo4j.filter.isTrue
import com.thomas.database.neo4j.filter.lessThanEquals
import com.thomas.database.neo4j.filter.likeUnaccentedLower
import com.thomas.database.neo4j.filter.or
import com.thomas.database.neo4j.filter.page
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.data.entity.UserSimpleEntity
import com.thomas.management.data.neo4j.model.mapper.toUserCompleteEntity
import com.thomas.management.data.neo4j.model.mapper.toUserNode
import com.thomas.management.data.neo4j.model.mapper.toUserSimpleEntity
import com.thomas.management.data.neo4j.model.mapper.updateFrom
import com.thomas.management.data.neo4j.model.node.UserGroupNode
import com.thomas.management.data.neo4j.model.node.UserNode
import com.thomas.management.data.neo4j.model.node.UserOrganizationNode
import com.thomas.management.data.neo4j.model.node.UserUnitNode
import com.thomas.management.data.repository.UserRepository
import java.util.UUID
import org.neo4j.ogm.cypher.Filters
import org.neo4j.ogm.session.SessionFactory

class UserNeo4JRepository(
    sessionFactory: SessionFactory
) : ManagementNeo4JRepository(sessionFactory), UserRepository {

    override suspend fun page(
        keywordText: String?,
        isActive: Boolean?,
        organizationId: UUID,
        pageable: PageRequestPeriod
    ): PageResponse<UserSimpleEntity> = sessionFactory.page<UserNode>(
        listOfNotNull(
            isEquals(UserOrganizationNode::organizationId, UserNode::userOrganization, organizationId),
            keywordText?.let {
                or(
                    likeUnaccentedLower(UserNode::firstName, it.unaccentedLower()),
                    likeUnaccentedLower(UserNode::lastName, it.unaccentedLower()),
                    likeUnaccentedLower(UserNode::mainEmail, it.unaccentedLower()),
                    likeUnaccentedLower(UserNode::documentNumber, it.unaccentedLower()),
                )
            },
            isActive?.let {
                isTrue(UserNode::isActive)
            },
            pageable.createdStart?.let {
                greaterThanEquals(UserNode::createdAt, it.toZonedDateTime())
            },
            pageable.createdEnd?.let {
                lessThanEquals(UserNode::createdAt, it.toZonedDateTime())
            },
            pageable.updatedStart?.let {
                greaterThanEquals(UserNode::updatedAt, it.toZonedDateTime())
            },
            pageable.updatedEnd?.let {
                lessThanEquals(UserNode::updatedAt, it.toZonedDateTime())
            },
        ),
        pageable,
        defaultDepth
    ).map {
        it.toUserSimpleEntity()
    }

    override suspend fun one(
        id: UUID,
        organizationId: UUID
    ): UserCompleteEntity? = sessionFactory.openSession().loadAll(
        UserNode::class.java,
        isEquals(UserNode::id, id).and(isEquals(UserOrganizationNode::organizationId, UserNode::userOrganization, organizationId)),
        defaultDepth
    ).firstOrNull()?.toUserCompleteEntity()

    override suspend fun create(
        entity: UserCompleteEntity
    ): UserCompleteEntity = transaction { session ->
        entity.apply {
            session.save(this.toUserNode())
        }
    }

    override suspend fun update(
        entity: UserCompleteEntity
    ): UserCompleteEntity = transaction { session ->
        entity.apply {
            session.delete(UserUnitNode::class.java, Filters(isEquals(UserUnitNode::userId, entity.id)), false)
            session.delete(UserGroupNode::class.java, Filters(isEquals(UserGroupNode::userId, entity.id)), false)
            session.save(this.toUserNode())
        }
    }

    override suspend fun updateSimple(
        entity: UserSimpleEntity
    ): UserSimpleEntity = transaction { session ->
        entity.apply {
            session.load(UserNode::class.java, entity.id, defaultDepth)?.let { node ->
                node.updateFrom(this@apply)
                session.save(node)
            }
        }
    }

    override suspend fun simpleByEmail(
        email: String
    ): UserSimpleEntity? = sessionFactory.openSession()
        .loadAll(UserNode::class.java, isEquals(UserNode::mainEmail, email))
        .firstOrNull()
        ?.toUserSimpleEntity()

    override suspend fun hasAnotherWithDocument(
        id: UUID,
        organizationId: UUID,
        documentNumber: String
    ): Boolean = sessionFactory.count<UserNode>(
        listOf(
            isNotEquals(UserNode::id, id),
            isEquals(UserOrganizationNode::organizationId, UserNode::userOrganization, organizationId),
            equalsUnaccentedLower(UserNode::documentNumber, documentNumber),
        )
    ).isHigher(0)

    override suspend fun hasAnotherWithEmail(
        id: UUID,
        mainEmail: String
    ): Boolean = sessionFactory.count<UserNode>(
        listOf(
            isNotEquals(UserNode::id, id),
            equalsUnaccentedLower(UserNode::mainEmail, mainEmail),
        )
    ).isHigher(0)

    override suspend fun limitReached(
        id: UUID,
        organizationId: UUID
    ): Boolean = sessionFactory.openSession().let {
        val result = it.query(
            """
                MATCH(u: User) WHERE NOT(u.id = ${"$"}`user_id`)
                MATCH(o: Organization) WHERE o.id = ${"$"}`organization_id`
                MATCH(u)-[:`USER_BELONGS_TO_ORGANIZATION`]->(o)
                WITH o.maximum_units as max_units, COUNT(u) as total_units
                RETURN (max_units - total_units) as available
            """.trimIndent(),
            mapOf(
                "user_id" to id.toString(),
                "organization_id" to organizationId.toString(),
            )
        )
        val available: Long = (result.queryResults().firstOrNull()?.get("available") ?: 0L) as Long
        available < 1L
    }

    override suspend fun byId(
        id: UUID
    ): UserSimpleEntity? = sessionFactory.openSession().loadAll(
        UserNode::class.java,
        isEquals(UserNode::id, id),
        defaultDepth
    ).firstOrNull()?.toUserSimpleEntity()

    override suspend fun findByUsername(
        username: String
    ): UserCompleteEntity? = sessionFactory.openSession().loadAll(
        UserNode::class.java,
        isEquals(UserNode::mainEmail, username),
        defaultDepth
    ).firstOrNull()?.toUserCompleteEntity()

}
