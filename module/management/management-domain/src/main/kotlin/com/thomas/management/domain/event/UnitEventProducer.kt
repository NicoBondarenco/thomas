package com.thomas.management.domain.event

import com.thomas.management.data.entity.UnitEntity
import java.util.UUID

interface UnitEventProducer {

    suspend fun unitCreated(entity: UnitEntity)

    suspend fun unitUpdated(entity: UnitEntity)

    suspend fun unitDeleted(id: UUID)

}