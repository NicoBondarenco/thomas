package com.thomas.locality.spring.configuration

import com.thomas.cache.handler.CacheHandler
import com.thomas.cache.handler.caffeine.CaffeineCacheHandler
import com.thomas.cache.handler.caffeine.CaffeineCacheHandlerProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CacheConfiguration {

    @Bean
    fun cacheProperties(
        @Value("\${locality.cache.initialCacheCapacity}") initialCacheCapacity: Int,
        @Value("\${locality.cache.maximumCacheSize}") maximumCacheSize: Int,
        @Value("\${locality.cache.expireWriteSeconds}") expireWriteSeconds: Long,
        @Value("\${locality.cache.expireAccessSeconds}") expireAccessSeconds: Long,
    ): CaffeineCacheHandlerProperties = CaffeineCacheHandlerProperties(
        initialCacheCapacity = initialCacheCapacity,
        maximumCacheSize = maximumCacheSize,
        expireWriteSeconds = expireWriteSeconds,
        expireAccessSeconds = expireAccessSeconds,
    )

    @Bean
    fun cacheHandler(
        cacheProperties: CaffeineCacheHandlerProperties,
    ): CacheHandler = CaffeineCacheHandler(cacheProperties)

}