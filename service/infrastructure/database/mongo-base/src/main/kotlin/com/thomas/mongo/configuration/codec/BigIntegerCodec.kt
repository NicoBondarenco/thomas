package com.thomas.mongo.configuration.codec

import java.math.BigInteger
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.types.Decimal128

internal class BigIntegerCodec : Codec<BigInteger> {

    override fun getEncoderClass(): Class<BigInteger> = BigInteger::class.java

    override fun encode(
        writer: BsonWriter,
        value: BigInteger,
        encoderContext: EncoderContext
    ) = writer.writeDecimal128(Decimal128(value.toBigDecimal()))

    override fun decode(
        reader: BsonReader,
        decoderContext: DecoderContext
    ): BigInteger = reader.readDecimal128().bigDecimalValue().toBigIntegerExact()

}
