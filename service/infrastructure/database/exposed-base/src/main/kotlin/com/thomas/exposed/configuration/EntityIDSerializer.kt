package com.thomas.exposed.configuration

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import org.jetbrains.exposed.dao.id.EntityID

internal val exposedModule = SimpleModule("Exposed").apply {
    addSerializer(
        EntityID::class.java,
        object : JsonSerializer<EntityID<*>>() {
            override fun serialize(
                entityId: EntityID<*>,
                generator: JsonGenerator,
                provider: SerializerProvider
            ) = generator.writeString(entityId.value.toString())
        }
    )
}
