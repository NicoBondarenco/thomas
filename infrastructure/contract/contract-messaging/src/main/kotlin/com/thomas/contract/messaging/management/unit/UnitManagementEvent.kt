package com.thomas.contract.messaging.management.unit

import com.thomas.contract.messaging.ApplicationEvent
import com.thomas.contract.messaging.ApplicationEventType
import java.time.OffsetDateTime
import java.util.UUID

data class UnitManagementEvent(
    override val eventType: ApplicationEventType,
    override val eventTimestamp: OffsetDateTime,
    override val eventKey: UUID,
    override val eventData: UnitDataEvent?,
) : ApplicationEvent<UUID, UnitDataEvent?>()