package com.thomas.spring.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

class MaskFieldSerializer : StdSerializer<Any>(Any::class.java) {
    override fun serialize(value: Any?, gen: JsonGenerator, provider: SerializerProvider?) {
        value?.let {
            gen.writeString("*".repeat(it.toString().length))
        } ?: gen.writeNull()
    }
}
