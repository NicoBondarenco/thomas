package com.thomas.management.domain

import com.thomas.management.message.event.UserUpsertedEvent

interface UserEventProducer {

    fun sendCreatedEvent(event: UserUpsertedEvent)

    fun sendUpdatedEvent(event: UserUpsertedEvent)

}
