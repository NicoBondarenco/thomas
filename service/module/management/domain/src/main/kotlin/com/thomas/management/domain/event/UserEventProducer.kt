package com.thomas.management.domain.event

import com.thomas.management.message.event.UserUpsertedEvent

interface UserEventProducer {

    fun sendCreatedEvent(event: UserUpsertedEvent)

    fun sendUpdatedEvent(event: UserUpsertedEvent)

}
