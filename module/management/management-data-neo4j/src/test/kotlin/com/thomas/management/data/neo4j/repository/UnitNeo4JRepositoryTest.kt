package com.thomas.management.data.neo4j.repository

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection.ASC
import com.thomas.core.model.pagination.PageSortDirection.DESC
import com.thomas.core.util.StringUtils.randomString
import com.thomas.database.neo4j.filter.isEquals
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.UnitEntity
import com.thomas.management.data.entity.unitEntity
import com.thomas.management.data.neo4j.model.mapper.toOrganizationEntity
import com.thomas.management.data.neo4j.model.mapper.toUnitEntity
import com.thomas.management.data.neo4j.model.mapper.toUnitNode
import com.thomas.management.data.neo4j.model.node.GroupUnitNode
import com.thomas.management.data.neo4j.model.node.OrganizationNode
import com.thomas.management.data.neo4j.model.node.UnitNode
import com.thomas.management.data.neo4j.util.EntityFindOneData
import com.thomas.management.data.neo4j.util.EntitySameData
import com.thomas.management.data.neo4j.util.UnitSearchData
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
import java.util.UUID
import java.util.UUID.randomUUID
import org.neo4j.ogm.session.SessionFactory

class UnitNeo4JRepositoryTest : ManagementFunSpec<UnitNeo4JRepository>(
    body = {

        initNodes(
            mapOf(
                UnitNode::class to { (it as UnitNode).toUnitEntity() },
                OrganizationNode::class to { (it as OrganizationNode).toOrganizationEntity() },
            )
        )

        context(name = "One", script = "/scripts/unit/one.cypher") {
            val units = entities(UnitEntity::class)
            val data = units.shuffled().take(5).map { node ->
                EntityFindOneData<UnitEntity>(node.id, node.unitOrganization.id, node)
            } + (1..5).map {
                EntityFindOneData<UnitEntity>(randomUUID(), randomUUID(), null)
            } + units.shuffled().take(5).map { node ->
                EntityFindOneData<UnitEntity>(node.id, entities(OrganizationEntity::class).filter { o ->
                    o.id != node.unitOrganization.id
                }.random().id, null)
            }
            withData(data) {
                val result = repository.one(it.id, it.organizationId)
                result shouldBe it.entity
            }
        }

        context(name = "Page", script = "/scripts/unit/page.cypher") {
            val organizations = entities(OrganizationEntity::class)
            val units = entities(UnitEntity::class)
            val data = listOf(
                UnitSearchData("tóri", null, organizations.random().id, PageRequestPeriod(pageNumber = 1, pageSize = 15, pageSort = listOf(PageSort("updated_at", DESC))), compareByDescending { it.updatedAt }),
                UnitSearchData(null, true, organizations.random().id, PageRequestPeriod(pageNumber = 2, pageSize = 5, pageSort = listOf(PageSort("created_at", ASC))), compareBy { it.createdAt }),
                UnitSearchData(null, null, organizations.random().id, PageRequestPeriod(createdStart = OffsetDateTime.parse("2025-04-01T00:00:00.000000Z", ISO_OFFSET_DATE_TIME), pageNumber = 3, pageSize = 7, pageSort = listOf(PageSort("unit_name", ASC))), compareBy { it.unitName }),
                UnitSearchData(null, null, organizations.random().id, PageRequestPeriod(updatedStart = OffsetDateTime.parse("2025-04-01T00:00:00.000000Z", ISO_OFFSET_DATE_TIME), updatedEnd = OffsetDateTime.parse("2025-10-31T23:59:59.999999Z", ISO_OFFSET_DATE_TIME), pageNumber = 2, pageSize = 4, pageSort = listOf(PageSort("fantasy_name", DESC))), compareByDescending { it.fantasyName }),
                UnitSearchData(null, true, organizations.random().id, PageRequestPeriod(updatedStart = OffsetDateTime.parse("2025-03-01T00:00:00.000000Z", ISO_OFFSET_DATE_TIME), updatedEnd = OffsetDateTime.parse("2025-07-31T23:59:59.999999Z", ISO_OFFSET_DATE_TIME), pageNumber = 1, pageSize = 3, pageSort = listOf(PageSort("created_at", DESC), PageSort("registration_number", ASC))), compareByDescending<UnitEntity> { it.createdAt }.thenBy { it.documentNumber }),
            )
            withData(data) {
                val page = it.page(units.toList())
                val result = repository.page(it.organizationId!!, it.keyword, it.isActive, it.pageable)
                result.contentList.size shouldBe page.contentList.size
                result.totalItems shouldBe page.totalItems
                result.totalPages shouldBe page.totalPages
                page.contentList.forEach { entity ->
                    result.contentList.contains(entity) shouldBe true
                }
            }
        }

        context(name = "Save", script = "/scripts/unit/upsert.cypher") {
            val organization = entities(OrganizationEntity::class).random()
            val unit = unitEntity.copy(unitOrganization = organization)
            repository.create(unit)
            val result = sessionFactory.openSession().load(UnitNode::class.java, unit.id)
            result shouldBe unit.toUnitNode()
        }

        context(name = "Update", script = "/scripts/unit/upsert.cypher") {
            val existent = entities(UnitEntity::class).random()
            val unit = unitEntity.copy(id = existent.id, unitOrganization = existent.unitOrganization)
            repository.update(unit)
            val result = sessionFactory.openSession().load(UnitNode::class.java, existent.id)
            result shouldBe unit.toUnitNode()
        }

        context(name = "Delete", script = "/scripts/unit/upsert.cypher") {
            val unitId: UUID = sessionFactory.openSession().loadAll(GroupUnitNode::class.java).random().unitId
            repository.delete(unitId)
            val result = sessionFactory.openSession().load(UnitNode::class.java, unitId)
            result shouldBe null
            val relations = sessionFactory.openSession().loadAll(GroupUnitNode::class.java, isEquals(GroupUnitNode::unitId, unitId))
            relations.isEmpty() shouldBe true
        }

        context(name = "All by ID", script = "/scripts/unit/page.cypher") {
            val organization = entities(OrganizationEntity::class).random()
            val units = entities(UnitEntity::class).filter { it.unitOrganization.id == organization.id }.shuffled().take(10)
            val result = repository.allByIds(units.map { it.id }.toSet(), organization.id)
            units.forEach { unit ->
                result.contains(unit) shouldBe true
            }
        }

        context(name = "Exists same", script = "/scripts/unit/upsert.cypher") {
            val unit = entities(UnitEntity::class).random()
            val organization = unit.unitOrganization
            val data = mapOf(
                "Unit same name same organization" to EntitySameData(true, action = { repository.hasAnotherWithName(randomUUID(), organization.id, unit.unitName) }),
                "Unit same name new unit" to EntitySameData(false, action = { repository.hasAnotherWithName(randomUUID(), organization.id, randomString()) }),
                "Unit same name another organization" to EntitySameData(false, action = { repository.hasAnotherWithName(randomUUID(), randomUUID(), unit.unitName) }),
                "Unit same name same unit" to EntitySameData(false, action = { repository.hasAnotherWithName(unit.id, organization.id, unit.unitName) }),
                "Unit same document same organization" to EntitySameData(true, action = { repository.hasAnotherWithDocument(randomUUID(), organization.id, unit.documentNumber) }),
                "Unit same document new unit" to EntitySameData(false, action = { repository.hasAnotherWithDocument(randomUUID(), organization.id, randomString()) }),
                "Unit same document another organization" to EntitySameData(false, action = { repository.hasAnotherWithDocument(randomUUID(), randomUUID(), unit.documentNumber) }),
                "Unit same document same unit" to EntitySameData(false, action = { repository.hasAnotherWithDocument(unit.id, organization.id, unit.documentNumber) }),
            )
            withData(data) {
                it.action() shouldBe it.result
            }
        }

        context(name = "Limit", script = "/scripts/unit/limit.cypher") {
            val reached = UUID.fromString("69743b95-5c49-4286-adb4-f02c10143de4")
            val available = UUID.fromString("744a0082-037a-4722-8730-60da67d2eea4")
            val unitReached = entities(UnitEntity::class).filter { it.unitOrganization.id == reached }.random()
            val unitAvailable = entities(UnitEntity::class).filter { it.unitOrganization.id == reached }.random()

            repository.limitReached(randomUUID(), reached) shouldBe true
            repository.limitReached(unitReached.id, reached) shouldBe false
            repository.limitReached(randomUUID(), available) shouldBe false
            repository.limitReached(unitAvailable.id, available) shouldBe false
            repository.limitReached(randomUUID(), randomUUID()) shouldBe true
        }

    }
) {

    override fun createRepository(
        sessionFactory: SessionFactory
    ): UnitNeo4JRepository = UnitNeo4JRepository(sessionFactory)

}
