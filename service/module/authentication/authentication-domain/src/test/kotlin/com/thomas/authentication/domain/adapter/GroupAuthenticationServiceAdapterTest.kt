package com.thomas.authentication.domain.adapter

import com.thomas.authentication.data.entity.GroupAuthenticationEntity
import com.thomas.authentication.data.repository.GroupAuthenticationRepository
import com.thomas.authentication.domain.GroupAuthenticationService
import com.thomas.authentication.domain.exception.GroupAuthenticationNotFoundException
import com.thomas.authentication.domain.i18n.AuthenticationDomainMessageI18N.authenticationGroupAuthenticationNotFoundErrorMessage
import com.thomas.authentication.event.groupAuthenticationEntity
import com.thomas.authentication.event.groupDeletedEvent
import com.thomas.authentication.event.groupUpsertedEvent
import com.thomas.management.message.event.GroupUpsertedEvent
import io.mockk.every
import io.mockk.mockk
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GroupAuthenticationServiceAdapterTest {

    private val groups: MutableMap<UUID, GroupAuthenticationEntity> = mutableMapOf()

    private val repository: GroupAuthenticationRepository = mockk<GroupAuthenticationRepository>().apply {
        every { one(any()) } answers {
            val id = (it.invocation.args[0] as UUID)
            groups[id]
        }

        every { create(any()) } answers {
            (it.invocation.args[0] as GroupAuthenticationEntity).apply {
                groups[this.id] = this
            }
        }

        every { update(any()) } answers {
            (it.invocation.args[0] as GroupAuthenticationEntity).apply {
                groups[this.id] = this
            }
        }

        every { delete(any()) } answers {
            val id = (it.invocation.args[0] as UUID)
            groups.remove(id)
        }

    }

    private val service: GroupAuthenticationService = GroupAuthenticationServiceAdapter(repository)

    @Test
    fun `WHEN creating group by event THEN should save the group`() {
        val event = groupUpsertedEvent
        service.create(event)

        val entity = groups[event.id]
        assertNotNull(entity)

        entity!!.assertWithEvent(event)
    }

    @Test
    fun `WHEN updating group by event THEN should update the group`() {
        val event = groupUpsertedEvent
        groups[event.id] = groupAuthenticationEntity.copy(id = event.id)
        service.update(event)

        val entity = groups[event.id]
        assertNotNull(entity)

        entity!!.assertWithEvent(event)
    }

    @Test
    fun `WHEN updating group by event AND groups is not found THEN should throws GroupAuthenticationNotFoundException`() {
        val event = groupUpsertedEvent

        val exception = assertThrows<GroupAuthenticationNotFoundException> {
            service.update(event)
        }

        assertEquals(exception.message, authenticationGroupAuthenticationNotFoundErrorMessage(event.id))
    }

    @Test
    fun `WHEN deleting a group by event THEN should remove the group`() {
        val event = groupDeletedEvent
        groups[event.id] = groupAuthenticationEntity.copy(id = event.id)

        service.delete(event)

        val entity = groups[event.id]
        assertNull(entity)
    }

    private fun GroupAuthenticationEntity.assertWithEvent(
        event: GroupUpsertedEvent,
    ) {
        assertEquals(this.groupName, event.groupName)
        assertEquals(this.groupDescription, event.groupDescription)
        assertEquals(this.isActive, event.isActive)
        assertEquals(this.createdAt, event.createdAt)
        assertEquals(this.updatedAt, event.updatedAt)
        assertEquals(this.groupRoles, event.groupRoles)
    }

}
