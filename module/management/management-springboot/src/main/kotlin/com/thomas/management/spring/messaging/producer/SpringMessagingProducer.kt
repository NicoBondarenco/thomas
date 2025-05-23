package com.thomas.management.spring.messaging.producer

import com.thomas.core.context.SessionContextHolder.currentToken
import kotlinx.coroutines.coroutineScope
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.messaging.support.MessageBuilder

abstract class SpringMessagingProducer<T : Any>(
    private val streamBridge: StreamBridge,
    private val keyHeader: String,
) {

    protected suspend fun sendMessage(
        channel: String,
        key: String,
        payload: T,
    ): Unit = coroutineScope {
        streamBridge.send(
            channel,
            MessageBuilder
                .withPayload(payload)
                .setHeader(AUTHORIZATION, "Bearer ${currentToken ?: ""}")
                .setHeader(keyHeader, key)
                .build(),
        )
    }

}
