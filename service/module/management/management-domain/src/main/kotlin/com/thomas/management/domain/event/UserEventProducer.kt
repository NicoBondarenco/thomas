package com.thomas.management.domain.event

import com.thomas.management.message.event.UserUpsertedEvent

interface UserEventProducer {

    fun sendSignupEvent(event: UserUpsertedEvent): Boolean

    fun sendCreatedEvent(event: UserUpsertedEvent): Boolean

    fun sendUpdatedEvent(event: UserUpsertedEvent): Boolean

}
