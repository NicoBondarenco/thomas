package com.thomas.management.data.neo4j.repository

import com.thomas.core.extension.isHigher
import com.thomas.core.extension.unaccentedLower
import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageResponse
import com.thomas.database.neo4j.filter.count
import com.thomas.database.neo4j.filter.isEquals
import com.thomas.database.neo4j.filter.equalsUnaccentedLower
import com.thomas.database.neo4j.filter.greaterThanEquals
import com.thomas.database.neo4j.filter.inValues
import com.thomas.database.neo4j.filter.isNull
import com.thomas.database.neo4j.filter.isTrue
import com.thomas.database.neo4j.filter.lessThanEquals
import com.thomas.database.neo4j.filter.likeUnaccentedLower
import com.thomas.database.neo4j.filter.isNotEquals
import com.thomas.database.neo4j.filter.or
import com.thomas.database.neo4j.filter.page
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.GroupSimpleEntity
import com.thomas.management.data.neo4j.model.mapper.toGroupCompleteEntity
import com.thomas.management.data.neo4j.model.mapper.toGroupSimpleEntity
import com.thomas.management.data.neo4j.model.mapper.toGroupNode
import com.thomas.management.data.neo4j.model.node.GroupNode
import com.thomas.management.data.neo4j.model.node.GroupOrganizationNode
import com.thomas.management.data.neo4j.model.node.GroupUnitNode
import com.thomas.management.data.repository.GroupRepository
import java.util.UUID
import org.neo4j.ogm.cypher.Filters
import org.neo4j.ogm.session.SessionFactory

class GroupNeo4JRepository(
    sessionFactory: SessionFactory
) : ManagementNeo4JRepository(sessionFactory), GroupRepository {

    override suspend fun page(
        keywordText: String?,
        isActive: Boolean?,
        organizationId: UUID,
        pageable: PageRequestPeriod
    ): PageResponse<GroupSimpleEntity> = sessionFactory.page<GroupNode>(
        listOfNotNull(
            isEquals(GroupOrganizationNode::organizationId, GroupNode::groupOrganization, organizationId),
            keywordText?.let {
                or(
                    likeUnaccentedLower(GroupNode::groupName, it.unaccentedLower()),
                    or(likeUnaccentedLower(GroupNode::groupDescription, it.unaccentedLower()), isNull(GroupNode::groupDescription)),
                )
            },
            isActive?.let {
                isTrue(GroupNode::isActive)
            },
            pageable.createdStart?.let {
                greaterThanEquals(GroupNode::createdAt, it.toZonedDateTime())
            },
            pageable.createdEnd?.let {
                lessThanEquals(GroupNode::createdAt, it.toZonedDateTime())
            },
            pageable.updatedStart?.let {
                greaterThanEquals(GroupNode::updatedAt, it.toZonedDateTime())
            },
            pageable.updatedEnd?.let {
                lessThanEquals(GroupNode::updatedAt, it.toZonedDateTime())
            },
        ),
        pageable,
        defaultDepth
    ).map {
        it.toGroupSimpleEntity()
    }

    override suspend fun one(
        id: UUID,
        organizationId: UUID
    ): GroupCompleteEntity? = sessionFactory.openSession().loadAll(
        GroupNode::class.java,
        isEquals(GroupNode::id, id).and(isEquals(GroupOrganizationNode::organizationId, GroupNode::groupOrganization, organizationId)),
        defaultDepth
    ).firstOrNull()?.toGroupCompleteEntity()

    override suspend fun create(
        entity: GroupCompleteEntity
    ): GroupCompleteEntity = transaction { session ->
        entity.apply {
            session.save(this.toGroupNode())
        }
    }

    override suspend fun update(
        entity: GroupCompleteEntity
    ): GroupCompleteEntity = transaction { session ->
        entity.apply {
            session.delete(GroupUnitNode::class.java, Filters(isEquals(GroupUnitNode::groupId, entity.id)), false)
            session.save(this.toGroupNode())
        }
    }

    override suspend fun delete(
        id: UUID
    ): Unit = transaction { session ->
        session.delete(GroupOrganizationNode::class.java, Filters(isEquals(GroupOrganizationNode::groupId, id)), false)
        session.delete(GroupUnitNode::class.java, Filters(isEquals(GroupUnitNode::groupId, id)), false)
        session.delete(GroupNode::class.java, Filters(isEquals(GroupNode::id, id)), false)
    }

    override suspend fun allByIds(
        ids: Set<UUID>,
        organizationId: UUID
    ): Set<GroupSimpleEntity> = sessionFactory.openSession().loadAll(
        GroupNode::class.java,
        inValues(GroupNode::id, ids).and(isEquals(GroupOrganizationNode::organizationId, GroupNode::groupOrganization, organizationId)),
        defaultDepth
    ).map { it.toGroupSimpleEntity() }.toSet()

    override suspend fun allFullByIds(
        ids: Set<UUID>,
        organizationId: UUID
    ): Set<GroupCompleteEntity> = sessionFactory.openSession().loadAll(
        GroupNode::class.java,
        inValues(GroupNode::id, ids).and(isEquals(GroupOrganizationNode::organizationId, GroupNode::groupOrganization, organizationId)),
        defaultDepth
    ).map { it.toGroupCompleteEntity() }.toSet()

    override suspend fun hasAnotherWithName(
        id: UUID,
        organizationId: UUID,
        groupName: String
    ): Boolean = sessionFactory.count<GroupNode>(
        listOf(
            isNotEquals(GroupNode::id, id),
            isEquals(GroupOrganizationNode::organizationId, GroupNode::groupOrganization, organizationId),
            equalsUnaccentedLower(GroupNode::groupName, groupName),
        )
    ).isHigher(0)

}