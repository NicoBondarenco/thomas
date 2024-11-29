package com.thomas.contract.messaging.management.group

import java.time.OffsetDateTime
import java.util.UUID

data class GroupDeletedEvent(
    val id: UUID,
    val deletedAt: OffsetDateTime,
)
