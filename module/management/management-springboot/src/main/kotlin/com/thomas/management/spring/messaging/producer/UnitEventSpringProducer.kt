package com.thomas.management.spring.messaging.producer

import com.thomas.contract.messaging.management.unit.UnitManagementEvent
import com.thomas.core.extension.withSessionContextVT
import com.thomas.management.domain.messaging.event.UnitEventProducer
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.stereotype.Component

@Component
class UnitEventSpringProducer(
    streamBridge: StreamBridge,
) : SpringMessagingProducer<UnitManagementEvent>(
    streamBridge
), UnitEventProducer {

    companion object {
        private const val OUTPUT_CHANNEL = "unitManagementProducer-out-0"
    }

    override suspend fun unitCreated(
        event: UnitManagementEvent
    ) = sendEvent(event)

    override suspend fun unitUpdated(
        event: UnitManagementEvent
    ) = sendEvent(event)

    override suspend fun unitDeleted(
        event: UnitManagementEvent
    ) = sendEvent(event)

    private suspend fun sendEvent(
        event: UnitManagementEvent
    ) = withSessionContextVT {
        sendMessage(OUTPUT_CHANNEL, event)
    }

}
