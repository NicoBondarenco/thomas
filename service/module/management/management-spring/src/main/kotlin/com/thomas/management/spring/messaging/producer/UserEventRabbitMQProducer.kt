package com.thomas.management.spring.messaging.producer

import com.thomas.core.context.SessionContextHolder.currentToken
import com.thomas.management.domain.event.UserEventProducer
import com.thomas.management.message.event.UserUpsertedEvent
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component

@Component
class UserEventRabbitMQProducer(
    private val streamBridge: StreamBridge,
) : UserEventProducer {

    companion object {
        private const val USER_CREATED_PRODUCER_OUTPUT = "userCreatedProducer-out-0"
        private const val USER_UPDATED_PRODUCER_OUTPUT = "userUpdatedProducer-out-0"
    }

    override fun sendCreatedEvent(
        event: UserUpsertedEvent
    ): Boolean = sendMessage(USER_CREATED_PRODUCER_OUTPUT, event)

    override fun sendUpdatedEvent(
        event: UserUpsertedEvent
    ): Boolean = sendMessage(USER_UPDATED_PRODUCER_OUTPUT, event)

    private fun sendMessage(
        output: String,
        event: UserUpsertedEvent,
    ): Boolean = streamBridge.send(
        output,
        MessageBuilder
            .withPayload(event)
            .setHeader("Authorization", "Bearer ${currentToken ?: ""}")
            .build(),
    )

}
