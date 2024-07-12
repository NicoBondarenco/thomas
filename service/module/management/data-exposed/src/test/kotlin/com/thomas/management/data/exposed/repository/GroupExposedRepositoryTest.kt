package com.thomas.management.data.exposed.repository

import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection.ASC
import com.thomas.core.model.pagination.PageSortDirection.DESC
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_READ
import com.thomas.core.model.security.SecurityRole.ROLE_GROUP_UPDATE
import com.thomas.core.model.security.SecurityRole.ROLE_USER_CREATE
import com.thomas.core.model.security.SecurityRole.ROLE_USER_READ
import com.thomas.core.model.security.SecurityRole.ROLE_USER_UPDATE
import com.thomas.management.data.exposed.extension.groupUpsert
import com.thomas.management.data.exposed.extension.toGroupCompleteEntity
import com.thomas.management.data.exposed.mapping.GroupExposedEntity
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

class GroupExposedRepositoryTest : BaseExposedRepositoryTest() {

    private lateinit var repository: GroupExposedRepository

    override fun configureBeforeAll() {
        repository = GroupExposedRepository(database)
    }

    @Test
    fun `Page without filter and order`() {
        insertData("group-page-insert")
        val page = repository.page(pageable = PageRequest())
        assertEquals(10, page.contentList.size)
        assertEquals(45, page.totalItems)
        assertEquals(5, page.totalPages)
        assertTrue(page.sortedBy.isEmpty())
    }

    @Test
    fun `Page with order`() {
        insertData("group-page-insert")
        val page = repository.page(pageable = PageRequest(pageSort = listOf(PageSort("updated_at", DESC))))
        assertEquals(10, page.contentList.size)
        assertEquals(45, page.totalItems)
        assertEquals(5, page.totalPages)
        assertEquals(1, page.sortedBy.size)
        assertEquals("updated_at", page.sortedBy.first().sortField)
        assertEquals(DESC, page.sortedBy.first().sortDirection)
        listOf(
            fromString("b2abc97e-e90b-46b2-af2f-6c5667447b70"),
            fromString("7e0ca235-4ab2-4906-a47d-ff668fd0f7c3"),
            fromString("bdd28f05-cab5-41e1-b9cc-edf74e0c14d3"),
            fromString("3c0d2f68-9de6-4427-bb27-4261416ae99f"),
            fromString("2af0aafe-ce12-4ef6-a92e-0884fa85cedf"),
            fromString("a1728bca-267b-4f79-819e-3ed4a8daf7a5"),
            fromString("2f9d857c-5d23-4d45-b817-c1dccd5cc9c9"),
            fromString("b08f9651-8c2f-416a-a5f2-0687d6642fa6"),
            fromString("b72d6443-b331-4de1-a9aa-e489659b0e8b"),
            fromString("d6f1e3f0-a324-4dba-a73d-2651fdf737d7"),
        ).forEach { id ->
            assertTrue(page.contentList.any { it.id == id })
        }
    }

    @Test
    fun `Page with filter keyword`() {
        insertData("group-page-insert")
        val page = repository.page(
            keywordText = "ger",
            pageable = PageRequest(),
        )
        assertEquals(6, page.contentList.size)
        assertEquals(6, page.totalItems)
        assertEquals(1, page.totalPages)
        assertTrue(page.sortedBy.isEmpty())
        listOf(
            fromString("591ec026-dca7-483c-a8be-af95bfc8dafc"),
            fromString("c7330798-0c3f-49aa-8a12-d94f6a33084d"),
            fromString("0d812141-87fd-4c92-839e-733975fa599a"),
            fromString("2f9d857c-5d23-4d45-b817-c1dccd5cc9c9"),
            fromString("d01d802a-c19d-401f-af03-9b227ab1bd9f"),
            fromString("d6f1e3f0-a324-4dba-a73d-2651fdf737d7"),
        ).forEach { id ->
            assertTrue(page.contentList.any { it.id == id })
        }
    }

    @Test
    fun `Page with filter is active`() {
        insertData("group-page-insert")
        val page = repository.page(
            isActive = true,
            pageable = PageRequest(
                pageNumber = 2,
                pageSize = 5,
                pageSort = listOf(PageSort("created_at", DESC))
            ),
        )
        assertEquals(5, page.contentList.size)
        assertEquals(25, page.totalItems)
        assertEquals(5, page.totalPages)
        assertEquals(1, page.sortedBy.size)
        assertEquals("created_at", page.sortedBy.first().sortField)
        assertEquals(DESC, page.sortedBy.first().sortDirection)
        listOf(
            fromString("b72d6443-b331-4de1-a9aa-e489659b0e8b"),
            fromString("16fbdc96-85b1-41f2-84ba-7fd5ca4225b2"),
            fromString("fc320435-48d1-4c01-9fa1-60f865495043"),
            fromString("637a37b4-47fb-4840-8a0d-1f649c080737"),
            fromString("f8cb77d0-c324-4fd0-afcb-c04d916dc90c"),
        ).forEach { id ->
            assertTrue(page.contentList.any { it.id == id })
        }
    }

    @Test
    fun `Page with filter created start`() {
        insertData("group-page-insert")
        val page = repository.page(
            createdStart = OffsetDateTime.of(2024, 8, 1, 1, 48, 17, 683000, UTC),
            pageable = PageRequest(
                pageNumber = 3,
                pageSize = 3,
                pageSort = listOf(PageSort("group_name", ASC))
            ),
        )
        assertEquals(3, page.contentList.size)
        assertEquals(20, page.totalItems)
        assertEquals(7, page.totalPages)
        assertEquals(1, page.sortedBy.size)
        assertEquals("group_name", page.sortedBy.first().sortField)
        assertEquals(ASC, page.sortedBy.first().sortDirection)
        listOf(
            fromString("591ec026-dca7-483c-a8be-af95bfc8dafc"),
            fromString("2f9d857c-5d23-4d45-b817-c1dccd5cc9c9"),
            fromString("eb781cdb-9907-4699-bcfa-66e2037c0fcc"),
        ).forEach { id ->
            assertTrue(page.contentList.any { it.id == id })
        }
    }

    @Test
    fun `Page with filter created end`() {
        insertData("group-page-insert")
        val page = repository.page(
            createdEnd = OffsetDateTime.of(2024, 3, 26, 12, 52, 23, 623000000, UTC),
            pageable = PageRequest(
                pageNumber = 2,
                pageSize = 5,
                pageSort = listOf(PageSort("group_name", DESC))
            ),
        )
        assertEquals(4, page.contentList.size)
        assertEquals(9, page.totalItems)
        assertEquals(2, page.totalPages)
        assertEquals(1, page.sortedBy.size)
        assertEquals("group_name", page.sortedBy.first().sortField)
        assertEquals(DESC, page.sortedBy.first().sortDirection)
        listOf(
            fromString("56adc836-3243-4df2-857e-4185edb4e625"),
            fromString("c7330798-0c3f-49aa-8a12-d94f6a33084d"),
            fromString("948a69a6-be75-4a0f-bff1-33195dc12794"),
            fromString("939488b4-6fc4-49cd-96d7-2197c7bd1205"),
        ).forEach { id ->
            assertTrue(page.contentList.any { it.id == id })
        }
    }

    @Test
    fun `Page with filter update start`() {
        insertData("group-page-insert")
        val page = repository.page(
            updatedStart = OffsetDateTime.of(2024, 8, 17, 4, 42, 28, 925059000, UTC),
            pageable = PageRequest(
                pageNumber = 3,
                pageSize = 3,
                pageSort = listOf(PageSort("group_name", DESC))
            ),
        )
        assertEquals(3, page.contentList.size)
        assertEquals(17, page.totalItems)
        assertEquals(6, page.totalPages)
        assertEquals(1, page.sortedBy.size)
        assertEquals("group_name", page.sortedBy[0].sortField)
        assertEquals(DESC, page.sortedBy[0].sortDirection)
        listOf(
            fromString("bdd28f05-cab5-41e1-b9cc-edf74e0c14d3"),
            fromString("b08f9651-8c2f-416a-a5f2-0687d6642fa6"),
            fromString("fc320435-48d1-4c01-9fa1-60f865495043"),
        ).forEach { id ->
            assertTrue(page.contentList.any { it.id == id })
        }
    }

    @Test
    fun `Page with filter updated end`() {
        insertData("group-page-insert")
        val page = repository.page(
            updatedEnd = OffsetDateTime.of(2024, 3, 11, 5, 55, 57, 154768000, UTC),
            pageable = PageRequest(
                pageNumber = 2,
                pageSize = 5,
                pageSort = listOf(PageSort("group_name", DESC))
            ),
        )
        assertEquals(3, page.contentList.size)
        assertEquals(8, page.totalItems)
        assertEquals(2, page.totalPages)
        assertEquals(1, page.sortedBy.size)
        assertEquals("group_name", page.sortedBy[0].sortField)
        assertEquals(DESC, page.sortedBy[0].sortDirection)
        listOf(
            fromString("c7330798-0c3f-49aa-8a12-d94f6a33084d"),
            fromString("948a69a6-be75-4a0f-bff1-33195dc12794"),
            fromString("939488b4-6fc4-49cd-96d7-2197c7bd1205"),
        ).forEach { id ->
            assertTrue(page.contentList.any { it.id == id })
        }
    }

    @Test
    fun `Page with filter created range`() {
        insertData("group-page-insert")
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
            fromString("245d5a56-5f6f-415e-b362-a1f4249d407d"),
            fromString("14e0794f-2eab-48fd-b1e8-fe7dba869feb"),
            fromString("4dfbd699-67a4-480c-8ce8-66de287361c4"),
            fromString("6098701c-e36a-4c50-8b23-338411ba8808"),
        ).forEach { id ->
            assertTrue(page.contentList.any { it.id == id })
        }
    }

    @Test
    fun `Page with filter updated range`() {
        insertData("group-page-insert")
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
            fromString("b08f9651-8c2f-416a-a5f2-0687d6642fa6"),
            fromString("b72d6443-b331-4de1-a9aa-e489659b0e8b"),
            fromString("d6f1e3f0-a324-4dba-a73d-2651fdf737d7"),
            fromString("d35aedb5-27d1-446b-8cbc-a1e4395c0ff1"),
            fromString("16fbdc96-85b1-41f2-84ba-7fd5ca4225b2"),
            fromString("fc320435-48d1-4c01-9fa1-60f865495043"),
        ).forEach { id ->
            assertTrue(page.contentList.any { it.id == id })
        }
    }

    @Test
    fun `One with role list`() {
        insertData("group-one-insert")
        val entity = repository.one(fromString("dc25382c-6fe3-4ee5-a12e-59c8fd3b5442"))
        assertNotNull(entity)
        assertEquals("The Avengers", entity!!.groupName)
        assertEquals("The Avengers - Protectors of the Earth", entity.groupDescription)
        assertTrue(entity.isActive)

        listOf(
            ROLE_USER_READ,
            ROLE_GROUP_READ,
        ).forEach { role ->
            assertTrue(entity.groupRoles.any { it == role })
        }

    }

    @Test
    fun `One without role`() {
        insertData("group-one-insert")
        val entity = repository.one(fromString("1f85e146-32ac-4fdd-a3ae-e97a55978c99"))
        assertNotNull(entity)
        assertEquals("Team Iron Man", entity!!.groupName)
        assertNull(entity.groupDescription)
        assertTrue(entity.isActive)
        assertTrue(entity.groupRoles.isEmpty())
    }

    @Test
    fun `One inexistent`() {
        insertData("group-one-insert")
        val entity = repository.one(randomUUID())
        assertNull(entity)
    }

    @Test
    fun `Different group with same name true`() {
        insertData("group-one-insert")
        assertTrue(repository.hasAnotherWithSameGroupName(randomUUID(), "The Avengers"))
    }

    @Test
    fun `Different group with same name false`() {
        insertData("group-one-insert")
        assertFalse(repository.hasAnotherWithSameGroupName(randomUUID(), "Fantastic Four"))
    }

    @Test
    fun `Same group with same name false`() {
        insertData("group-one-insert")
        assertFalse(repository.hasAnotherWithSameGroupName(fromString("dc25382c-6fe3-4ee5-a12e-59c8fd3b5442"), "The Avengers"))
    }

    @Test
    fun `Create group with all data`() {
        insertData("group-one-insert")
        groupUpsert.copy(
            groupDescription = "Group Description",
        ).apply {
            repository.create(this)
            val entity = transaction(database) {
                GroupExposedEntity.findById(this@apply.id)?.toGroupCompleteEntity()
            }

            assertNotNull(entity)
            assertEquals(this.groupName, entity!!.groupName)
            assertEquals(this.groupDescription, entity.groupDescription)
            assertEquals(this.isActive, entity.isActive)

            this.groupRoles.forEach { role ->
                assertTrue(entity.groupRoles.any { it == role })
            }
        }
    }

    @Test
    fun `Create group with nullable fields null`() {
        insertData("group-one-insert")
        groupUpsert.copy(
            groupDescription = null,
            groupRoles = setOf()
        ).apply {
            repository.create(this)
            val entity = transaction(database) {
                GroupExposedEntity.findById(this@apply.id)?.toGroupCompleteEntity()
            }

            assertNotNull(entity)
            assertEquals(this.groupName, entity!!.groupName)
            assertEquals(this.groupDescription, entity.groupDescription)
            assertEquals(this.isActive, entity.isActive)
            assertTrue(entity.groupRoles.isEmpty())
        }
    }

    @Test
    fun `Update group with all data`() {
        insertData("group-one-insert")
        groupUpsert.copy(
            id = fromString("096e5d76-b40b-4668-9515-68963d0db7ca"),
            groupDescription = "Group Description",
        ).apply {
            repository.update(this)
            val entity = transaction(database) {
                GroupExposedEntity.findById(this@apply.id)?.toGroupCompleteEntity()
            }

            assertNotNull(entity)
            assertEquals(this.groupName, entity!!.groupName)
            assertEquals(this.groupDescription, entity.groupDescription)
            assertEquals(this.isActive, entity.isActive)

            this.groupRoles.forEach { role ->
                assertTrue(entity.groupRoles.any { it == role })
            }
        }
    }

    @Test
    fun `Update group with nullable fields not null then null`() {
        insertData("group-one-insert")
        groupUpsert.copy(
            id = fromString("096e5d76-b40b-4668-9515-68963d0db7ca"),
            groupDescription = null,
            groupRoles = setOf()

        ).apply {
            repository.update(this)
            val entity = transaction(database) {
                GroupExposedEntity.findById(this@apply.id)?.toGroupCompleteEntity()
            }

            assertNotNull(entity)
            assertEquals(this.groupName, entity!!.groupName)
            assertEquals(this.groupDescription, entity.groupDescription)
            assertEquals(this.isActive, entity.isActive)
            assertTrue(entity.groupRoles.isEmpty())
        }
    }

    @Test
    fun `Update group with nullable fields null then not null`() {
        insertData("group-one-insert")
        groupUpsert.copy(
            id = fromString("1f85e146-32ac-4fdd-a3ae-e97a55978c99"),
            groupDescription = "Group Description",
            groupRoles = setOf(
                ROLE_USER_READ,
                ROLE_USER_CREATE,
                ROLE_USER_UPDATE,
            )
        ).apply {
            repository.update(this)
            val entity = transaction(database) {
                GroupExposedEntity.findById(this@apply.id)?.toGroupCompleteEntity()
            }

            assertNotNull(entity)
            assertEquals(this.groupName, entity!!.groupName)
            assertEquals(this.groupDescription, entity.groupDescription)
            assertEquals(this.isActive, entity.isActive)

            this.groupRoles.forEach { role ->
                assertTrue(entity.groupRoles.any { it == role })
            }
        }
    }

    @Test
    fun `Update group roles removing and adding`() {
        insertData("group-one-insert")
        transaction(database) {
            GroupExposedEntity.findById(fromString("b63a98f7-f111-4171-98cb-3f9e25a80890"))?.toGroupCompleteEntity()
        }!!.copy(
            groupRoles = setOf(
                ROLE_USER_READ,
                ROLE_USER_UPDATE,
                ROLE_GROUP_READ,
                ROLE_GROUP_UPDATE,
            )
        ).apply {
            repository.update(this)
            val entity = transaction(database) {
                GroupExposedEntity.findById(this@apply.id)?.toGroupCompleteEntity()
            }

            assertNotNull(entity)
            assertEquals(this.groupName, entity!!.groupName)
            assertEquals(this.groupDescription, entity.groupDescription)
            assertEquals(this.isActive, entity.isActive)

            this.groupRoles.forEach { role ->
                assertTrue(entity.groupRoles.any { it == role })
            }
        }
    }

}
