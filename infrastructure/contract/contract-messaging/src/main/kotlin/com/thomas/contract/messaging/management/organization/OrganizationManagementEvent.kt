package com.thomas.contract.messaging.management.organization

import com.thomas.contract.messaging.ApplicationEvent
import com.thomas.contract.messaging.ApplicationEventType
import java.time.OffsetDateTime
import java.util.UUID

data class OrganizationManagementEvent(
    override val eventType: ApplicationEventType,
    override val eventTimestamp: OffsetDateTime,
    override val eventKey: UUID,
    override val eventData: OrganizationDataEvent,
) : ApplicationEvent<UUID, OrganizationDataEvent>()