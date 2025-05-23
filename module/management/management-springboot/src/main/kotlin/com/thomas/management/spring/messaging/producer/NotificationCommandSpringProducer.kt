package com.thomas.management.spring.messaging.producer

import com.thomas.contract.messaging.notification.email.SendEmailCommand
import com.thomas.core.extension.withSessionContextVT
import com.thomas.management.domain.messaging.command.NotificationCommandProducer
import java.util.UUID.randomUUID
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.stereotype.Component

@Component
class NotificationCommandSpringProducer(
    streamBridge: StreamBridge,
    @Value("\${spring.cloud.stream.key-header}") keyHeader: String,
) : SpringMessagingProducer<SendEmailCommand>(
    streamBridge,
    keyHeader
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
        sendMessage(OUTPUT_CHANNEL, randomUUID().toString(), event)
    }

}
