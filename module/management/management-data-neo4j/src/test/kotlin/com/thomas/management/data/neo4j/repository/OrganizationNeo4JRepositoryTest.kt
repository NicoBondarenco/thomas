package com.thomas.management.data.neo4j.repository

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection.ASC
import com.thomas.core.model.pagination.PageSortDirection.DESC
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.organizationEntity
import com.thomas.management.data.neo4j.model.mapper.toOrganizationEntity
import com.thomas.management.data.neo4j.model.mapper.toOrganizationNode
import com.thomas.management.data.neo4j.model.node.OrganizationNode
import com.thomas.management.data.neo4j.util.OrganizationSameData
import com.thomas.management.data.neo4j.util.OrganizationSearchData
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
import java.util.UUID.randomUUID
import org.neo4j.ogm.session.SessionFactory

class OrganizationNeo4JRepositoryTest : ManagementFunSpec<OrganizationNeo4JRepository>(
    body = {
        initNodes(
            mapOf(OrganizationNode::class to { (it as OrganizationNode).toOrganizationEntity() })
        )

        context(name = "Save") {
            val organization = organizationEntity
            repository.create(organization)
            val result = sessionFactory.openSession().load(OrganizationNode::class.java, organization.id)
            result shouldBe organization.toOrganizationNode()
        }

        context(name = "Update", script = "/scripts/organization/update.cypher") {
            val id = entities(OrganizationEntity::class).random().id
            val organization = organizationEntity.copy(id = id)
            repository.update(organization)
            val result = sessionFactory.openSession().load(OrganizationNode::class.java, id)
            result shouldBe organization.toOrganizationNode()
        }

        context(name = "One", script = "/scripts/organization/one.cypher") {
            val organization = entities(OrganizationEntity::class).random()
            val result = repository.one(organization.id)
            result shouldBe organization
        }

        context(name = "Exists same", script = "/scripts/organization/one-with.cypher") {
            val organization = entities(OrganizationEntity::class).random()
            val data = mapOf(
                "Organization same name true" to OrganizationSameData(true, action = { repository.hasAnotherWithName(randomUUID(), organization.organizationName) }),
                "Organization same name false" to OrganizationSameData(false, action = { repository.hasAnotherWithName(organization.id, organization.organizationName) }),
                "Organization same registration true" to OrganizationSameData(true, action = { repository.hasAnotherWithRegistration(randomUUID(), organization.registrationNumber) }),
                "Organization same registration false" to OrganizationSameData(false, action = { repository.hasAnotherWithRegistration(organization.id, organization.registrationNumber) }),
            )
            withData(data) {
                it.action() shouldBe it.result
            }
        }

        context(name = "Page", script = "/scripts/organization/page.cypher") {
            val organizations = entities(OrganizationEntity::class)
            val data = listOf(
                OrganizationSearchData("tóri", null, PageRequestPeriod(pageNumber = 1, pageSize = 15, pageSort = listOf(PageSort("updated_at", DESC))), compareByDescending { it.updatedAt }),
                OrganizationSearchData(null, true, PageRequestPeriod(pageNumber = 2, pageSize = 5, pageSort = listOf(PageSort("created_at", ASC))), compareBy { it.createdAt }),
                OrganizationSearchData(null, null, PageRequestPeriod(createdStart = OffsetDateTime.parse("2025-04-01T00:00:00.000000Z", ISO_OFFSET_DATE_TIME), pageNumber = 3, pageSize = 7, pageSort = listOf(PageSort("organization_name", ASC))), compareBy { it.organizationName }),
                OrganizationSearchData(null, null, PageRequestPeriod(updatedStart = OffsetDateTime.parse("2025-04-01T00:00:00.000000Z", ISO_OFFSET_DATE_TIME), updatedEnd = OffsetDateTime.parse("2025-10-31T23:59:59.999999Z", ISO_OFFSET_DATE_TIME), pageNumber = 2, pageSize = 4, pageSort = listOf(PageSort("fantasy_name", DESC))), compareByDescending { it.fantasyName }),
                OrganizationSearchData(null, true, PageRequestPeriod(updatedStart = OffsetDateTime.parse("2025-03-01T00:00:00.000000Z", ISO_OFFSET_DATE_TIME), updatedEnd = OffsetDateTime.parse("2025-07-31T23:59:59.999999Z", ISO_OFFSET_DATE_TIME), pageNumber = 1, pageSize = 3, pageSort = listOf(PageSort("created_at", DESC), PageSort("registration_number", ASC))), compareByDescending<OrganizationEntity> { it.createdAt }.thenBy { it.registrationNumber }),
            )
            withData(data) {
                val page = it.page(organizations.toList())
                val result = repository.page(it.keyword, it.isActive, it.pageable)
                result.contentList.size shouldBe page.contentList.size
                result.totalItems shouldBe page.totalItems
                result.totalPages shouldBe page.totalPages
                page.contentList.forEach { entity ->
                    result.contentList.contains(entity) shouldBe true
                }
            }
        }

    }
) {

    override fun createRepository(
        sessionFactory: SessionFactory
    ): OrganizationNeo4JRepository = OrganizationNeo4JRepository(sessionFactory)

}
