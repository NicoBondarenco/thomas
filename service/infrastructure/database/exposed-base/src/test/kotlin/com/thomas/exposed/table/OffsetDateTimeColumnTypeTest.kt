package com.thomas.exposed.table

import com.thomas.exposed.exception.ExposedException
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource

class OffsetDateTimeColumnTypeTest {

    companion object {

        @JvmStatic
        fun offsetDateTimes(): List<Arguments> = listOf(
            Arguments.of(OffsetDateTime.now(UTC)),
            Arguments.of(OffsetDateTime.now(ZoneOffset.ofHours(-3)).plusDays(5)),
            Arguments.of(OffsetDateTime.now(ZoneOffset.ofHours(4)).minusDays(10)),
        )

        @JvmStatic
        fun anyNonNullValues(): List<Arguments> = listOf(
            Arguments.of(9806321064L),
            Arguments.of(ZonedDateTime.now()),
            Arguments.of(BigDecimal("216864.48943484")),
            Arguments.of("2024-05-01 08:01:00.993995-03"),
            Arguments.of("2020-01-30 06:49:55.662Z"),
            Arguments.of("1990-11-24 22:31:27.057416802+04"),
        )

    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 10])
    fun `Given value outside allowed precision must throw ExposedException`(value: Int) {
        val exception = assertThrows<ExposedException> { OffsetDateTimeColumnType(value) }
        assertEquals("precision must be between 0 and 9", exception.message)
    }

    @Test
    fun `Column must have time part`() {
        assertTrue(OffsetDateTimeColumnType(6).hasTimePart)
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 3, 6, 9])
    fun `Columns SQL type precision must correspond to the given value`(value: Int) {
        assertEquals("TIMESTAMP($value) WITH TIME ZONE", OffsetDateTimeColumnType(value).sqlType())
    }

    @ParameterizedTest
    @MethodSource("offsetDateTimes")
    fun `Parsing non null OffsetDateTime must return the formatted string`(value: OffsetDateTime) {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSX")
        val result = OffsetDateTimeColumnType(6).nonNullValueToString(value)
        assertEquals("'${formatter.format(value)}'", result)
    }

    @ParameterizedTest
    @MethodSource("anyNonNullValues")
    fun `Parsing non null value different of OffsetDateTime throw ExposedException`(value: Any) {
        val exception = assertThrows<ExposedException> { OffsetDateTimeColumnType(8).nonNullValueToString(value) }
        assertEquals("value $value (${value::class}) is not of type OffsetDateTime", exception.message)
    }

    @ParameterizedTest
    @MethodSource("offsetDateTimes")
    fun `Getting non null timestamp with time zone from database must return OffsetDateTime`(value: OffsetDateTime) {
        val result = OffsetDateTimeColumnType(7).valueFromDB(value)
        assertEquals(value, result)
    }

    @ParameterizedTest
    @MethodSource("anyNonNullValues")
    fun `Getting non null value different of timestamp with time zone from database throw ExposedException`(value: Any) {
        val exception = assertThrows<ExposedException> { OffsetDateTimeColumnType(3).valueFromDB(value) }
        assertEquals("value $value (${value::class}) is not of type OffsetDateTime", exception.message)
    }

    @ParameterizedTest
    @MethodSource("offsetDateTimes")
    fun `Converting non null OffsetDateTime to database type must return OffsetDateTime`(value: OffsetDateTime) {
        val result = OffsetDateTimeColumnType(2).notNullValueToDB(value)
        assertEquals(value, result)
    }

    @ParameterizedTest
    @MethodSource("anyNonNullValues")
    fun `Converting non null value different of OffsetDateTime to database type throw ExposedException`(value: Any) {
        val exception = assertThrows<ExposedException> { OffsetDateTimeColumnType(0).notNullValueToDB(value) }
        assertEquals("value $value (${value::class}) is not of type OffsetDateTime", exception.message)
    }

}
