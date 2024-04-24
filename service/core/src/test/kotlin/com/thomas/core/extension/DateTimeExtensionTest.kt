package com.thomas.core.extension

import com.thomas.core.exception.InstantEpochNanoException
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.ZoneOffset.UTC
import java.time.ZoneOffset.ofHours
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class DateTimeExtensionTest {

    @Test
    fun `LocalDate to ISO date should return a ISO String correctly`() {
        assertEquals("1990-04-28", LocalDate.of(1990, Month.APRIL, 28).toIsoDate())
        assertEquals("2020-08-05", LocalDate.of(2020, Month.AUGUST, 5).toIsoDate())
    }

    @Test
    fun `OffsetDateTime to ISO date time should return a ISO String correctly`() {
        assertEquals("1990-04-28T09:30:27.657-03:00", OffsetDateTime.of(1990, Month.APRIL.value, 28, 9, 30, 27, 657000000, ZoneOffset.ofHours(-3)).toIsoOffsetDateTime())
        assertEquals("2005-12-31T17:04:01.992135171Z", OffsetDateTime.of(2005, Month.DECEMBER.value, 31, 17, 4, 1, 992135171, UTC).toIsoOffsetDateTime())
    }

    @Test
    fun `Instant is between values`(){
        val instant = OffsetDateTime.of(1990,4,28,9,38,44,512684739, UTC).toInstant()
        val min = OffsetDateTime.of(1990,4,27,0,0,0,0, UTC).toInstant()
        val max = OffsetDateTime.of(1990,4,29,0,0,0,0, UTC).toInstant()
        assertTrue(instant.isBetween(min, max))
    }

    @Test
    fun `Instant is between values equals min`(){
        val instant = OffsetDateTime.of(1990,4,28,9,38,44,512684739, UTC).toInstant()
        val min = OffsetDateTime.of(1990,4,28,9,38,44,512684739, UTC).toInstant()
        val max = OffsetDateTime.of(1990,4,29,0,0,0,0, UTC).toInstant()
        assertTrue(instant.isBetween(min, max))
    }

    @Test
    fun `Instant is between values equals max`(){
        val instant = OffsetDateTime.of(1990,4,28,9,38,44,512684739, UTC).toInstant()
        val min = OffsetDateTime.of(1990,4,27,0,0,0,0, UTC).toInstant()
        val max = OffsetDateTime.of(1990,4,28,9,38,44,512684739, UTC).toInstant()
        assertTrue(instant.isBetween(min, max))
    }

    @Test
    fun `Instant is not between values lower than min`(){
        val instant = OffsetDateTime.of(1990,4,28,9,38,44,512684739, UTC).toInstant()
        val min = OffsetDateTime.of(1990,4,28,9,38,44,512684740, UTC).toInstant()
        val max = OffsetDateTime.of(1990,4,29,0,0,0,0, UTC).toInstant()
        assertFalse(instant.isBetween(min, max))
    }

    @Test
    fun `Instant is not between values higher than max`(){
        val instant = OffsetDateTime.of(1990,4,28,9,38,44,512684740, UTC).toInstant()
        val min = OffsetDateTime.of(1990,4,27,0,0,0,0, UTC).toInstant()
        val max = OffsetDateTime.of(1990,4,28,9,38,44,512684739, UTC).toInstant()
        assertFalse(instant.isBetween(min, max))
    }

    @Test
    fun `Instant is between values different zones`(){
        val instant = OffsetDateTime.of(1990,4,28,6,38,44,512684739, ofHours(-3)).toInstant()
        val min = OffsetDateTime.of(1990,4,27,0,0,0,0, UTC).toInstant()
        val max = OffsetDateTime.of(1990,4,29,0,0,0,0, UTC).toInstant()
        assertTrue(instant.isBetween(min, max))
    }

    @Test
    fun `Instant is between values equals min different zones`(){
        val instant = OffsetDateTime.of(1990,4,28,4,38,44,512684739, ofHours(-5)).toInstant()
        val min = OffsetDateTime.of(1990,4,28,9,38,44,512684739, UTC).toInstant()
        val max = OffsetDateTime.of(1990,4,29,0,0,0,0, UTC).toInstant()
        assertTrue(instant.isBetween(min, max))
    }

    @Test
    fun `Instant is between values equals max different zones`(){
        val instant = OffsetDateTime.of(1990,4,28,11,38,44,512684739, ofHours(2)).toInstant()
        val min = OffsetDateTime.of(1990,4,27,0,0,0,0, UTC).toInstant()
        val max = OffsetDateTime.of(1990,4,28,9,38,44,512684739, UTC).toInstant()
        assertTrue(instant.isBetween(min, max))
    }

    @Test
    fun `Instant to epoch nano`(){
        val instant = Instant.parse("1990-04-28T09:38:44.01741Z")
        assertEquals(641295524017410000L, instant.epochNanoSeconds())
    }

    @Test
    fun `Instant to epoch nano negative`(){
        val instant = Instant.parse("1955-09-27T14:01:13.009465Z")
        assertEquals(-450093527009465000L, instant.epochNanoSeconds())
    }

    @Test
    fun `Instant to epoch nano min value`(){
        val instant = Instant.parse("1900-01-01T00:00:00Z")
        assertEquals(-2208988800000000000L, instant.epochNanoSeconds())
    }

    @Test
    fun `Instant to epoch nano max value`(){
        val instant = Instant.parse("2099-12-31T23:59:59.999999999Z")
        assertEquals(4102444799999999999L, instant.epochNanoSeconds())
    }

    @Test
    fun `Instant to epoch nano lower than min`(){
        val instant = Instant.parse("1899-12-31T23:59:59.999999999Z")
        assertThrows<InstantEpochNanoException> { instant.epochNanoSeconds() }
    }

    @Test
    fun `Instant to epoch nano higher than max`(){
        val instant = Instant.parse("2100-01-01T00:00:00Z")
        assertThrows<InstantEpochNanoException> { instant.epochNanoSeconds() }
    }

    @Test
    fun `Instant from epoch nano`(){
        val instant = Instant.parse("1990-04-28T09:38:44.01741Z")
        val parsed = instantOfEpochNanoSeconds(641295524017410000L)
        assertEquals(instant, parsed)
    }

    @Test
    fun `Instant from epoch nano negative`(){
        val instant = Instant.parse("1955-09-27T14:01:13.009465Z")
        val parsed = instantOfEpochNanoSeconds(-450093527009465000L)
        assertEquals(instant, parsed)
    }

    @Test
    fun `Instant from epoch nano min value`(){
        val instant = Instant.parse("1900-01-01T00:00:00Z")
        val parsed = instantOfEpochNanoSeconds(-2208988800000000000L)
        assertEquals(instant, parsed)
    }

    @Test
    fun `Instant from epoch nano max value`(){
        val instant = Instant.parse("2099-12-31T23:59:59.999999999Z")
        val parsed = instantOfEpochNanoSeconds(4102444799999999999L)
        assertEquals(instant, parsed)
    }

    @Test
    fun `Instant from epoch nano lower than min`(){
        assertThrows<InstantEpochNanoException> { instantOfEpochNanoSeconds(-2208988800000000001L) }
    }

    @Test
    fun `Instant from epoch nano higher than max`(){
        assertThrows<InstantEpochNanoException> { instantOfEpochNanoSeconds(4102444800000000000L) }
    }

}