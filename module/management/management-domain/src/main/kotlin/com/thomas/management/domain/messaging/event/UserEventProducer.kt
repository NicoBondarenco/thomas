package com.thomas.management.domain.messaging.event

import com.thomas.contract.messaging.management.user.UserCreatedEvent
import com.thomas.contract.messaging.management.user.UserUpdatedEvent

interface UserEventProducer {

    suspend fun userCreated(event: UserCreatedEvent)

    suspend fun userUpdated(event: UserUpdatedEvent)

}
