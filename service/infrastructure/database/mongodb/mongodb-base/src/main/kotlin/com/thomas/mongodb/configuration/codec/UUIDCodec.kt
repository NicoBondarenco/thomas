package com.thomas.mongodb.configuration.codec

import java.util.UUID
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext

internal class UUIDCodec : Codec<UUID> {

    override fun getEncoderClass(): Class<UUID> = UUID::class.java

    override fun encode(
        writer: BsonWriter,
        value: UUID,
        encoderContext: EncoderContext
    ) = writer.writeString(value.toString())

    override fun decode(
        reader: BsonReader,
        decoderContext: DecoderContext
    ): UUID = UUID.fromString(reader.readString())

}
