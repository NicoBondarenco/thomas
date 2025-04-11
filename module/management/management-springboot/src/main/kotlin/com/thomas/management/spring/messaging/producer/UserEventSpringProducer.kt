package com.thomas.management.spring.messaging.producer

import com.thomas.contract.messaging.management.user.UserManagementEvent
import com.thomas.core.extension.withSessionContextVT
import com.thomas.management.domain.messaging.event.UserEventProducer
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.stereotype.Component

@Component
class UserEventSpringProducer(
    streamBridge: StreamBridge,
) : SpringMessagingProducer<UserManagementEvent>(
    streamBridge
), UserEventProducer {

    companion object {
        private const val OUTPUT_CHANNEL = "userManagementProducer-out-0"
    }

    override suspend fun userCreated(
        event: UserManagementEvent
    ) = sendEvent(event)

    override suspend fun userUpdated(
        event: UserManagementEvent
    ) = sendEvent(event)

    private suspend fun sendEvent(
        event: UserManagementEvent
    ) = withSessionContextVT {
        sendMessage(OUTPUT_CHANNEL, event)
    }

}
