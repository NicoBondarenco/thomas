package com.thomas.management.domain.adapter

import com.thomas.management.domain.UserEventProducer
import com.thomas.management.message.event.UserUpsertedEvent
import java.util.UUID

internal class UserEventProducerMock : UserEventProducer {

    private val events = mutableMapOf<UUID, UserUpsertedEvent>()

    override fun sendCreatedEvent(event: UserUpsertedEvent) {
        events[event.id] = event
    }

    override fun sendUpdatedEvent(event: UserUpsertedEvent) {
        events[event.id] = event
    }

}
