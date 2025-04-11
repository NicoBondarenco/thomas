package com.thomas.management.spring.messaging.producer

import com.thomas.contract.messaging.notification.email.SendEmailCommand
import com.thomas.core.extension.withSessionContextVT
import com.thomas.management.domain.messaging.command.NotificationCommandProducer
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.stereotype.Component

@Component
class NotificationCommandSpringProducer(
    streamBridge: StreamBridge,
) : SpringMessagingProducer<SendEmailCommand>(
    streamBridge
), NotificationCommandProducer {

    companion object {
        private const val OUTPUT_CHANNEL = "emailNotificationProducer-out-0"
    }

    override suspend fun sendEmail(
        command: SendEmailCommand
    ) = sendEvent(command)

    private suspend fun sendEvent(
        event: SendEmailCommand
    ) = withSessionContextVT {
        sendMessage(OUTPUT_CHANNEL, event)
    }

}
