package com.thomas.management.spring.messaging.producer

import com.thomas.core.context.SessionContextHolder.currentToken
import com.thomas.management.domain.event.GroupEventProducer
import com.thomas.management.message.event.GroupDeletedEvent
import com.thomas.management.message.event.GroupUpsertedEvent
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component

@Component
class GroupEventRabbitMQProducer(
    private val streamBridge: StreamBridge,
) : GroupEventProducer {

    companion object {
        private const val GROUP_CREATED_PRODUCER_OUTPUT = "groupCreatedProducer-out-0"
        private const val GROUP_UPDATED_PRODUCER_OUTPUT = "groupUpdatedProducer-out-0"
        private const val GROUP_DELETED_PRODUCER_OUTPUT = "groupDeletedProducer-out-0"
    }

    override fun sendCreatedEvent(
        event: GroupUpsertedEvent
    ): Boolean = sendMessage(GROUP_CREATED_PRODUCER_OUTPUT, event)

    override fun sendUpdatedEvent(
        event: GroupUpsertedEvent
    ): Boolean = sendMessage(GROUP_UPDATED_PRODUCER_OUTPUT, event)

    override fun sendDeletedEvent(
        event: GroupDeletedEvent
    ): Boolean = sendMessage(GROUP_DELETED_PRODUCER_OUTPUT, event)

    private fun <T : Any> sendMessage(
        output: String,
        event: T,
    ): Boolean = streamBridge.send(
        output,
        MessageBuilder
            .withPayload(event)
            .setHeader("Authorization", "Bearer ${currentToken ?: ""}")
            .build(),
    )

}
