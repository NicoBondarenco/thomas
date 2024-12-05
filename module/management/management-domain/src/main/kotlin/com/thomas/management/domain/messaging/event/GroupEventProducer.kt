package com.thomas.management.domain.messaging.event

import com.thomas.contract.messaging.management.group.GroupCreatedEvent
import com.thomas.contract.messaging.management.group.GroupDeletedEvent
import com.thomas.contract.messaging.management.group.GroupUpdatedEvent

interface GroupEventProducer {

    suspend fun groupCreated(event: GroupCreatedEvent)

    suspend fun groupUpdated(event: GroupUpdatedEvent)

    suspend fun groupDeleted(event: GroupDeletedEvent)

}
