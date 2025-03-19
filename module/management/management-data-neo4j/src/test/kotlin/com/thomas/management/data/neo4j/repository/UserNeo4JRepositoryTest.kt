package com.thomas.management.data.neo4j.repository

import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection.ASC
import com.thomas.core.model.pagination.PageSortDirection.DESC
import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.core.util.NumberUtils.randomInteger
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.UnitEntity
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.data.entity.UserUnitEntity
import com.thomas.management.data.entity.userCompleteEntity
import com.thomas.management.data.neo4j.model.mapper.toGroupCompleteEntity
import com.thomas.management.data.neo4j.model.mapper.toOrganizationEntity
import com.thomas.management.data.neo4j.model.mapper.toUnitEntity
import com.thomas.management.data.neo4j.model.mapper.toUserCompleteEntity
import com.thomas.management.data.neo4j.model.mapper.toUserNode
import com.thomas.management.data.neo4j.model.node.GroupNode
import com.thomas.management.data.neo4j.model.node.OrganizationNode
import com.thomas.management.data.neo4j.model.node.UnitNode
import com.thomas.management.data.neo4j.model.node.UserNode
import com.thomas.management.data.neo4j.util.EntityFindOneData
import com.thomas.management.data.neo4j.util.UserSearchData
import com.thomas.management.data.neo4j.util.toUserSimpleEntity
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
import java.util.UUID.randomUUID
import org.neo4j.ogm.session.SessionFactory

class UserNeo4JRepositoryTest : ManagementFunSpec<UserNeo4JRepository>(
    body = {
        initNodes(
            mapOf(
                UserNode::class to { (it as UserNode).toUserCompleteEntity() },
                GroupNode::class to { (it as GroupNode).toGroupCompleteEntity() },
                UnitNode::class to { (it as UnitNode).toUnitEntity() },
                OrganizationNode::class to { (it as OrganizationNode).toOrganizationEntity() },
            )
        )

        context(name = "One", script = "/scripts/user/page.cypher") {
            val users = entities(UserCompleteEntity::class)
            val organizations = entities(OrganizationEntity::class)

            val data = mutableListOf<EntityFindOneData<UserCompleteEntity>>()

            organizations.forEach { organization ->
                users.filter {
                    it.userOrganization.id == organization.id
                }.take(5).forEach { group ->
                    data += EntityFindOneData(group.id, organization.id, group)
                }
                users.filter {
                    it.userOrganization.id != organization.id
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

        /*context(name = "Page", script = "/scripts/user/page.cypher") {
            val users = entities(UserCompleteEntity::class)
            val organizations = entities(OrganizationEntity::class)

            val data = listOf(
                UserSearchData("josé", null, organizations.random().id, PageRequestPeriod(pageNumber = 2, pageSize = 6, pageSort = listOf(PageSort("updated_at", DESC))), compareByDescending { it.updatedAt }),
                UserSearchData("eira", null, organizations.random().id, PageRequestPeriod(pageNumber = 1, pageSize = 5, pageSort = listOf(PageSort("document_number", ASC))), compareBy { it.documentNumber }),
                UserSearchData("449", null, organizations.random().id, PageRequestPeriod(pageNumber = 1, pageSize = 5, pageSort = listOf(PageSort("first_name", ASC), PageSort("last_name", DESC))), compareBy<UserCompleteEntity> { it.firstName }.thenByDescending { it.lastName }),
                UserSearchData("hotmail", null, organizations.random().id, PageRequestPeriod(pageNumber = 3, pageSize = 3, pageSort = listOf(PageSort("last_name", ASC), PageSort("created_at", DESC))), compareBy<UserCompleteEntity> { it.lastName }.thenByDescending { it.createdAt }),
                UserSearchData(null, true, organizations.random().id, PageRequestPeriod(pageNumber = 4, pageSize = 4, pageSort = listOf(PageSort("first_name", ASC), PageSort("last_name", DESC))), compareBy<UserCompleteEntity> { it.firstName }.thenByDescending { it.lastName }),
                UserSearchData(null, null, organizations.random().id, PageRequestPeriod(createdStart = OffsetDateTime.parse("2025-01-01T00:00:00.000000Z", ISO_OFFSET_DATE_TIME), createdEnd = OffsetDateTime.parse("2025-06-30T23:59:59.999999Z", ISO_OFFSET_DATE_TIME), pageSort = listOf(PageSort("updated_at", ASC))), compareBy { it.updatedAt }),
                UserSearchData(null, null, organizations.random().id, PageRequestPeriod(updatedStart = OffsetDateTime.parse("2025-07-01T00:00:00.000000Z", ISO_OFFSET_DATE_TIME), updatedEnd = OffsetDateTime.parse("2025-12-31T23:59:59.999999Z", ISO_OFFSET_DATE_TIME), pageSort = listOf(PageSort("first_name", DESC))), compareByDescending { it.firstName }),
                UserSearchData("JOSÉ", true, organizations.random().id, PageRequestPeriod(pageNumber = 2, pageSize = 5, updatedStart = OffsetDateTime.parse("2025-07-01T00:00:00.000000Z", ISO_OFFSET_DATE_TIME), updatedEnd = OffsetDateTime.parse("2025-12-31T23:59:59.999999Z", ISO_OFFSET_DATE_TIME), pageSort = listOf(PageSort("last_name", DESC), PageSort("updated_at", DESC))), compareByDescending<UserCompleteEntity> { it.lastName }.thenByDescending { it.updatedAt }),
            )

            withData(data) {
                val page = it.page(users.toList()).map { u -> u.toUserSimpleEntity() }
                val result = repository.page(it.keyword, it.isActive, it.organizationId, it.pageable)
                result.contentList.size shouldBe page.contentList.size
                result.totalItems shouldBe page.totalItems
                result.totalPages shouldBe page.totalPages
                page.contentList.forEach { entity ->
                    result.contentList.contains(entity) shouldBe true
                }
            }
        }

        context(name = "Save", script = "/scripts/user/page.cypher") {

            val data: Map<String, UserCompleteEntity> = (1..10).map {
                val organization = entities(OrganizationEntity::class).random()
                val units = entities(UnitEntity::class).filter {
                    it.unitOrganization.id == organization.id
                }
                val groups = entities(GroupCompleteEntity::class).filter {
                    it.groupOrganization.id == organization.id
                }

                userCompleteEntity.copy(
                    userOrganization = organization,
                    organizationRoles = SecurityOrganizationRole.entries.shuffled().take(randomInteger(0, SecurityOrganizationRole.entries.size)).toSet(),
                    userUnits = units.take(randomInteger(0, units.size)).map {
                        UserUnitEntity(
                            userUnit = it,
                            userRoles = SecurityUnitRole.entries.shuffled().take(randomInteger(0, SecurityUnitRole.entries.size)).toSet()
                        )
                    }.toSet(),
                    userGroups = groups.take(randomInteger(0, groups.size)).toSet()
                )
            }.associateBy { it.id.toString() }

            withData(data) { user ->
                repository.create(user)

                val node = user.toUserNode()
                val result = sessionFactory.openSession().load(UserNode::class.java, user.id, 5)

                result.id shouldBe node.id
                result.firstName shouldBe node.firstName
                result.lastName shouldBe node.lastName
                result.documentNumber shouldBe node.documentNumber
                result.profilePhoto shouldBe node.profilePhoto
                result.userGender shouldBe node.userGender
                result.birthDate shouldBe node.birthDate
                result.passwordSalt shouldBe node.passwordSalt
                result.passwordHash shouldBe node.passwordHash
                result.userOrganization shouldBe node.userOrganization
                result.isActive shouldBe node.isActive
                result.createdAt shouldBe node.createdAt
                result.updatedAt shouldBe node.updatedAt

                result.userOrganization shouldBe node.userOrganization

                (result.userUnits?.size ?: 0) shouldBe (node.userUnits?.size ?: 0)

                node.userUnits?.forEach { userUnit ->
                    result.userUnits!!.contains(userUnit) shouldBe true
                }

                (result.userGroups?.size ?: 0) shouldBe (node.userGroups?.size ?: 0)

                node.userGroups?.map { it.id }?.forEach { groupId ->
                    result.userGroups!!.any { it.id == groupId } shouldBe true
                }

            }

        }

        context(name = "Update", script = "/scripts/user/page.cypher") {

            val data: Map<String, UserCompleteEntity> = entities(UserCompleteEntity::class).shuffled().take(10).map { existent ->
                val organization = existent.userOrganization
                val units = entities(UnitEntity::class).filter {
                    it.unitOrganization.id == organization.id
                }
                val groups = entities(GroupCompleteEntity::class).filter {
                    it.groupOrganization.id == organization.id
                }

                userCompleteEntity.copy(
                    id = existent.id,
                    userOrganization = organization,
                    organizationRoles = SecurityOrganizationRole.entries.shuffled().take(randomInteger(0, SecurityOrganizationRole.entries.size)).toSet(),
                    userUnits = units.take(randomInteger(0, units.size)).map {
                        UserUnitEntity(
                            userUnit = it,
                            userRoles = SecurityUnitRole.entries.shuffled().take(randomInteger(0, SecurityUnitRole.entries.size)).toSet()
                        )
                    }.toSet(),
                    userGroups = groups.take(randomInteger(0, groups.size)).toSet()
                )
            }.associateBy { it.id.toString() }

            withData(data) { user ->
                repository.update(user)

                val node = user.toUserNode()
                val result = sessionFactory.openSession().load(UserNode::class.java, user.id, 5)

                result.id shouldBe node.id
                result.firstName shouldBe node.firstName
                result.lastName shouldBe node.lastName
                result.documentNumber shouldBe node.documentNumber
                result.profilePhoto shouldBe node.profilePhoto
                result.userGender shouldBe node.userGender
                result.birthDate shouldBe node.birthDate
                result.passwordSalt shouldBe node.passwordSalt
                result.passwordHash shouldBe node.passwordHash
                result.userOrganization shouldBe node.userOrganization
                result.isActive shouldBe node.isActive
                result.createdAt shouldBe node.createdAt
                result.updatedAt shouldBe node.updatedAt

                result.userOrganization shouldBe node.userOrganization

                (result.userUnits?.size ?: 0) shouldBe (node.userUnits?.size ?: 0)

                node.userUnits?.forEach { userUnit ->
                    result.userUnits!!.contains(userUnit) shouldBe true
                }

                (result.userGroups?.size ?: 0) shouldBe (node.userGroups?.size ?: 0)

                node.userGroups?.map { it.id }?.forEach { groupId ->
                    result.userGroups!!.any { it.id == groupId } shouldBe true
                }
            }

        }*/

    }
) {

    override fun createRepository(
        sessionFactory: SessionFactory
    ): UserNeo4JRepository = UserNeo4JRepository(sessionFactory)

}