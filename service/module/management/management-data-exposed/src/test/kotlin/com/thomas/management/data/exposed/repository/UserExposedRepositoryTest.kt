package com.thomas.management.data.exposed.repository

import com.thomas.core.model.general.Gender.CIS_FEMALE
import com.thomas.core.model.general.Gender.CIS_MALE
import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection.ASC
import com.thomas.core.model.pagination.PageSortDirection.DESC
import com.thomas.core.model.security.SecurityRole.MASTER
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_CREATE
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_DELETE
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_READ
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_UPDATE
import com.thomas.core.model.security.SecurityRole.ROLE_USER_CREATE
import com.thomas.core.model.security.SecurityRole.ROLE_USER_READ
import com.thomas.core.model.security.SecurityRole.ROLE_USER_UPDATE
import com.thomas.management.data.exposed.extension.guardiansGalaxyGroup
import com.thomas.management.data.exposed.extension.theAvengersGroup
import com.thomas.management.data.exposed.extension.toUserCompleteEntity
import com.thomas.management.data.exposed.extension.userSignup
import com.thomas.management.data.exposed.extension.userUpsert
import com.thomas.management.data.exposed.mapping.UserExposedEntity
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset.UTC
import java.util.UUID.fromString
import java.util.UUID.randomUUID
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@Suppress("LargeClass")
class UserExposedRepositoryTest : BaseExposedRepositoryTest() {

    private lateinit var repository: UserExposedRepository

    override fun configureBeforeAll() {
        repository = UserExposedRepository(database)
    }

    @Test
    fun `Page without filter and order`() {
        insertData("user-page-insert")
        val page = repository.page(pageable = PageRequest())
        assertEquals(10, page.contentList.size)
        assertEquals(45, page.totalItems)
        assertEquals(5, page.totalPages)
        assertTrue(page.sortedBy.isEmpty())
    }

    @Test
    fun `Page with order`() {
        insertData("user-page-insert")
        val page = repository.page(pageable = PageRequest(pageSort = listOf(PageSort("updated_at", DESC))))
        assertEquals(10, page.contentList.size)
        assertEquals(45, page.totalItems)
        assertEquals(5, page.totalPages)
        assertEquals(1, page.sortedBy.size)
        assertEquals("updated_at", page.sortedBy.first().sortField)
        assertEquals(DESC, page.sortedBy.first().sortDirection)
        listOf(
            fromString("e40e9e1b-0040-4f78-82aa-b38dbea58b3e"),
            fromString("7c9fe91b-e8e0-4446-b4c8-91905111a919"),
            fromString("e00d9d88-a9c3-49ac-948c-1c54f1c3980d"),
            fromString("3005ba9c-bd00-4ddd-886e-746515d0b3b1"),
            fromString("00168bb5-f15e-4216-b891-cf9168e7740d"),
            fromString("b6699478-e293-42b4-828c-4617864b8ec7"),
            fromString("bcbd4b4a-9b95-43f8-88ed-1b1dd708eda3"),
            fromString("12c3856c-5f5b-4086-b56d-7514289d1a9b"),
            fromString("788eef4e-3d1c-405c-ac74-794cb4e546bf"),
            fromString("750b3d62-8d4b-4cd0-9f62-82380ed16d5b"),
        ).forEach { id ->
            assertTrue(page.contentList.any { it.id == id })
        }
    }

    @Test
    fun `Page with filter document number`() {
        insertData("user-page-insert")
        val page = repository.page(
            keywordText = "986",
            pageable = PageRequest(),
        )
        assertEquals(2, page.contentList.size)
        assertEquals(2, page.totalItems)
        assertEquals(1, page.totalPages)
        assertTrue(page.sortedBy.isEmpty())
        listOf(
            fromString("c9030de7-0197-4d6c-9814-d9c163171fec"),
            fromString("d37f5e72-f5fc-4bd6-9797-344daa11d1fd"),
        ).forEach { id ->
            assertTrue(page.contentList.any { it.id == id })
        }
    }

    @Test
    fun `Page with filter first name`() {
        insertData("user-page-insert")
        val page = repository.page(
            keywordText = "Mar",
            pageable = PageRequest(),
        )
        assertEquals(6, page.contentList.size)
        assertEquals(6, page.totalItems)
        assertEquals(1, page.totalPages)
        assertTrue(page.sortedBy.isEmpty())
        listOf(
            fromString("ba0c4ea0-df86-47a9-8497-f5dfbc86afc4"),
            fromString("12c3856c-5f5b-4086-b56d-7514289d1a9b"),
            fromString("f9e4afff-5aa5-433e-9a8b-8684c2d0f106"),
            fromString("0de13556-afe6-4c65-a27e-c3596f893b17"),
            fromString("12c2086c-ce54-4dc8-a923-a6394c43e0a7"),
            fromString("26c92737-962a-4823-8875-f373ab9b2fde"),
        ).forEach { id ->
            assertTrue(page.contentList.any { it.id == id })
        }
    }

    @Test
    fun `Page with filter last name`() {
        insertData("user-page-insert")
        val page = repository.page(
            keywordText = "Silva",
            pageable = PageRequest(),
        )
        assertEquals(9, page.contentList.size)
        assertEquals(9, page.totalItems)
        assertEquals(1, page.totalPages)
        assertTrue(page.sortedBy.isEmpty())
        listOf(
            fromString("e535818f-d44e-49f1-9eeb-e3b24a526405"),
            fromString("bcbd4b4a-9b95-43f8-88ed-1b1dd708eda3"),
            fromString("4c979e44-faf2-443f-8105-53460185be38"),
            fromString("f792d2e0-bded-4a2a-a833-814cf6402348"),
            fromString("12c2086c-ce54-4dc8-a923-a6394c43e0a7"),
            fromString("c9030de7-0197-4d6c-9814-d9c163171fec"),
            fromString("1d7034a3-a974-46d1-8d0e-bf3ed6526f03"),
            fromString("b6699478-e293-42b4-828c-4617864b8ec7"),
            fromString("46678d45-e677-4b66-9869-82a1e76f2483"),
        ).forEach { id ->
            assertTrue(page.contentList.any { it.id == id })
        }
    }

    @Test
    fun `Page with filter main email`() {
        insertData("user-page-insert")
        val page = repository.page(
            keywordText = "pereira@email",
            pageable = PageRequest(),
        )
        assertEquals(4, page.contentList.size)
        assertEquals(4, page.totalItems)
        assertEquals(1, page.totalPages)
        assertTrue(page.sortedBy.isEmpty())
        listOf(
            fromString("750b3d62-8d4b-4cd0-9f62-82380ed16d5b"),
            fromString("23aece33-b541-4cd9-8c4e-b1121347c999"),
            fromString("94dddc64-6774-4c9d-9a86-6a5b6c06ec84"),
            fromString("00168bb5-f15e-4216-b891-cf9168e7740d"),
        ).forEach { id ->
            assertTrue(page.contentList.any { it.id == id })
        }
    }

    @Test
    fun `Page with filter is active`() {
        insertData("user-page-insert")
        val page = repository.page(
            isActive = true,
            pageable = PageRequest(
                pageNumber = 2,
                pageSize = 5,
                pageSort = listOf(PageSort("created_at", DESC))
            ),
        )
        assertEquals(5, page.contentList.size)
        assertEquals(32, page.totalItems)
        assertEquals(7, page.totalPages)
        assertEquals(1, page.sortedBy.size)
        assertEquals("created_at", page.sortedBy.first().sortField)
        assertEquals(DESC, page.sortedBy.first().sortDirection)
        listOf(
            fromString("00168bb5-f15e-4216-b891-cf9168e7740d"),
            fromString("05c55e1a-acc4-4d89-a36c-834fe48bd458"),
            fromString("a4a6b518-3308-44e2-b053-c015dec4479a"),
            fromString("7c9fe91b-e8e0-4446-b4c8-91905111a919"),
            fromString("788eef4e-3d1c-405c-ac74-794cb4e546bf"),
        ).forEach { id ->
            assertTrue(page.contentList.any { it.id == id })
        }
    }

    @Test
    fun `Page with filter created start`() {
        insertData("user-page-insert")
        val page = repository.page(
            createdStart = OffsetDateTime.of(2024, 8, 1, 1, 48, 17, 683000, UTC),
            pageable = PageRequest(
                pageNumber = 3,
                pageSize = 3,
                pageSort = listOf(PageSort("first_name", ASC))
            ),
        )
        assertEquals(3, page.contentList.size)
        assertEquals(20, page.totalItems)
        assertEquals(7, page.totalPages)
        assertEquals(1, page.sortedBy.size)
        assertEquals("first_name", page.sortedBy.first().sortField)
        assertEquals(ASC, page.sortedBy.first().sortDirection)
        listOf(
            fromString("750b3d62-8d4b-4cd0-9f62-82380ed16d5b"),
            fromString("7c9fe91b-e8e0-4446-b4c8-91905111a919"),
            fromString("3994c8bc-8799-4bc7-8b26-b49af7fa4858"),
        ).forEach { id ->
            assertTrue(page.contentList.any { it.id == id })
        }
    }

    @Test
    fun `Page with filter created end`() {
        insertData("user-page-insert")
        val page = repository.page(
            createdEnd = OffsetDateTime.of(2024, 3, 26, 12, 52, 23, 623000000, UTC),
            pageable = PageRequest(
                pageNumber = 2,
                pageSize = 5,
                pageSort = listOf(PageSort("first_name", DESC))
            ),
        )
        assertEquals(4, page.contentList.size)
        assertEquals(9, page.totalItems)
        assertEquals(2, page.totalPages)
        assertEquals(1, page.sortedBy.size)
        assertEquals("first_name", page.sortedBy.first().sortField)
        assertEquals(DESC, page.sortedBy.first().sortDirection)
        listOf(
            fromString("f792d2e0-bded-4a2a-a833-814cf6402348"),
            fromString("b79dc2d0-a4cc-49e5-b006-2e638cc70fb6"),
            fromString("11becf75-121a-4b43-ade3-6626bde70420"),
            fromString("472e6a00-a16d-49fd-9a83-e0c2334a91d9"),
        ).forEach { id ->
            assertTrue(page.contentList.any { it.id == id })
        }
    }

    @Test
    fun `Page with filter update start`() {
        insertData("user-page-insert")
        val page = repository.page(
            updatedStart = OffsetDateTime.of(2024, 8, 17, 4, 42, 28, 925059000, UTC),
            pageable = PageRequest(
                pageNumber = 3,
                pageSize = 3,
                pageSort = listOf(PageSort("first_name", DESC), PageSort("document_number", DESC))
            ),
        )
        assertEquals(3, page.contentList.size)
        assertEquals(17, page.totalItems)
        assertEquals(6, page.totalPages)
        assertEquals(2, page.sortedBy.size)
        assertEquals("first_name", page.sortedBy[0].sortField)
        assertEquals(DESC, page.sortedBy[0].sortDirection)
        assertEquals("document_number", page.sortedBy[1].sortField)
        assertEquals(DESC, page.sortedBy[1].sortDirection)
        listOf(
            fromString("00168bb5-f15e-4216-b891-cf9168e7740d"),
            fromString("c9030de7-0197-4d6c-9814-d9c163171fec"),
            fromString("bcbd4b4a-9b95-43f8-88ed-1b1dd708eda3"),
        ).forEach { id ->
            assertTrue(page.contentList.any { it.id == id })
        }
    }

    @Test
    fun `Page with filter updated end`() {
        insertData("user-page-insert")
        val page = repository.page(
            updatedEnd = OffsetDateTime.of(2024, 3, 11, 5, 55, 57, 154768000, UTC),
            pageable = PageRequest(
                pageNumber = 2,
                pageSize = 5,
                pageSort = listOf(PageSort("last_name", DESC), PageSort("first_name", ASC))
            ),
        )
        assertEquals(3, page.contentList.size)
        assertEquals(8, page.totalItems)
        assertEquals(2, page.totalPages)
        assertEquals(2, page.sortedBy.size)
        assertEquals("last_name", page.sortedBy[0].sortField)
        assertEquals(DESC, page.sortedBy[0].sortDirection)
        assertEquals("first_name", page.sortedBy[1].sortField)
        assertEquals(ASC, page.sortedBy[1].sortDirection)
        listOf(
            fromString("d37f5e72-f5fc-4bd6-9797-344daa11d1fd"),
            fromString("7f2c137c-72ff-4b7d-b249-09b9eea9e12c"),
            fromString("472e6a00-a16d-49fd-9a83-e0c2334a91d9"),
        ).forEach { id ->
            assertTrue(page.contentList.any { it.id == id })
        }
    }

    @Test
    fun `Page with filter created range`() {
        insertData("user-page-insert")
        val page = repository.page(
            createdStart = OffsetDateTime.of(2024, 4, 23, 4, 58, 23, 879000000, UTC),
            createdEnd = OffsetDateTime.of(2024, 8, 30, 14, 49, 5, 844000000, UTC),
            pageable = PageRequest(
                pageNumber = 4,
                pageSize = 4,
                pageSort = listOf(PageSort("updated_at", DESC))
            ),
        )
        assertEquals(4, page.contentList.size)
        assertEquals(20, page.totalItems)
        assertEquals(5, page.totalPages)
        assertEquals(1, page.sortedBy.size)
        assertEquals("updated_at", page.sortedBy[0].sortField)
        assertEquals(DESC, page.sortedBy[0].sortDirection)
        listOf(
            fromString("800659d2-2a38-479b-83ba-4e619a85cae1"),
            fromString("46678d45-e677-4b66-9869-82a1e76f2483"),
            fromString("74a1ec20-6dae-470b-8010-d2486822316d"),
            fromString("43393528-fbcc-4495-a303-6b4f6395871b"),
        ).forEach { id ->
            assertTrue(page.contentList.any { it.id == id })
        }
    }

    @Test
    fun `Page with filter updated range`() {
        insertData("user-page-insert")
        val page = repository.page(
            updatedStart = OffsetDateTime.of(2024, 7, 31, 7, 59, 29, 805091000, UTC),
            updatedEnd = OffsetDateTime.of(2024, 12, 28, 18, 0, 27, 787116000, UTC),
            pageable = PageRequest(
                pageNumber = 2,
                pageSize = 6,
                pageSort = listOf(PageSort("created_at", DESC))
            ),
        )
        assertEquals(6, page.contentList.size)
        assertEquals(20, page.totalItems)
        assertEquals(4, page.totalPages)
        assertEquals(1, page.sortedBy.size)
        assertEquals("created_at", page.sortedBy[0].sortField)
        assertEquals(DESC, page.sortedBy[0].sortDirection)
        listOf(
            fromString("7c9fe91b-e8e0-4446-b4c8-91905111a919"),
            fromString("750b3d62-8d4b-4cd0-9f62-82380ed16d5b"),
            fromString("788eef4e-3d1c-405c-ac74-794cb4e546bf"),
            fromString("a4a6b518-3308-44e2-b053-c015dec4479a"),
            fromString("05c55e1a-acc4-4d89-a36c-834fe48bd458"),
            fromString("3994c8bc-8799-4bc7-8b26-b49af7fa4858"),
        ).forEach { id ->
            assertTrue(page.contentList.any { it.id == id })
        }
    }

    @Test
    fun `One with role and group list`() {
        insertData("user-one-insert")
        val entity = repository.one(fromString("436d12f3-d23e-4346-b847-37188521a968"))
        assertNotNull(entity)
        assertEquals("Anthony", entity!!.firstName)
        assertEquals("Stark", entity.lastName)
        assertEquals("iron.man@avengers.com", entity.mainEmail)
        assertEquals("10775576042", entity.documentNumber)
        assertEquals("16988776655", entity.phoneNumber)
        assertEquals(LocalDate.of(1990, 4, 28), entity.birthDate)
        assertEquals(CIS_MALE, entity.userGender)
        assertTrue(entity.isActive)

        listOf(
            MASTER,
            ROLE_USER_READ,
            ROLE_USER_CREATE,
            ROLE_USER_UPDATE,
            ROLE_GROUP_READ,
            ROLE_GROUP_CREATE,
            ROLE_GROUP_UPDATE,
            ROLE_GROUP_DELETE,
        ).forEach { role ->
            assertTrue(entity.userRoles.any { it == role })
        }

        listOf(
            fromString("dc25382c-6fe3-4ee5-a12e-59c8fd3b5442"),
            fromString("1f85e146-32ac-4fdd-a3ae-e97a55978c99"),
        ).forEach { groupId ->
            assertTrue(entity.userGroups.any { it.id == groupId })
        }
    }

    @Test
    fun `One with role list`() {
        insertData("user-one-insert")
        val entity = repository.one(fromString("2ba86417-aeb0-46f6-9011-61e0aa3593f0"))
        assertNotNull(entity)
        assertEquals("Steve", entity!!.firstName)
        assertEquals("Rogers", entity.lastName)
        assertEquals("captain.america@avengers.com", entity.mainEmail)
        assertEquals("47851306019", entity.documentNumber)
        assertEquals("14900112233", entity.phoneNumber)
        assertEquals(LocalDate.of(1991, 5, 29), entity.birthDate)
        assertEquals(CIS_MALE, entity.userGender)
        assertTrue(entity.isActive)
        assertTrue(entity.userGroups.isEmpty())

        listOf(
            ROLE_USER_READ,
            ROLE_GROUP_READ,
        ).forEach { role ->
            assertTrue(entity.userRoles.any { it == role })
        }

    }

    @Test
    fun `One with group list`() {
        insertData("user-one-insert")
        val entity = repository.one(fromString("5f5be08f-aed5-42f2-bbc5-e55bff4528af"))
        assertNotNull(entity)
        assertEquals("Natasha", entity!!.firstName)
        assertEquals("Romanoff", entity.lastName)
        assertEquals("black.widow@avengers.com", entity.mainEmail)
        assertEquals("93552381007", entity.documentNumber)
        assertEquals("12974183652", entity.phoneNumber)
        assertEquals(LocalDate.of(1992, 6, 30), entity.birthDate)
        assertEquals(CIS_FEMALE, entity.userGender)
        assertTrue(entity.isActive)
        assertTrue(entity.userRoles.isEmpty())

        listOf(
            fromString("dc25382c-6fe3-4ee5-a12e-59c8fd3b5442"),
            fromString("b63a98f7-f111-4171-98cb-3f9e25a80890"),
        ).forEach { groupId ->
            assertTrue(entity.userGroups.any { it.id == groupId })
        }
    }

    @Test
    fun `One without role and group list`() {
        insertData("user-one-insert")
        val entity = repository.one(fromString("33a7c837-92f3-4a8d-b6ba-1ad75b5f0656"))
        assertNotNull(entity)
        assertEquals("Bruce", entity!!.firstName)
        assertEquals("Banner", entity.lastName)
        assertEquals("the.hulk@avengers.com", entity.mainEmail)
        assertEquals("05564611004", entity.documentNumber)
        assertEquals("10901234567", entity.phoneNumber)
        assertEquals(LocalDate.of(1993, 7, 15), entity.birthDate)
        assertEquals(CIS_MALE, entity.userGender)
        assertTrue(entity.isActive)
        assertTrue(entity.userRoles.isEmpty())
        assertTrue(entity.userGroups.isEmpty())
    }

    @Test
    fun `One inexistent`() {
        insertData("user-one-insert")
        val entity = repository.one(randomUUID())
        assertNull(entity)
    }

    @Test
    fun `Different user with same email true`() {
        insertData("user-one-insert")
        assertTrue(repository.hasAnotherWithSameMainEmail(randomUUID(), "iron.man@avengers.com"))
    }

    @Test
    fun `Different user with same email false`() {
        insertData("user-one-insert")
        assertFalse(repository.hasAnotherWithSameMainEmail(randomUUID(), "charles.xavier@xmen.com"))
    }

    @Test
    fun `Same user with same email false`() {
        insertData("user-one-insert")
        assertFalse(repository.hasAnotherWithSameMainEmail(fromString("436d12f3-d23e-4346-b847-37188521a968"), "iron.man@avengers.com"))
    }

    @Test
    fun `Different user with same document true`() {
        insertData("user-one-insert")
        assertTrue(repository.hasAnotherWithSameDocumentNumber(randomUUID(), "10775576042"))
    }

    @Test
    fun `Different user with same document false`() {
        insertData("user-one-insert")
        assertFalse(repository.hasAnotherWithSameDocumentNumber(randomUUID(), "79569665076"))
    }

    @Test
    fun `Same user with same document false`() {
        insertData("user-one-insert")
        assertFalse(repository.hasAnotherWithSameDocumentNumber(fromString("436d12f3-d23e-4346-b847-37188521a968"), "10775576042"))
    }

    @Test
    fun `Different user with same phone true`() {
        insertData("user-one-insert")
        assertTrue(repository.hasAnotherWithSamePhoneNumber(randomUUID(), "16988776655"))
    }

    @Test
    fun `Different user with same phone false`() {
        insertData("user-one-insert")
        assertFalse(repository.hasAnotherWithSamePhoneNumber(randomUUID(), "16952524545"))
    }

    @Test
    fun `Same user with same phone false`() {
        insertData("user-one-insert")
        assertFalse(repository.hasAnotherWithSamePhoneNumber(fromString("436d12f3-d23e-4346-b847-37188521a968"), "16988776655"))
    }

    @Test
    fun `Create user with all data`() {
        insertData("user-one-insert")
        userUpsert.apply {
            repository.create(this)
            val entity = transaction(database) {
                UserExposedEntity.findById(this@apply.id)?.toUserCompleteEntity()
            }

            assertNotNull(entity)
            assertEquals(this.firstName, entity!!.firstName)
            assertEquals(this.lastName, entity.lastName)
            assertEquals(this.mainEmail, entity.mainEmail)
            assertEquals(this.documentNumber, entity.documentNumber)
            assertEquals(this.phoneNumber, entity.phoneNumber)
            assertEquals(this.birthDate, entity.birthDate)
            assertEquals(this.userGender, entity.userGender)
            assertEquals(this.isActive, entity.isActive)

            this.userRoles.forEach { role ->
                assertTrue(entity.userRoles.any { it == role })
            }

            this.userGroups.forEach { group ->
                assertTrue(entity.userGroups.any { it.id == group.id })
            }
        }
    }

    @Test
    fun `Signup user`() {
        insertData("user-one-insert")
        userSignup.apply {
            repository.signup(this)
            val entity = transaction(database) {
                UserExposedEntity.findById(this@apply.id)?.toUserCompleteEntity()
            }

            assertNotNull(entity)
            assertEquals(this.firstName, entity!!.firstName)
            assertEquals(this.lastName, entity.lastName)
            assertEquals(this.mainEmail, entity.mainEmail)
            assertEquals(this.documentNumber, entity.documentNumber)
            assertEquals(this.phoneNumber, entity.phoneNumber)
            assertEquals(this.birthDate, entity.birthDate)
            assertEquals(this.userGender, entity.userGender)
            assertEquals(this.isActive, entity.isActive)
            assertTrue(entity.userRoles.isEmpty())
            assertTrue(entity.userGroups.isEmpty())
        }
    }

    @Test
    fun `Create user with nullable fields null`() {
        insertData("user-one-insert")
        userUpsert.copy(
            phoneNumber = null,
            profilePhoto = null,
            birthDate = null,
            userGender = null,
            userRoles = setOf(),
            userGroups = setOf(),
        ).apply {
            repository.create(this)
            val entity = transaction(database) {
                UserExposedEntity.findById(this@apply.id)?.toUserCompleteEntity()
            }

            assertNotNull(entity)
            assertEquals(this.firstName, entity!!.firstName)
            assertEquals(this.lastName, entity.lastName)
            assertEquals(this.mainEmail, entity.mainEmail)
            assertEquals(this.documentNumber, entity.documentNumber)
            assertEquals(this.phoneNumber, entity.phoneNumber)
            assertEquals(this.birthDate, entity.birthDate)
            assertEquals(this.userGender, entity.userGender)
            assertEquals(this.isActive, entity.isActive)

            this.userRoles.forEach { role ->
                assertTrue(entity.userRoles.any { it == role })
            }

            this.userGroups.forEach { group ->
                assertTrue(entity.userGroups.any { it.id == group.id })
            }
        }
    }

    @Test
    fun `Update user with all data`() {
        insertData("user-one-insert")
        userUpsert.copy(
            id = fromString("33a7c837-92f3-4a8d-b6ba-1ad75b5f0656"),
            creatorId = fromString("33a7c837-92f3-4a8d-b6ba-1ad75b5f0656"),
            phoneNumber = null,
            profilePhoto = null,
            birthDate = null,
            userGender = null,
            userRoles = setOf(),
            userGroups = setOf(),
        ).apply {
            repository.update(this)
            val entity = transaction(database) {
                UserExposedEntity.findById(this@apply.id)?.toUserCompleteEntity()
            }

            assertNotNull(entity)
            assertEquals(this.firstName, entity!!.firstName)
            assertEquals(this.lastName, entity.lastName)
            assertEquals(this.mainEmail, entity.mainEmail)
            assertEquals(this.documentNumber, entity.documentNumber)
            assertEquals(this.phoneNumber, entity.phoneNumber)
            assertEquals(this.birthDate, entity.birthDate)
            assertEquals(this.userGender, entity.userGender)
            assertEquals(this.isActive, entity.isActive)

            this.userRoles.forEach { role ->
                assertTrue(entity.userRoles.any { it == role })
            }

            this.userGroups.forEach { group ->
                assertTrue(entity.userGroups.any { it.id == group.id })
            }
        }
    }

    @Test
    fun `Update user with nullable fields not null then null`() {
        insertData("user-one-insert")
        userUpsert.copy(
            id = fromString("436d12f3-d23e-4346-b847-37188521a968"),
            creatorId = fromString("436d12f3-d23e-4346-b847-37188521a968"),
        ).apply {
            repository.update(this)
            val entity = transaction(database) {
                UserExposedEntity.findById(this@apply.id)?.toUserCompleteEntity()
            }

            assertNotNull(entity)
            assertEquals(this.firstName, entity!!.firstName)
            assertEquals(this.lastName, entity.lastName)
            assertEquals(this.mainEmail, entity.mainEmail)
            assertEquals(this.documentNumber, entity.documentNumber)
            assertEquals(this.phoneNumber, entity.phoneNumber)
            assertEquals(this.birthDate, entity.birthDate)
            assertEquals(this.userGender, entity.userGender)
            assertEquals(this.isActive, entity.isActive)

            this.userRoles.forEach { role ->
                assertTrue(entity.userRoles.any { it == role })
            }

            this.userGroups.forEach { group ->
                assertTrue(entity.userGroups.any { it.id == group.id })
            }
        }
    }

    @Test
    fun `Update user with nullable fields null then not null`() {
        insertData("user-one-insert")
        userUpsert.copy(
            id = fromString("46daf531-114f-495d-83de-bcb67b4c493d"),
            creatorId = fromString("46daf531-114f-495d-83de-bcb67b4c493d"),
        ).apply {
            repository.update(this)
            val entity = transaction(database) {
                UserExposedEntity.findById(this@apply.id)?.toUserCompleteEntity()
            }

            assertNotNull(entity)
            assertEquals(this.firstName, entity!!.firstName)
            assertEquals(this.lastName, entity.lastName)
            assertEquals(this.mainEmail, entity.mainEmail)
            assertEquals(this.documentNumber, entity.documentNumber)
            assertEquals(this.phoneNumber, entity.phoneNumber)
            assertEquals(this.birthDate, entity.birthDate)
            assertEquals(this.userGender, entity.userGender)
            assertEquals(this.isActive, entity.isActive)

            this.userRoles.forEach { role ->
                assertTrue(entity.userRoles.any { it == role })
            }

            this.userGroups.forEach { group ->
                assertTrue(entity.userGroups.any { it.id == group.id })
            }
        }
    }

    @Test
    fun `Update user roles removing and adding`() {
        insertData("user-one-insert")
        transaction(database) {
            UserExposedEntity.findById(fromString("2d891ac7-8ad8-441a-9d49-ea8011ea9390"))?.toUserCompleteEntity()
        }!!.copy(
            userRoles = setOf(
                ROLE_USER_READ,
                ROLE_USER_UPDATE,
                ROLE_GROUP_READ,
                ROLE_GROUP_UPDATE,
            )
        ).apply {
            repository.update(this)
            val entity = transaction(database) {
                UserExposedEntity.findById(this@apply.id)?.toUserCompleteEntity()
            }

            assertNotNull(entity)
            assertEquals(this.firstName, entity!!.firstName)
            assertEquals(this.lastName, entity.lastName)
            assertEquals(this.mainEmail, entity.mainEmail)
            assertEquals(this.documentNumber, entity.documentNumber)
            assertEquals(this.phoneNumber, entity.phoneNumber)
            assertEquals(this.birthDate, entity.birthDate)
            assertEquals(this.userGender, entity.userGender)
            assertEquals(this.isActive, entity.isActive)

            this.userRoles.forEach { role ->
                assertTrue(entity.userRoles.any { it == role })
            }

            this.userGroups.forEach { group ->
                assertTrue(entity.userGroups.any { it.id == group.id })
            }
        }
    }

    @Test
    fun `Update user groups removing and adding`() {
        insertData("user-one-insert")
        transaction(database) {
            UserExposedEntity.findById(fromString("2d891ac7-8ad8-441a-9d49-ea8011ea9390"))?.toUserCompleteEntity()
        }!!.copy(
            userGroups = setOf(
                theAvengersGroup,
                guardiansGalaxyGroup,
            )
        ).apply {
            repository.update(this)
            val entity = transaction(database) {
                UserExposedEntity.findById(this@apply.id)?.toUserCompleteEntity()
            }

            assertNotNull(entity)
            assertEquals(this.firstName, entity!!.firstName)
            assertEquals(this.lastName, entity.lastName)
            assertEquals(this.mainEmail, entity.mainEmail)
            assertEquals(this.documentNumber, entity.documentNumber)
            assertEquals(this.phoneNumber, entity.phoneNumber)
            assertEquals(this.birthDate, entity.birthDate)
            assertEquals(this.userGender, entity.userGender)
            assertEquals(this.isActive, entity.isActive)

            this.userRoles.forEach { role ->
                assertTrue(entity.userRoles.any { it == role })
            }

            this.userGroups.forEach { group ->
                assertTrue(entity.userGroups.any { it.id == group.id })
            }
        }
    }

}
