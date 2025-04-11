package com.thomas.contract.messaging.management.unit

import com.thomas.contract.messaging.management.ManagementEvent
import com.thomas.contract.messaging.management.ManagementEventType
import java.time.OffsetDateTime
import java.util.UUID

data class UnitManagementEvent(
    override val eventType: ManagementEventType,
    override val eventTimestamp: OffsetDateTime,
    override val eventKey: UUID,
    override val eventData: UnitDataEvent?,
) : ManagementEvent<UUID, UnitDataEvent?>()