package com.thomas.core.extension

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class NumberExtensionTest {

    @Test
    fun `Int is within range`() {
        assertTrue(10.isBetween(9, 11))
        assertTrue(10.isBetween(10, 11))
        assertTrue(10.isBetween(10, 10))
        assertTrue(10.isBetween(9, 10))
    }

    @Test
    fun `Int is not within range`() {
        assertFalse(10.isBetween(11, 15))
        assertFalse(10.isBetween(11, 11))
        assertFalse(10.isBetween(9, 9))
        assertFalse(10.isBetween(11, 9))
    }

    @Test
    fun `Long is within range`() {
        assertTrue(100000L.isBetween(99999, 100001))
        assertTrue(100000L.isBetween(100000, 100001))
        assertTrue(100000L.isBetween(100000, 100000))
        assertTrue(100000L.isBetween(99999, 100000))
    }

    @Test
    fun `Long is not within range`() {
        assertFalse(100000L.isBetween(100001, 110000))
        assertFalse(100000L.isBetween(100001, 100001))
        assertFalse(100000L.isBetween(99999, 99999))
        assertFalse(100000L.isBetween(100001, 99999))
    }

    @Test
    fun `Long to positive`(){
        assertEquals(1234567890L.toPositive(), 1234567890L)
        assertEquals((-1234567890L).toPositive(), 1234567890L)
        assertEquals(0L.toPositive(), 0L)
        assertEquals((-0L).toPositive(), 0L)
        assertEquals(1L.toPositive(), 1L)
        assertEquals((-1L).toPositive(), 1L)
    }

}