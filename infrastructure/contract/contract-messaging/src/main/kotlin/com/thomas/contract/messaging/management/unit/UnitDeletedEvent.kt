package com.thomas.contract.messaging.management.unit

import java.time.OffsetDateTime
import java.util.UUID

data class UnitDeletedEvent(
    val id: UUID,
    val deletedAt: OffsetDateTime,
)
