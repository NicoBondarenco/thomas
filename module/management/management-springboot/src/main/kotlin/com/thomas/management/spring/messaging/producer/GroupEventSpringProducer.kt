package com.thomas.management.spring.messaging.producer

import com.thomas.contract.messaging.management.group.GroupManagementEvent
import com.thomas.core.extension.withSessionContextVT
import com.thomas.management.domain.messaging.event.GroupEventProducer
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.stereotype.Component

@Component
class GroupEventSpringProducer(
    streamBridge: StreamBridge,
    @Value("\${spring.cloud.stream.key-header}") keyHeader: String,
) : SpringMessagingProducer<GroupManagementEvent>(
    streamBridge,
    keyHeader
), GroupEventProducer {

    companion object {
        private const val OUTPUT_CHANNEL = "groupManagementProducer-out-0"
    }

    override suspend fun groupCreated(
        event: GroupManagementEvent
    ) = sendEvent(event)

    override suspend fun groupUpdated(
        event: GroupManagementEvent
    ) = sendEvent(event)

    override suspend fun groupDeleted(
        event: GroupManagementEvent
    ) = sendEvent(event)

    private suspend fun sendEvent(
        event: GroupManagementEvent
    ) = withSessionContextVT {
        sendMessage(OUTPUT_CHANNEL, event.eventKey.toString(), event)
    }

}
