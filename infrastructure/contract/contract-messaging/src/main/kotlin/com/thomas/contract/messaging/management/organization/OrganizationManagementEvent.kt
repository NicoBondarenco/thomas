package com.thomas.contract.messaging.management.organization

import com.thomas.contract.messaging.management.ManagementEvent
import com.thomas.contract.messaging.management.ManagementEventType
import java.time.OffsetDateTime
import java.util.UUID

data class OrganizationManagementEvent(
    override val eventType: ManagementEventType,
    override val eventTimestamp: OffsetDateTime,
    override val eventKey: UUID,
    override val eventData: OrganizationDataEvent,
) : ManagementEvent<UUID, OrganizationDataEvent>()