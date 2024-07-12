package com.thomas.management.domain.event

import com.thomas.management.message.event.GroupDeletedEvent
import com.thomas.management.message.event.GroupUpsertedEvent

interface GroupEventProducer {

    fun sendCreatedEvent(event: GroupUpsertedEvent)

    fun sendUpdatedEvent(event: GroupUpsertedEvent)

    fun sendDeletedEvent(event: GroupDeletedEvent)

}
