package com.thomas.contract.messaging.management

import java.time.OffsetDateTime

abstract class ManagementEvent<K, T> {

    abstract val eventType: ManagementEventType
    abstract val eventTimestamp: OffsetDateTime
    abstract val eventKey: K
    abstract val eventData: T

}
