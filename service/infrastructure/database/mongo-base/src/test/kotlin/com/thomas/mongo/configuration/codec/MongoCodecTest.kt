package com.thomas.mongo.configuration.codec

import java.math.BigInteger
import java.time.OffsetDateTime
import java.time.ZoneOffset.UTC
import java.util.Date
import java.util.UUID
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.types.Decimal128
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class MongoCodecTest {

    private val bigIntegerCodec = BigIntegerCodec()
    private val offsetDatetimeCodec = OffsetDatetimeCodec()
    private val uuidCodec = UUIDCodec()
    private val decimal128Captor = ArgumentCaptor.forClass(Decimal128::class.java)
    private val dateCaptor = ArgumentCaptor.forClass(Long::class.java)
    private val uuidCaptor = ArgumentCaptor.forClass(String::class.java)
    private val encoderContext = EncoderContext.builder().build()
    private val decoderContext = DecoderContext.builder().build()
    private val writer = mock<BsonWriter>()
    private val reader = mock<BsonReader>()

    @Test
    fun `BigInteger encoder class test`() {
        assertEquals(BigInteger::class.java, bigIntegerCodec.encoderClass)
    }

    @Test
    fun `BigInteger encode test`() {
        doNothing().`when`(writer).writeDecimal128(decimal128Captor.capture())
        val value = BigInteger.valueOf(20)
        bigIntegerCodec.encode(writer, value, encoderContext)
        assertEquals(Decimal128(value.toBigDecimal()), decimal128Captor.value)
    }

    @Test
    fun `BigInteger decode test`() {
        val value = BigInteger.valueOf(70)
        `when`(reader.readDecimal128()).thenReturn(Decimal128(value.toBigDecimal()))
        val result = bigIntegerCodec.decode(reader, decoderContext)
        assertEquals(value, result)
    }

    @Test
    fun `OffsetDatetime encoder class test`() {
        assertEquals(OffsetDateTime::class.java, offsetDatetimeCodec.encoderClass)
    }

    @Test
    fun `OffsetDateTime encode test`() {
        doNothing().`when`(writer).writeDateTime(dateCaptor.capture())
        val value = OffsetDateTime.now(UTC)
        offsetDatetimeCodec.encode(writer, value, encoderContext)
        assertEquals(value.toInstant().toEpochMilli(), dateCaptor.value)
    }

    @Test
    fun `OffsetDateTime decode test`() {
        val value = OffsetDateTime.now(UTC).withNano(0)
        `when`(reader.readDateTime()).thenReturn(value.toInstant().toEpochMilli())
        val result = offsetDatetimeCodec.decode(reader, decoderContext)
        assertEquals(value, result)
    }

    @Test
    fun `UUID encoder class test`() {
        assertEquals(UUID::class.java, uuidCodec.encoderClass)
    }

    @Test
    fun `UUID encode test`() {
        doNothing().`when`(writer).writeString(uuidCaptor.capture())
        val value = UUID.randomUUID()
        uuidCodec.encode(writer, value, encoderContext)
        assertEquals(value.toString(), uuidCaptor.value)
    }

    @Test
    fun `UUID decode test`() {
        val value = UUID.randomUUID()
        `when`(reader.readString()).thenReturn(value.toString())
        val result = uuidCodec.decode(reader, decoderContext)
        assertEquals(value, result)
    }

}