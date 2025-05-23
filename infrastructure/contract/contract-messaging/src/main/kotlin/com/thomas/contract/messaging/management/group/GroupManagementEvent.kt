package com.thomas.contract.messaging.management.group

import com.thomas.contract.messaging.ApplicationEvent
import com.thomas.contract.messaging.ApplicationEventType
import java.time.OffsetDateTime
import java.util.UUID

data class GroupManagementEvent(
    override val eventType: ApplicationEventType,
    override val eventTimestamp: OffsetDateTime,
    override val eventKey: UUID,
    override val eventData: GroupDataEvent?,
) : ApplicationEvent<UUID, GroupDataEvent?>()