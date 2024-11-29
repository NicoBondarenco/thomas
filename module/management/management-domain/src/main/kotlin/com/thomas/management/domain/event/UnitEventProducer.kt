package com.thomas.management.domain.event

import com.thomas.contract.messaging.management.unit.UnitCreatedEvent
import com.thomas.contract.messaging.management.unit.UnitDeletedEvent
import com.thomas.contract.messaging.management.unit.UnitUpdatedEvent

interface UnitEventProducer {

    suspend fun unitCreated(event: UnitCreatedEvent)

    suspend fun unitUpdated(event: UnitUpdatedEvent)

    suspend fun unitDeleted(event: UnitDeletedEvent)

}
