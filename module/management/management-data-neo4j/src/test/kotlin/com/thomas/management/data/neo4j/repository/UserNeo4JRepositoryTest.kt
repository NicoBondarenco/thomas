package com.thomas.management.data.neo4j.repository

import com.thomas.core.model.general.UserType
import com.thomas.core.model.general.UserType.COMMON
import com.thomas.core.model.pagination.PageRequestPeriod
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection.ASC
import com.thomas.core.model.pagination.PageSortDirection.DESC
import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.core.util.NumberUtils.randomInteger
import com.thomas.core.util.StringUtils.randomEmail
import com.thomas.core.util.StringUtils.randomString
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.UnitEntity
import com.thomas.management.data.entity.UnitRoleEntity
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.data.entity.UserSimpleEntity
import com.thomas.management.data.entity.userCompleteEntity
import com.thomas.management.data.entity.userEntity
import com.thomas.management.data.neo4j.model.mapper.toGroupCompleteEntity
import com.thomas.management.data.neo4j.model.mapper.toOrganizationEntity
import com.thomas.management.data.neo4j.model.mapper.toUnitEntity
import com.thomas.management.data.neo4j.model.mapper.toUserCompleteEntity
import com.thomas.management.data.neo4j.model.mapper.toUserSimpleEntity
import com.thomas.management.data.neo4j.model.node.GroupNode
import com.thomas.management.data.neo4j.model.node.OrganizationNode
import com.thomas.management.data.neo4j.model.node.UnitNode
import com.thomas.management.data.neo4j.model.node.UserNode
import com.thomas.management.data.neo4j.util.EntityFindOneData
import com.thomas.management.data.neo4j.util.EntitySameData
import com.thomas.management.data.neo4j.util.UserSearchData
import com.thomas.management.data.neo4j.util.toUserSimpleEntity
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
import java.util.UUID
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

            val data = mutableListOf<EntityFindOneData<UUID, UserCompleteEntity>>()

            organizations.forEach { organization ->
                users.filter {
                    it.userOrganization.id == organization.id
                }.take(5).forEach { user ->
                    data += EntityFindOneData(user.id, organization.id, user)
                }
                users.filter {
                    it.userOrganization.id != organization.id
                }.take(5).forEach { user ->
                    data += EntityFindOneData(user.id, organization.id, null)
                }
            }
            (1..5).forEach { _ ->
                data += EntityFindOneData(randomUUID(), randomUUID(), null)
            }

            withData(data) {
                val result = repository.one(it.field, it.organizationId, listOf(COMMON))
                result shouldBe it.entity
            }
        }

        context(name = "Page", script = "/scripts/user/page.cypher") {
            val users = entities(UserCompleteEntity::class)
            val organizations = entities(OrganizationEntity::class)

            val data = mapOf(
                "UserSearchData 01" to UserSearchData("josé", null, organizations.random().id, PageRequestPeriod(pageNumber = 2, pageSize = 6, pageSort = listOf(PageSort("updated_at", DESC))), compareByDescending { it.updatedAt }),
                "UserSearchData 02" to UserSearchData("eira", null, organizations.random().id, PageRequestPeriod(pageNumber = 1, pageSize = 5, pageSort = listOf(PageSort("document_number", ASC))), compareBy { it.documentNumber }),
                "UserSearchData 03" to UserSearchData("449", null, organizations.random().id, PageRequestPeriod(pageNumber = 1, pageSize = 5, pageSort = listOf(PageSort("first_name", ASC), PageSort("last_name", DESC))), compareBy<UserCompleteEntity> { it.firstName }.thenByDescending { it.lastName }),
                "UserSearchData 04" to UserSearchData("hotmail", null, organizations.random().id, PageRequestPeriod(pageNumber = 3, pageSize = 3, pageSort = listOf(PageSort("last_name", ASC), PageSort("created_at", DESC))), compareBy<UserCompleteEntity> { it.lastName }.thenByDescending { it.createdAt }),
                "UserSearchData 05" to UserSearchData(null, true, organizations.random().id, PageRequestPeriod(pageNumber = 4, pageSize = 4, pageSort = listOf(PageSort("first_name", ASC), PageSort("last_name", DESC))), compareBy<UserCompleteEntity> { it.firstName }.thenByDescending { it.lastName }),
                "UserSearchData 06" to UserSearchData(null, null, organizations.random().id, PageRequestPeriod(createdStart = OffsetDateTime.parse("2025-01-01T00:00:00.000000Z", ISO_OFFSET_DATE_TIME), createdEnd = OffsetDateTime.parse("2025-06-30T23:59:59.999999Z", ISO_OFFSET_DATE_TIME), pageSort = listOf(PageSort("updated_at", ASC))), compareBy { it.updatedAt }),
                "UserSearchData 07" to UserSearchData(null, null, organizations.random().id, PageRequestPeriod(updatedStart = OffsetDateTime.parse("2025-07-01T00:00:00.000000Z", ISO_OFFSET_DATE_TIME), updatedEnd = OffsetDateTime.parse("2025-12-31T23:59:59.999999Z", ISO_OFFSET_DATE_TIME), pageSort = listOf(PageSort("document_number", DESC))), compareByDescending { it.documentNumber }),
                "UserSearchData 08" to UserSearchData("JOSÉ", true, organizations.random().id, PageRequestPeriod(pageNumber = 2, pageSize = 5, updatedStart = OffsetDateTime.parse("2025-07-01T00:00:00.000000Z", ISO_OFFSET_DATE_TIME), updatedEnd = OffsetDateTime.parse("2025-12-31T23:59:59.999999Z", ISO_OFFSET_DATE_TIME), pageSort = listOf(PageSort("last_name", DESC), PageSort("updated_at", DESC))), compareByDescending<UserCompleteEntity> { it.lastName }.thenByDescending { it.updatedAt }),
            )

            withData(data) {
                val page = it.page(users.toList()).map { u -> u.toUserSimpleEntity() }
                val result = repository.page(it.keyword, it.isActive, listOf(COMMON), it.organizationId, it.pageable)
                result.contentList.size shouldBe page.contentList.size
                result.totalItems shouldBe page.totalItems
                result.totalPages shouldBe page.totalPages
                page.contentList.map { user -> user.id }.forEach { entity ->
                    result.contentList.map { user -> user.id }.contains(entity) shouldBe true
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
                        UnitRoleEntity(
                            roleUnit = it,
                            roleList = SecurityUnitRole.entries.shuffled().take(randomInteger(0, SecurityUnitRole.entries.size)).toSet()
                        )
                    }.toSet(),
                    userGroups = groups.take(randomInteger(0, groups.size)).toSet()
                )
            }.associateBy { it.id.toString() }

            withData(data) { user ->
                repository.create(user)

                val result = sessionFactory.openSession().load(UserNode::class.java, user.id, 5).toUserCompleteEntity()

                result.id shouldBe user.id
                result.firstName shouldBe user.firstName
                result.lastName shouldBe user.lastName
                result.documentNumber shouldBe user.documentNumber
                result.profilePhoto shouldBe user.profilePhoto
                result.userGender shouldBe user.userGender
                result.birthDate shouldBe user.birthDate
                result.passwordSalt shouldBe user.passwordSalt
                result.passwordHash shouldBe user.passwordHash
                result.userOrganization shouldBe user.userOrganization
                result.isActive shouldBe user.isActive
                result.createdAt shouldBe user.createdAt
                result.updatedAt shouldBe user.updatedAt

                result.userOrganization shouldBe user.userOrganization

                result.userUnits.size shouldBe user.userUnits.size

                user.userUnits.forEach { userUnit ->
                    result.userUnits.contains(userUnit) shouldBe true
                }

                result.userGroups.size shouldBe user.userGroups.size

                user.userGroups.map { it.id }.forEach { groupId ->
                    result.userGroups.any { it.id == groupId } shouldBe true
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
                        UnitRoleEntity(
                            roleUnit = it,
                            roleList = SecurityUnitRole.entries.shuffled().take(randomInteger(0, SecurityUnitRole.entries.size)).toSet()
                        )
                    }.toSet(),
                    userGroups = groups.take(randomInteger(0, groups.size)).toSet()
                )
            }.associateBy { it.id.toString() }

            withData(data) { user ->
                repository.update(user)

                val result = sessionFactory.openSession().load(UserNode::class.java, user.id, 5).toUserCompleteEntity()

                result.id shouldBe user.id
                result.firstName shouldBe user.firstName
                result.lastName shouldBe user.lastName
                result.documentNumber shouldBe user.documentNumber
                result.profilePhoto shouldBe user.profilePhoto
                result.userGender shouldBe user.userGender
                result.birthDate shouldBe user.birthDate
                result.passwordSalt shouldBe user.passwordSalt
                result.passwordHash shouldBe user.passwordHash
                result.userOrganization shouldBe user.userOrganization
                result.isActive shouldBe user.isActive
                result.createdAt shouldBe user.createdAt
                result.updatedAt shouldBe user.updatedAt

                result.userOrganization shouldBe user.userOrganization

                result.userUnits.size shouldBe user.userUnits.size

                user.userUnits.forEach { userUnit ->
                    result.userUnits.contains(userUnit) shouldBe true
                }

                result.userGroups.size shouldBe user.userGroups.size

                user.userGroups.map { it.id }.forEach { groupId ->
                    result.userGroups.any { it.id == groupId } shouldBe true
                }
            }

        }

        context(name = "Update Simple", script = "/scripts/user/page.cypher") {

            val data: Map<String, UserSimpleEntity> = entities(UserCompleteEntity::class)
                .filter { it.userGroups.isNotEmpty() && it.userUnits.isNotEmpty() }
                .shuffled().take(5).map { existent ->
                    val organization = existent.userOrganization

                    userEntity.copy(
                        id = existent.id,
                        userOrganization = organization,
                        organizationRoles = SecurityOrganizationRole.entries.shuffled().take(randomInteger(0, SecurityOrganizationRole.entries.size)).toSet(),
                    )
                }.associateBy { it.id.toString() }

            withData(data) { user ->
                repository.updateSimple(user)

                val result = sessionFactory.openSession().load(UserNode::class.java, user.id, 5).toUserCompleteEntity()

                result.id shouldBe user.id
                result.firstName shouldBe user.firstName
                result.lastName shouldBe user.lastName
                result.documentNumber shouldBe user.documentNumber
                result.profilePhoto shouldBe user.profilePhoto
                result.userGender shouldBe user.userGender
                result.birthDate shouldBe user.birthDate
                result.passwordSalt shouldBe user.passwordSalt
                result.passwordHash shouldBe user.passwordHash
                result.userOrganization shouldBe user.userOrganization
                result.isActive shouldBe user.isActive
                result.createdAt shouldBe user.createdAt
                result.updatedAt shouldBe user.updatedAt

                result.userOrganization shouldBe user.userOrganization

                result.userUnits.isNotEmpty() shouldBe true

                result.userGroups.isNotEmpty() shouldBe true
            }

        }

        context(name = "Simple by E-mail", script = "/scripts/user/page.cypher") {

            val data: Map<String, UserSimpleEntity> = entities(UserCompleteEntity::class)
                .shuffled()
                .take(5)
                .map { it.toUserSimpleEntity() }
                .associateBy { it.id.toString() }

            withData(data) { user ->
                repository.simpleByEmail(user.mainEmail)

                val result = sessionFactory.openSession().load(UserNode::class.java, user.id, 5).toUserSimpleEntity()

                result.id shouldBe user.id
                result.firstName shouldBe user.firstName
                result.lastName shouldBe user.lastName
                result.documentNumber shouldBe user.documentNumber
                result.profilePhoto shouldBe user.profilePhoto
                result.userGender shouldBe user.userGender
                result.birthDate shouldBe user.birthDate
                result.passwordSalt shouldBe user.passwordSalt
                result.passwordHash shouldBe user.passwordHash
                result.userOrganization shouldBe user.userOrganization
                result.isActive shouldBe user.isActive
                result.createdAt shouldBe user.createdAt
                result.updatedAt shouldBe user.updatedAt

                result.userOrganization shouldBe user.userOrganization
            }

        }

        context(name = "Exists same", script = "/scripts/user/page.cypher") {
            val user = entities(UserCompleteEntity::class).random()
            val organization = user.userOrganization
            val data = mapOf(
                "User same e-mail another user" to EntitySameData(true, action = { repository.hasAnotherWithEmail(randomUUID(), user.mainEmail) }),
                "User new e-mail new user" to EntitySameData(false, action = { repository.hasAnotherWithEmail(randomUUID(), randomEmail()) }),
                "User same e-mail same user" to EntitySameData(false, action = { repository.hasAnotherWithEmail(user.id, user.mainEmail) }),
                "User same document same organization" to EntitySameData(true, action = { repository.hasAnotherWithDocument(randomUUID(), organization.id, user.documentNumber) }),
                "User same document new user" to EntitySameData(false, action = { repository.hasAnotherWithDocument(randomUUID(), organization.id, randomString()) }),
                "User same document another organization" to EntitySameData(false, action = { repository.hasAnotherWithDocument(randomUUID(), randomUUID(), user.documentNumber) }),
                "User same document same user" to EntitySameData(false, action = { repository.hasAnotherWithDocument(user.id, organization.id, user.documentNumber) }),
            )
            withData(data) {
                it.action() shouldBe it.result
            }
        }

        context(name = "Limit", script = "/scripts/user/page.cypher") {
            val reached = UUID.fromString("744a0082-037a-4722-8730-60da67d2eea4")
            val available = UUID.fromString("ed45dad6-8afc-49f7-9215-ce1de2849f74")
            val userReached = entities(UserCompleteEntity::class).filter { it.userOrganization.id == reached }.random()
            val userAvailable = entities(UserCompleteEntity::class).filter { it.userOrganization.id == available }.random()

            repository.limitReached(randomUUID(), reached) shouldBe true
            repository.limitReached(userReached.id, reached) shouldBe false
            repository.limitReached(randomUUID(), available) shouldBe false
            repository.limitReached(userAvailable.id, available) shouldBe false
            repository.limitReached(randomUUID(), randomUUID()) shouldBe true
        }

        context(name = "Simple by ID", script = "/scripts/user/page.cypher") {

            val data: Map<String, UserSimpleEntity> = entities(UserCompleteEntity::class)
                .shuffled()
                .take(5)
                .map { it.toUserSimpleEntity() }
                .associateBy { it.id.toString() }

            withData(data) { user ->
                repository.byId(user.id)

                val result = sessionFactory.openSession().load(UserNode::class.java, user.id, 5).toUserSimpleEntity()

                result.id shouldBe user.id
                result.firstName shouldBe user.firstName
                result.lastName shouldBe user.lastName
                result.documentNumber shouldBe user.documentNumber
                result.profilePhoto shouldBe user.profilePhoto
                result.userGender shouldBe user.userGender
                result.birthDate shouldBe user.birthDate
                result.passwordSalt shouldBe user.passwordSalt
                result.passwordHash shouldBe user.passwordHash
                result.userOrganization shouldBe user.userOrganization
                result.isActive shouldBe user.isActive
                result.createdAt shouldBe user.createdAt
                result.updatedAt shouldBe user.updatedAt

                result.userOrganization shouldBe user.userOrganization
            }

        }

        context(name = "Find by Username", script = "/scripts/user/page.cypher") {
            val users = entities(UserCompleteEntity::class)
            val organizations = entities(OrganizationEntity::class)

            val data = mutableListOf<EntityFindOneData<String, UserCompleteEntity>>()

            organizations.forEach { organization ->
                users.filter {
                    it.userOrganization.id == organization.id
                }.take(5).forEach { user ->
                    data += EntityFindOneData(user.mainEmail, organization.id, user)
                }
            }
            (1..5).forEach { _ ->
                data += EntityFindOneData(randomEmail(), randomUUID(), null)
            }

            withData(data) {
                val result = repository.findByUsername(it.field)
                result shouldBe it.entity
            }
        }

        context(name = "Page Filter Type COMMON", script = "/scripts/user/type.cypher") {
            val users = entities(UserCompleteEntity::class)
            val organizations = entities(OrganizationEntity::class)

            val data = mapOf(
                "UserSearchData 01" to UserSearchData("josé", null, organizations.random().id, PageRequestPeriod(pageNumber = 2, pageSize = 6, pageSort = listOf(PageSort("updated_at", DESC))), compareByDescending { it.updatedAt }),
                "UserSearchData 02" to UserSearchData("eira", null, organizations.random().id, PageRequestPeriod(pageNumber = 1, pageSize = 5, pageSort = listOf(PageSort("document_number", ASC))), compareBy { it.documentNumber }),
                "UserSearchData 03" to UserSearchData("449", null, organizations.random().id, PageRequestPeriod(pageNumber = 1, pageSize = 5, pageSort = listOf(PageSort("first_name", ASC), PageSort("last_name", DESC))), compareBy<UserCompleteEntity> { it.firstName }.thenByDescending { it.lastName }),
                "UserSearchData 04" to UserSearchData("hotmail", null, organizations.random().id, PageRequestPeriod(pageNumber = 3, pageSize = 3, pageSort = listOf(PageSort("last_name", ASC), PageSort("created_at", DESC))), compareBy<UserCompleteEntity> { it.lastName }.thenByDescending { it.createdAt }),
                "UserSearchData 05" to UserSearchData(null, true, organizations.random().id, PageRequestPeriod(pageNumber = 4, pageSize = 4, pageSort = listOf(PageSort("first_name", ASC), PageSort("last_name", DESC))), compareBy<UserCompleteEntity> { it.firstName }.thenByDescending { it.lastName }),
                "UserSearchData 06" to UserSearchData(null, null, organizations.random().id, PageRequestPeriod(createdStart = OffsetDateTime.parse("2025-01-01T00:00:00.000000Z", ISO_OFFSET_DATE_TIME), createdEnd = OffsetDateTime.parse("2025-06-30T23:59:59.999999Z", ISO_OFFSET_DATE_TIME), pageSort = listOf(PageSort("updated_at", ASC))), compareBy { it.updatedAt }),
                "UserSearchData 07" to UserSearchData(null, null, organizations.random().id, PageRequestPeriod(updatedStart = OffsetDateTime.parse("2025-07-01T00:00:00.000000Z", ISO_OFFSET_DATE_TIME), updatedEnd = OffsetDateTime.parse("2025-12-31T23:59:59.999999Z", ISO_OFFSET_DATE_TIME), pageSort = listOf(PageSort("document_number", DESC))), compareByDescending { it.documentNumber }),
                "UserSearchData 08" to UserSearchData("JOSÉ", true, organizations.random().id, PageRequestPeriod(pageNumber = 2, pageSize = 5, updatedStart = OffsetDateTime.parse("2025-07-01T00:00:00.000000Z", ISO_OFFSET_DATE_TIME), updatedEnd = OffsetDateTime.parse("2025-12-31T23:59:59.999999Z", ISO_OFFSET_DATE_TIME), pageSort = listOf(PageSort("last_name", DESC), PageSort("updated_at", DESC))), compareByDescending<UserCompleteEntity> { it.lastName }.thenByDescending { it.updatedAt }),
            )

            withData(data) {
                val page = it.page(users.filter { u -> u.userType == COMMON }.toList()).map { u -> u.toUserSimpleEntity() }
                val result = repository.page(it.keyword, it.isActive, listOf(COMMON), it.organizationId, it.pageable)
                result.contentList.size shouldBe page.contentList.size
                result.totalItems shouldBe page.totalItems
                result.totalPages shouldBe page.totalPages
                page.contentList.map { user -> user.id }.forEach { entity ->
                    result.contentList.map { user -> user.id }.contains(entity) shouldBe true
                }
            }
        }

        context(name = "Page Filter Type ADMINISTRATOR MASTER", script = "/scripts/user/type.cypher") {
            val users = entities(UserCompleteEntity::class)
            val organizations = entities(OrganizationEntity::class)

            val data = mapOf(
                "UserSearchData 01" to UserSearchData("josé", null, organizations.random().id, PageRequestPeriod(pageNumber = 2, pageSize = 6, pageSort = listOf(PageSort("updated_at", DESC))), compareByDescending { it.updatedAt }),
                "UserSearchData 02" to UserSearchData("eira", null, organizations.random().id, PageRequestPeriod(pageNumber = 1, pageSize = 5, pageSort = listOf(PageSort("document_number", ASC))), compareBy { it.documentNumber }),
                "UserSearchData 03" to UserSearchData("449", null, organizations.random().id, PageRequestPeriod(pageNumber = 1, pageSize = 5, pageSort = listOf(PageSort("first_name", ASC), PageSort("last_name", DESC))), compareBy<UserCompleteEntity> { it.firstName }.thenByDescending { it.lastName }),
                "UserSearchData 04" to UserSearchData("hotmail", null, organizations.random().id, PageRequestPeriod(pageNumber = 3, pageSize = 3, pageSort = listOf(PageSort("last_name", ASC), PageSort("created_at", DESC))), compareBy<UserCompleteEntity> { it.lastName }.thenByDescending { it.createdAt }),
                "UserSearchData 05" to UserSearchData(null, true, organizations.random().id, PageRequestPeriod(pageNumber = 4, pageSize = 4, pageSort = listOf(PageSort("first_name", ASC), PageSort("last_name", DESC))), compareBy<UserCompleteEntity> { it.firstName }.thenByDescending { it.lastName }),
                "UserSearchData 06" to UserSearchData(null, null, organizations.random().id, PageRequestPeriod(createdStart = OffsetDateTime.parse("2025-01-01T00:00:00.000000Z", ISO_OFFSET_DATE_TIME), createdEnd = OffsetDateTime.parse("2025-06-30T23:59:59.999999Z", ISO_OFFSET_DATE_TIME), pageSort = listOf(PageSort("updated_at", ASC))), compareBy { it.updatedAt }),
                "UserSearchData 07" to UserSearchData(null, null, organizations.random().id, PageRequestPeriod(updatedStart = OffsetDateTime.parse("2025-07-01T00:00:00.000000Z", ISO_OFFSET_DATE_TIME), updatedEnd = OffsetDateTime.parse("2025-12-31T23:59:59.999999Z", ISO_OFFSET_DATE_TIME), pageSort = listOf(PageSort("document_number", DESC))), compareByDescending { it.documentNumber }),
                "UserSearchData 08" to UserSearchData("JOSÉ", true, organizations.random().id, PageRequestPeriod(pageNumber = 2, pageSize = 5, updatedStart = OffsetDateTime.parse("2025-07-01T00:00:00.000000Z", ISO_OFFSET_DATE_TIME), updatedEnd = OffsetDateTime.parse("2025-12-31T23:59:59.999999Z", ISO_OFFSET_DATE_TIME), pageSort = listOf(PageSort("last_name", DESC), PageSort("updated_at", DESC))), compareByDescending<UserCompleteEntity> { it.lastName }.thenByDescending { it.updatedAt }),
            )

            withData(data) {
                val page = it.page(users.toList()).map { u -> u.toUserSimpleEntity() }
                val result = repository.page(it.keyword, it.isActive, UserType.entries, it.organizationId, it.pageable)
                result.contentList.size shouldBe page.contentList.size
                result.totalItems shouldBe page.totalItems
                result.totalPages shouldBe page.totalPages
                page.contentList.map { user -> user.id }.forEach { entity ->
                    result.contentList.map { user -> user.id }.contains(entity) shouldBe true
                }
            }
        }

    }
) {

    override fun createRepository(
        sessionFactory: SessionFactory
    ): UserNeo4JRepository = UserNeo4JRepository(sessionFactory)

}