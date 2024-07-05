package com.thomas.management.domain.adapter

import com.thomas.core.authorization.UnauthorizedUserException
import com.thomas.core.context.SessionContextHolder.clearContext
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.entity.EntityValidationException
import com.thomas.core.model.pagination.PageRequest
import com.thomas.management.context.userWithGroupCreateRole
import com.thomas.management.context.userWithGroupDeleteRole
import com.thomas.management.context.userWithGroupReadRole
import com.thomas.management.context.userWithGroupUpdateRole
import com.thomas.management.context.userWithoutRole
import com.thomas.management.data.entity.GroupEntity
import com.thomas.management.data.repository.GroupRepositoryMock
import com.thomas.management.domain.GroupService
import com.thomas.management.domain.exception.GroupNotFoundException
import com.thomas.management.domain.exception.UserNotFoundException
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementGroupValidationGroupNameAlreadyUsed
import com.thomas.management.requests.activeGroupRequest
import com.thomas.management.requests.createGroupRequest
import com.thomas.management.requests.toGroupUpdateRequest
import com.thomas.management.requests.updateGroupRequest
import io.mockk.spyk
import io.mockk.verify
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.anyOrNull

class GroupServiceAdapterTest {

    private val groupRepository = spyk(GroupRepositoryMock())
    private val groupProducer = spyk(GroupEventProducerMock())
    private val service: GroupService = GroupServiceAdapter(
        groupRepository = groupRepository,
        eventProducer = groupProducer,
    )

    @BeforeEach
    fun setup() {
        clearContext()
        groupRepository.clear()
    }

    @Test
    fun `WHEN search groups page without permission SHOULD throws UnauthorizedUserException`() {
        currentUser = userWithoutRole
        assertThrows<UnauthorizedUserException> {
            service.page(pageable = PageRequest())
        }
        verify(exactly = 0) {
            groupRepository.page(anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), any())
        }
    }

    @Test
    fun `WHEN search groups page with ROLE_GROUP_READ permission SHOULD returns the result page`() {
        currentUser = userWithGroupReadRole
        groupRepository.generateGroups(10)
        service.page(pageable = PageRequest())
        verify(exactly = 1) {
            groupRepository.page(null, null, null, null, null, null, any())
        }
    }

    @Test
    fun `WHEN search one group without permission SHOULD throws UnauthorizedUserException`() {
        currentUser = userWithoutRole
        assertThrows<UnauthorizedUserException> {
            service.one(UUID.randomUUID())
        }
        verify(exactly = 0) { groupRepository.findById(anyOrNull()) }
    }

    @Test
    fun `WHEN search one group with ROLE_GROUP_READ permission AND group is not found SHOULD throws GroupNotFoundException`() {
        groupRepository.generateGroups(10)
        currentUser = userWithGroupReadRole
        assertThrows<GroupNotFoundException> {
            service.one(UUID.randomUUID())
        }
        verify(exactly = 1) { groupRepository.findById(any()) }
    }

    @Test
    fun `WHEN search one group with ROLE_GROUP_READ permission AND group exists SHOULD returns the group data`() {
        val groups = groupRepository.generateGroups(10)
        currentUser = userWithGroupReadRole
        service.one(groups.random().id)
        verify(exactly = 1) { groupRepository.findById(any()) }
    }

    @Test
    fun `WHEN create group without permission SHOULD throws UnauthorizedUserException`() {
        currentUser = userWithoutRole
        assertThrows<UnauthorizedUserException> {
            service.create(createGroupRequest)
        }
        verify(exactly = 0) { groupRepository.create(any()) }
        verify(exactly = 0) { groupProducer.sendCreatedEvent(any()) }
    }

    @Test
    fun `WHEN create group with ROLE_GROUP_CREATE permission SHOULD save and return the group`() {
        currentUser = userWithGroupCreateRole
        service.create(createGroupRequest)
        verify(exactly = 1) { groupRepository.create(any()) }
        verify(exactly = 1) { groupProducer.sendCreatedEvent(any()) }
    }

    @Test
    fun `WHEN create group without optional data SHOULD save and return the group`() {
        currentUser = userWithGroupCreateRole
        service.create(
            createGroupRequest.copy(
                groupDescription = null,
            )
        )
        verify(exactly = 1) { groupRepository.create(any()) }
        verify(exactly = 1) { groupProducer.sendCreatedEvent(any()) }
    }

    @Test
    fun `WHEN create group with already used name SHOULD throws EntityValidationException`() {
        val errorField = GroupEntity::groupName.name.toSnakeCase()
        val groups = groupRepository.generateGroups(10)
        currentUser = userWithGroupCreateRole
        val exception = assertThrows<EntityValidationException> {
            service.create(createGroupRequest.copy(groupName = groups.random().groupName))
        }
        assertEquals(1, exception.errors.size)
        assertTrue(exception.errors.containsKey(errorField))
        assertEquals(managementGroupValidationGroupNameAlreadyUsed(), exception.errors[errorField]!!.first())
        verify(exactly = 0) { groupRepository.create(any()) }
        verify(exactly = 0) { groupProducer.sendCreatedEvent(any()) }
    }

    @Test
    fun `WHEN update group without permission SHOULD throws UnauthorizedUserException`() {
        currentUser = userWithoutRole
        assertThrows<UnauthorizedUserException> {
            service.update(UUID.randomUUID(), updateGroupRequest)
        }
        verify(exactly = 0) { groupRepository.update(any()) }
        verify(exactly = 0) { groupProducer.sendUpdatedEvent(any()) }
    }

    @Test
    fun `WHEN update group with ROLE_GROUP_UPDATE permission SHOULD update and return the group`() {
        currentUser = userWithGroupUpdateRole
        val groups = groupRepository.generateGroups(10)
        service.update(groups.random().id, updateGroupRequest)
        verify(exactly = 1) { groupRepository.update(any()) }
        verify(exactly = 1) { groupProducer.sendUpdatedEvent(any()) }
    }

    @Test
    fun `WHEN update group without optional data SHOULD update and return the group`() {
        currentUser = userWithGroupUpdateRole
        val groups = groupRepository.generateGroups(10)
        service.update(
            groups.random().id,
            updateGroupRequest.copy(
                groupDescription = null,
            )
        )
        verify(exactly = 1) { groupRepository.update(any()) }
        verify(exactly = 1) { groupProducer.sendUpdatedEvent(any()) }
    }

    @Test
    fun `WHEN update group without updating data SHOULD update and return the group`() {
        currentUser = userWithGroupUpdateRole
        val groups = groupRepository.generateGroups(10)
        groups.random().apply {
            service.update(
                this.id,
                this.toGroupUpdateRequest()
            )
        }
        verify(exactly = 1) { groupRepository.update(any()) }
        verify(exactly = 1) { groupProducer.sendUpdatedEvent(any()) }
    }

    @Test
    fun `WHEN update group with already used name SHOULD throws EntityValidationException`() {
        val errorField = GroupEntity::groupName.name.toSnakeCase()
        val groups = groupRepository.generateGroups(10)
        currentUser = userWithGroupUpdateRole
        val id = groups.random().id
        val exception = assertThrows<EntityValidationException> {
            service.update(
                id,
                groups.filter {
                    it.id != id
                }.random().let {
                    updateGroupRequest.copy(groupName = it.groupName)
                }
            )
        }
        assertEquals(1, exception.errors.size)
        assertTrue(exception.errors.containsKey(errorField))
        assertEquals(managementGroupValidationGroupNameAlreadyUsed(), exception.errors[errorField]!!.first())
        verify(exactly = 0) { groupRepository.update(any()) }
        verify(exactly = 0) { groupProducer.sendUpdatedEvent(any()) }
    }

    @Test
    fun `WHEN set active group without permission SHOULD throws UnauthorizedUserException`() {
        currentUser = userWithoutRole
        assertThrows<UnauthorizedUserException> {
            service.active(UUID.randomUUID(), activeGroupRequest)
        }
        verify(exactly = 0) { groupRepository.update(any()) }
        verify(exactly = 0) { groupProducer.sendUpdatedEvent(any()) }
    }

    @Test
    fun `WHEN set active group with ROLE_GROUP_UPDATE permission SHOULD update and return the group`() {
        currentUser = userWithGroupUpdateRole
        val groups = groupRepository.generateGroups(10)
        service.active(groups.random().id, activeGroupRequest)
        verify(exactly = 1) { groupRepository.update(any()) }
        verify(exactly = 1) { groupProducer.sendUpdatedEvent(any()) }
    }

    @Test
    fun `WHEN set active group AND group is not found SHOULD throws GroupNotFoundException`() {
        currentUser = userWithGroupUpdateRole
        assertThrows<GroupNotFoundException> {
            service.active(UUID.randomUUID(), activeGroupRequest)
        }
        verify(exactly = 0) { groupRepository.update(any()) }
        verify(exactly = 0) { groupProducer.sendUpdatedEvent(any()) }
    }

    @Test
    fun `WHEN delete group without permission SHOULD throws UnauthorizedUserException`() {
        currentUser = userWithoutRole
        assertThrows<UnauthorizedUserException> {
            service.delete(UUID.randomUUID())
        }
        verify(exactly = 0) { groupRepository.delete(any()) }
        verify(exactly = 0) { groupProducer.sendDeletedEvent(any()) }
    }

    @Test
    fun `WHEN delete group with ROLE_GROUP_DELETE permission SHOULD update and return the group`() {
        currentUser = userWithGroupDeleteRole
        val groups = groupRepository.generateGroups(10)
        service.delete(groups.random().id)
        verify(exactly = 1) { groupRepository.delete(any()) }
        verify(exactly = 1) { groupProducer.sendDeletedEvent(any()) }
    }

    @Test
    fun `WHEN delete group AND group is not found SHOULD throws GroupNotFoundException`() {
        currentUser = userWithGroupDeleteRole
        assertThrows<GroupNotFoundException> {
            service.delete(UUID.randomUUID())
        }
        verify(exactly = 0) { groupRepository.delete(any()) }
        verify(exactly = 0) { groupProducer.sendDeletedEvent(any()) }
    }

}
