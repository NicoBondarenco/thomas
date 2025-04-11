package com.thomas.management.domain.messaging.event

import com.thomas.contract.messaging.management.unit.UnitManagementEvent

interface UnitEventProducer {

    suspend fun unitCreated(event: UnitManagementEvent)

    suspend fun unitUpdated(event: UnitManagementEvent)

    suspend fun unitDeleted(event: UnitManagementEvent)

}
