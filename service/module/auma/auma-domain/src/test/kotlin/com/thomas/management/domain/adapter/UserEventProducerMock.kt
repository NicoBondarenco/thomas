package com.thomas.management.domain.adapter

import com.thomas.management.domain.event.UserEventProducer
import com.thomas.management.message.event.UserUpsertedEvent
import java.util.UUID

internal class UserEventProducerMock : UserEventProducer {

    private val events = mutableMapOf<UUID, UserUpsertedEvent>()

    override fun sendSignupEvent(event: UserUpsertedEvent): Boolean {
        events[event.id] = event
        return true
    }

    override fun sendCreatedEvent(event: UserUpsertedEvent): Boolean {
        events[event.id] = event
        return true
    }

    override fun sendUpdatedEvent(event: UserUpsertedEvent): Boolean {
        events[event.id] = event
        return true
    }

}
