package com.thomas.contract.messaging

import java.time.OffsetDateTime

abstract class ApplicationEvent<K, T> {

    abstract val eventType: ApplicationEventType
    abstract val eventTimestamp: OffsetDateTime
    abstract val eventKey: K
    abstract val eventData: T

}
