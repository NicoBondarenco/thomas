package com.thomas.contract.messaging.management.group

import com.thomas.contract.messaging.management.ManagementEvent
import com.thomas.contract.messaging.management.ManagementEventType
import java.time.OffsetDateTime
import java.util.UUID

data class GroupManagementEvent(
    override val eventType: ManagementEventType,
    override val eventTimestamp: OffsetDateTime,
    override val eventKey: UUID,
    override val eventData: GroupDataEvent?,
) : ManagementEvent<UUID, GroupDataEvent?>()