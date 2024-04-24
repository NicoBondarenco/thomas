package com.thomas.core.extension

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

}
