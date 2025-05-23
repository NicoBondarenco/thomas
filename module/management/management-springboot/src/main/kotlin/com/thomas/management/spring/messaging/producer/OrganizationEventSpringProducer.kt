package com.thomas.management.spring.messaging.producer

import com.thomas.contract.messaging.management.organization.OrganizationManagementEvent
import com.thomas.core.extension.withSessionContextVT
import com.thomas.management.domain.messaging.event.OrganizationEventProducer
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.stereotype.Component

@Component
class OrganizationEventSpringProducer(
    streamBridge: StreamBridge,
    @Value("\${spring.cloud.stream.key-header}") keyHeader: String,
) : SpringMessagingProducer<OrganizationManagementEvent>(
    streamBridge,
    keyHeader
), OrganizationEventProducer {

    companion object {
        private const val OUTPUT_CHANNEL = "organizationManagementProducer-out-0"
    }

    override suspend fun organizationCreated(
        event: OrganizationManagementEvent
    ) = sendEvent(event)

    override suspend fun organizationUpdated(
        event: OrganizationManagementEvent
    ) = sendEvent(event)

    private suspend fun sendEvent(
        event: OrganizationManagementEvent
    ) = withSessionContextVT {
        sendMessage(OUTPUT_CHANNEL, event.eventKey.toString(), event)
    }

}
