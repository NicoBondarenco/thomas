package com.thomas.management.domain.adapter

import com.thomas.management.domain.event.GroupEventProducer
import com.thomas.management.message.event.GroupDeletedEvent
import com.thomas.management.message.event.GroupUpsertedEvent
import java.util.UUID

internal class GroupEventProducerMock : GroupEventProducer {

    private val upsertEvents = mutableMapOf<UUID, GroupUpsertedEvent>()
    private val deleteEvents = mutableMapOf<UUID, GroupDeletedEvent>()

    override fun sendCreatedEvent(event: GroupUpsertedEvent) {
        upsertEvents[event.id] = event
    }

    override fun sendUpdatedEvent(event: GroupUpsertedEvent) {
        upsertEvents[event.id] = event
    }

    override fun sendDeletedEvent(event: GroupDeletedEvent) {
        deleteEvents[event.id] = event
    }

}
