package com.thomas.exposed.configuration

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY
import com.fasterxml.jackson.annotation.PropertyAccessor.ALL
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_INVALID_SUBTYPE
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE
import com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import com.fasterxml.jackson.databind.node.JsonNodeFactory.withExactBigDecimals
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature.NullIsSameAsDefault
import com.fasterxml.jackson.module.kotlin.KotlinFeature.NullToEmptyCollection
import com.fasterxml.jackson.module.kotlin.KotlinFeature.NullToEmptyMap
import com.fasterxml.jackson.module.kotlin.KotlinFeature.SingletonSupport
import com.fasterxml.jackson.module.kotlin.KotlinFeature.StrictNullChecks
import com.fasterxml.jackson.module.kotlin.KotlinModule

internal object ExposedObjectMapperFactory {

    private const val REFLECTION_CACHE_SIZE = 512

    fun create(): ObjectMapper = ObjectMapper()
        .registerModule(
            KotlinModule.Builder()
                .withReflectionCacheSize(REFLECTION_CACHE_SIZE)
                .configure(NullToEmptyCollection, false)
                .configure(NullToEmptyMap, false)
                .configure(NullIsSameAsDefault, false)
                .configure(SingletonSupport, false)
                .configure(StrictNullChecks, false)
                .build()
        )
        .registerModule(JavaTimeModule())
        .registerModule(exposedModule)
        .setNodeFactory(withExactBigDecimals(true))
        .setPropertyNamingStrategy(SNAKE_CASE)
        .disable(FAIL_ON_UNKNOWN_PROPERTIES)
        .disable(FAIL_ON_INVALID_SUBTYPE)
        .disable(WRITE_DATES_AS_TIMESTAMPS)
        .disable(FAIL_ON_EMPTY_BEANS)
        .setVisibility(ALL, ANY)

}
