package com.thomas.management.domain.adapter

import com.thomas.management.domain.event.GroupEventProducer
import com.thomas.management.message.event.GroupDeletedEvent
import com.thomas.management.message.event.GroupUpsertedEvent
import java.util.UUID

internal class GroupEventProducerMock : GroupEventProducer {

    private val upsertEvents = mutableMapOf<UUID, GroupUpsertedEvent>()
    private val deleteEvents = mutableMapOf<UUID, GroupDeletedEvent>()

    override fun sendCreatedEvent(event: GroupUpsertedEvent): Boolean {
        upsertEvents[event.id] = event
        return true
    }

    override fun sendUpdatedEvent(event: GroupUpsertedEvent): Boolean {
        upsertEvents[event.id] = event
        return true
    }

    override fun sendDeletedEvent(event: GroupDeletedEvent): Boolean {
        deleteEvents[event.id] = event
        return true
    }

}
