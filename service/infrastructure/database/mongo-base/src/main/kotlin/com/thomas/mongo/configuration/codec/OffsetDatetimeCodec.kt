package com.thomas.mongo.configuration.codec

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset.UTC
import java.util.Date
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext

class OffsetDatetimeCodec : Codec<OffsetDateTime> {

    override fun getEncoderClass(): Class<OffsetDateTime> = OffsetDateTime::class.java

    override fun encode(
        writer: BsonWriter,
        value: OffsetDateTime,
        encoderContext: EncoderContext
    ) {
        writer.writeDateTime(value.toInstant().toEpochMilli())
    }

    override fun decode(
        reader: BsonReader,
        decoderContext: DecoderContext
    ): OffsetDateTime = Instant.ofEpochMilli(reader.readDateTime()).atOffset(UTC)

}