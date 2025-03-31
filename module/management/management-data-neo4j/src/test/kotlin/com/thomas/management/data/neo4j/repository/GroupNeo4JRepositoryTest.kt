package com.thomas.management.data.neo4j.repository

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection.ASC
import com.thomas.core.model.pagination.PageSortDirection.DESC
import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.core.util.NumberUtils.randomInteger
import com.thomas.core.util.StringUtils.randomString
import com.thomas.database.neo4j.filter.isEquals
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.UnitEntity
import com.thomas.management.data.entity.UnitRoleEntity
import com.thomas.management.data.entity.groupCompleteEntity
import com.thomas.management.data.neo4j.model.mapper.toGroupCompleteEntity
import com.thomas.management.data.neo4j.model.mapper.toGroupNode
import com.thomas.management.data.neo4j.model.mapper.toOrganizationEntity
import com.thomas.management.data.neo4j.model.mapper.toUnitEntity
import com.thomas.management.data.neo4j.model.node.GroupNode
import com.thomas.management.data.neo4j.model.node.GroupOrganizationNode
import com.thomas.management.data.neo4j.model.node.GroupUnitNode
import com.thomas.management.data.neo4j.model.node.OrganizationNode
import com.thomas.management.data.neo4j.model.node.UnitNode
import com.thomas.management.data.neo4j.util.EntityFindOneData
import com.thomas.management.data.neo4j.util.EntitySameData
import com.thomas.management.data.neo4j.util.GroupSearchData
import com.thomas.management.data.neo4j.util.toGroupSimpleEntity
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
import java.util.UUID.randomUUID
import org.neo4j.ogm.cypher.Filters
import org.neo4j.ogm.session.SessionFactory


class GroupNeo4JRepositoryTest : ManagementFunSpec<GroupNeo4JRepository>(
    body = {
        initNodes(
            mapOf(
                GroupNode::class to { (it as GroupNode).toGroupCompleteEntity() },
                UnitNode::class to { (it as UnitNode).toUnitEntity() },
                OrganizationNode::class to { (it as OrganizationNode).toOrganizationEntity() },
            )
        )

//        setNodeSearch(GroupNode::class) { sessionFactory ->
//            val result = sessionFactory.openSession().query(
//                GroupNode::class.java,
//                """
//                    MATCH (g:`Group`)-[gbto:`GROUP_BELONGS_TO_ORGANIZATION`]->(go:`Organization`)
//                    WITH g, gbto, go
//                    RETURN g, gbto, go, [ (g)-[gaiu:`GROUP_ALLOWED_IN_UNIT`]->(u:`Unit`) | [ gaiu, u, [ [ (u)-[ubto:`UNIT_BELONGS_TO_ORGANIZATION`]->(uo:`Organization`) | [ ubto, uo ] ] ] ] ];
//                """.trimIndent(),
//                mapOf<String, Any>()
//            ).toList()
//            result
//        }

        context(name = "One", script = "/scripts/group/page.cypher") {
            val groups = entities(GroupCompleteEntity::class)
            val organizations = entities(OrganizationEntity::class)

            val data = mutableListOf<EntityFindOneData<GroupCompleteEntity>>()

            organizations.forEach { organization ->
                groups.filter {
                    it.groupOrganization.id == organization.id
                }.take(5).forEach { group ->
                    data += EntityFindOneData(group.id, organization.id, group)
                }
                groups.filter {
                    it.groupOrganization.id != organization.id
                }.take(5).forEach { group ->
                    data += EntityFindOneData(group.id, organization.id, null)
                }
            }
            (1..5).forEach { _ ->
                data += EntityFindOneData(randomUUID(), randomUUID(), null)
            }

            withData(data) {
                val result = repository.one(it.id, it.organizationId)
                result shouldBe it.entity
            }
        }

        context(name = "Page", script = "/scripts/group/page.cypher") {
            val groups = entities(GroupCompleteEntity::class)
            val organizations = entities(OrganizationEntity::class)

            val data = listOf(
                GroupSearchData("gest", null, organizations.random().id, PageRequestPeriod(pageNumber = 3, pageSize = 4, pageSort = listOf(PageSort("updated_at", DESC))), compareByDescending { it.updatedAt }),
                GroupSearchData(null, true, organizations.random().id, PageRequestPeriod(pageNumber = 5, pageSize = 5, pageSort = listOf(PageSort("created_at", DESC), PageSort("group_name", ASC))), compareByDescending<GroupCompleteEntity> { it.createdAt }.thenBy { it.groupName }),
                GroupSearchData(null, null, organizations.random().id, PageRequestPeriod(pageNumber = 5, pageSize = 5, pageSort = listOf(PageSort("group_description", ASC))), compareBy { it.groupDescription }),
                GroupSearchData("ÇÃO", true, organizations.random().id, PageRequestPeriod(pageNumber = 2, pageSize = 3, pageSort = listOf(PageSort("updated_at", ASC))), compareBy { it.updatedAt }),
                GroupSearchData(null, null, organizations.random().id, PageRequestPeriod(createdStart = OffsetDateTime.parse("2025-01-01T00:00:00.000000Z", ISO_OFFSET_DATE_TIME), createdEnd = OffsetDateTime.parse("2025-06-30T23:59:59.999999Z", ISO_OFFSET_DATE_TIME), pageSort = listOf(PageSort("updated_at", ASC))), compareBy { it.updatedAt }),
                GroupSearchData(null, null, organizations.random().id, PageRequestPeriod(updatedStart = OffsetDateTime.parse("2025-07-01T00:00:00.000000Z", ISO_OFFSET_DATE_TIME), updatedEnd = OffsetDateTime.parse("2025-12-31T23:59:59.999999Z", ISO_OFFSET_DATE_TIME), pageSort = listOf(PageSort("group_name", DESC))), compareByDescending { it.groupName }),
            )

            withData(data) {
                val page = it.page(groups.toList()).map { g -> g.toGroupSimpleEntity() }
                val result = repository.page(it.keyword, it.isActive, it.organizationId, it.pageable)
                result.contentList.size shouldBe page.contentList.size
                result.totalItems shouldBe page.totalItems
                result.totalPages shouldBe page.totalPages
                page.contentList.forEach { entity ->
                    result.contentList.contains(entity) shouldBe true
                }
            }
        }

        context(name = "Save", script = "/scripts/group/page.cypher") {

            val data: Map<String, GroupCompleteEntity> = (1..10).map {
                val organization = entities(OrganizationEntity::class).random()
                val units = entities(UnitEntity::class).filter {
                    it.unitOrganization.id == organization.id
                }
                groupCompleteEntity.copy(
                    groupOrganization = organization,
                    organizationRoles = SecurityOrganizationRole.entries.shuffled().take(randomInteger(0, SecurityOrganizationRole.entries.size)).toSet(),
                    groupUnits = units.take(randomInteger(0, units.size)).map {
                        UnitRoleEntity(
                            roleUnit = it,
                            roleList = SecurityUnitRole.entries.shuffled().take(randomInteger(0, SecurityUnitRole.entries.size)).toSet()
                        )
                    }.toSet()
                )
            }.associateBy { it.id.toString() }

            withData(data) { group ->
                repository.create(group)

                val node = group.toGroupNode()
                val result = sessionFactory.openSession().load(GroupNode::class.java, group.id, 5)

                result.id shouldBe node.id
                result.groupName shouldBe node.groupName
                result.groupDescription shouldBe node.groupDescription
                result.isActive shouldBe node.isActive
                result.createdAt shouldBe node.createdAt
                result.updatedAt shouldBe node.updatedAt

                result.groupOrganization shouldBe node.groupOrganization

                (result.groupUnits?.size ?: 0) shouldBe (node.groupUnits?.size ?: 0)

                node.groupUnits?.forEach { groupUnit ->
                    result.groupUnits!!.contains(groupUnit) shouldBe true
                }
            }

        }

        context(name = "Update", script = "/scripts/group/page.cypher") {

            val data: Map<String, GroupCompleteEntity> = entities(GroupCompleteEntity::class).shuffled().take(10).map { existent ->
                val organization = existent.groupOrganization
                val units = entities(UnitEntity::class).filter {
                    it.unitOrganization.id == organization.id
                }
                groupCompleteEntity.copy(
                    id = existent.id,
                    groupOrganization = organization,
                    organizationRoles = SecurityOrganizationRole.entries.shuffled().take(randomInteger(0, SecurityOrganizationRole.entries.size)).toSet(),
                    groupUnits = units.take(randomInteger(0, units.size)).map {
                        UnitRoleEntity(
                            roleUnit = it,
                            roleList = SecurityUnitRole.entries.shuffled().take(randomInteger(0, SecurityUnitRole.entries.size)).toSet()
                        )
                    }.toSet()
                )
            }.associateBy { it.id.toString() }

            withData(data) { group ->
                repository.update(group)

                val result = sessionFactory.openSession().load(GroupNode::class.java, group.id, 5).toGroupCompleteEntity()

                result.id shouldBe group.id
                result.groupName shouldBe group.groupName
                result.groupDescription shouldBe group.groupDescription
                result.isActive shouldBe group.isActive
                result.createdAt shouldBe group.createdAt
                result.updatedAt shouldBe group.updatedAt

                result.groupOrganization shouldBe group.groupOrganization

                result.groupUnits.size shouldBe (group.groupUnits.size)

                group.groupUnits.forEach { groupUnit ->
                    result.groupUnits.contains(groupUnit) shouldBe true
                }
            }

        }

        context(name = "Delete", script = "/scripts/group/page.cypher") {
            val groupId = sessionFactory.openSession().loadAll(GroupUnitNode::class.java).random().groupId

            var resultUnits = sessionFactory.openSession().loadAll(GroupOrganizationNode::class.java, Filters(isEquals(GroupOrganizationNode::groupId, groupId)))
            var resultOrganization = sessionFactory.openSession().loadAll(GroupUnitNode::class.java, Filters(isEquals(GroupUnitNode::groupId, groupId)))
            var resultGroup = sessionFactory.openSession().loadAll(GroupNode::class.java, Filters(isEquals(GroupNode::id, groupId)))

            resultGroup.isEmpty() shouldBe false
            resultUnits.isEmpty() shouldBe false
            resultOrganization.isEmpty() shouldBe false

            repository.delete(groupId)

            resultUnits = sessionFactory.openSession().loadAll(GroupOrganizationNode::class.java, Filters(isEquals(GroupOrganizationNode::groupId, groupId)))
            resultOrganization = sessionFactory.openSession().loadAll(GroupUnitNode::class.java, Filters(isEquals(GroupUnitNode::groupId, groupId)))
            resultGroup = sessionFactory.openSession().loadAll(GroupNode::class.java, Filters(isEquals(GroupNode::id, groupId)))

            resultGroup.isEmpty() shouldBe true
            resultUnits.isEmpty() shouldBe true
            resultOrganization.isEmpty() shouldBe true
        }

        context(name = "All by ID", script = "/scripts/group/page.cypher") {
            val organization = entities(OrganizationEntity::class).random()
            val groups = entities(GroupCompleteEntity::class).map {
                it.toGroupSimpleEntity()
            }.filter {
                it.groupOrganization.id == organization.id
            }.shuffled().take(10)
            val result = repository.allByIds(groups.map { it.id }.toSet(), organization.id)
            groups.forEach { group ->
                result.contains(group) shouldBe true
            }
        }

        context(name = "All by ID Full", script = "/scripts/group/page.cypher") {
            val organization = entities(OrganizationEntity::class).random()
            val groups = entities(GroupCompleteEntity::class).filter {
                it.groupOrganization.id == organization.id
            }.shuffled().take(10)
            val result = repository.allFullByIds(groups.map { it.id }.toSet(), organization.id)
            groups.forEach { group ->
                result.contains(group) shouldBe true
            }
        }

        context(name = "Exists same", script = "/scripts/group/page.cypher") {
            val group = entities(GroupCompleteEntity::class).random()
            val organization = group.groupOrganization
            val data = mapOf(
                "Group same name same organization" to EntitySameData(true, action = { repository.hasAnotherWithName(randomUUID(), organization.id, group.groupName) }),
                "Group same name new unit" to EntitySameData(false, action = { repository.hasAnotherWithName(randomUUID(), organization.id, randomString()) }),
                "Group same name another organization" to EntitySameData(false, action = { repository.hasAnotherWithName(randomUUID(), randomUUID(), group.groupName) }),
                "Group same name same unit" to EntitySameData(false, action = { repository.hasAnotherWithName(group.id, organization.id, group.groupName) }),
            )
            withData(data) {
                it.action() shouldBe it.result
            }
        }

    }
) {

    override fun createRepository(
        sessionFactory: SessionFactory
    ): GroupNeo4JRepository = GroupNeo4JRepository(sessionFactory)

}