package com.thomas.core.extension

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class StringExtensionTest {

    @Test
    fun `String is quoted`() {
        assertTrue("\"qwerty\"".isQuoted())
        assertFalse("qwerty\"".isQuoted())
        assertFalse("\"qwerty".isQuoted())
        assertFalse("qwerty".isQuoted())
    }

    @Test
    fun `Unquote string`() {
        assertEquals("qwerty", "\"qwerty\"".unquote())
        assertEquals("qwerty\"", "qwerty\"".unquote())
        assertEquals("\"qwerty", "\"qwerty".unquote())
        assertEquals("qwerty", "qwerty".unquote())
    }

    @Test
    fun `Remove letters from string should leave only numbers`() {
        assertEquals("9876543210", "9876543210qwerty".onlyNumbers())
        assertEquals("9876543210", "9876qwerty543210".onlyNumbers())
        assertEquals("9876543210", "qwerty9876543210".onlyNumbers())
    }

    @Test
    fun `Remove symbols from string should leave only numbers`() {
        assertEquals("9876543210", "9!@#8$%¨7&*(6)_+5-=¬4§,.3;/<2>:?1`´\"0'[]{}^~ªº|\\".onlyNumbers())
    }

    @Test
    fun `Remove symbols from string should leave only numbers and letters`() {
        assertEquals("a9b8c7d6e5f4g3h2i1j0", "a9b!@#8$%¨c7&*d(6e)_+5f-=¬4§g,.3;h/<2i>:?1`´j\"0'[]{}^~|\\".onlyLettersAndNumbers())
    }

    @Test
    fun `Valid tax id numbers`() {
        listOf(
            "946.261.150-55",
            "530.224.560-42",
            "891.551.820-97",
            "346007430 24",
            "789 449 940 48",
            "549649210-69",
            "56704832009",
        ).forEach {
            assertTrue(it.isValidDocumentNumber(), "document $it should be valid")
        }
    }

    @Test
    fun `Invalid tax id numbers`() {
        listOf(
            "946.26.150-55",
            "530.224.560-420",
            "891.551.820-99",
            "346.017.430-24",
            "789.940-48",
            "549.009.210-69",
            "111.111.111-11",
        ).forEach {
            assertFalse(it.isValidDocumentNumber(), "document $it should be invalid")
        }
    }

    @Test
    fun `Valid registration numbers`() {
        listOf(
            "17.233.488/0001-08",
            "51.150.7220001-63",
            "34.326.546000159",
            "84773542/0001-97",
            "75874.243/0001-58",
            "52318044000168",
            "64-726-116-0001-95",
            "56.341.120000107",
            "46101682/000120",
            "26480.882/000123",
        ).forEach {
            assertTrue(it.isValidRegistrationNumber(), "document $it should be valid")
        }
    }

    @Test
    fun `Invalid registration numbers`() {
        listOf(
            "51.126.452/4001-55",
            "57.726.347/0001-333",
            "054.518.522/0001-09",
            "57.434/0001-67",
            "66.081.308-17",
            "12545156",
            "43505802000166987",
            "33.333.333/3333-33",
            "",
            "             ",
            "qwerty",
            "51.420.895/0001-55",
        ).forEach {
            assertFalse(it.isValidRegistrationNumber(), "document $it should be invalid")
        }
    }

    @Test
    fun `Repeat string n times`() {
        assertEquals("", "a".repeat(0))
        assertEquals("", "a".repeat(-1))
        assertEquals("a", "a".repeat(1))
        assertEquals("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT", "T".repeat(48))
        assertEquals("987654321098765432109876543210", "9876543210".repeat(3))
        assertEquals("qwertyqwertyqwertyqwerty", "qwerty".repeat(4))
    }

    @Test
    fun `Convert string to unaccented lower case`() {
        assertEquals("", "".unaccentedLower())
        assertEquals(" ", " ".unaccentedLower())
        assertEquals("qwerty", "Qwerty".unaccentedLower())
        assertEquals("aaaaaaaa cc eeeeee iiiiii nn oooooooo uuuuuu", "ÂâÃãÀàÁá Çç ÊêÈèÉé ÎîÌìÍí Ññ ÔôÕõÒòÓó ÛûÙùÚú".unaccentedLower())
    }

    @Test
    fun `Given a valid UUID string it should return a UUID`() {
        assertNotNull("34ca8da1-b484-4167-bcc9-e9498b6d3ae2".toUUIDOrNull())
        assertNotNull("23028e1b-ec24-4026-8a5b-9d7be867b155".toUUIDOrNull())
        assertNotNull("9c27c9cb-d51f-42ea-84d7-a5e9d00cd1e0".toUUIDOrNull())
        assertNotNull("e00fff05-9bd3-488f-86cf-ecdd388f8e11".toUUIDOrNull())
    }

    @Test
    fun `Given a invalid UUID string it should return null`() {
        assertNull("".toUUIDOrNull())
        assertNull("qwerty".toUUIDOrNull())
        assertNull("01FXTPFJCJFGSRVPHCMKZWCPTG".toUUIDOrNull())
        assertNull("cl0levgka00005440rr0vruzf".toUUIDOrNull())
        assertNull("94f0dd72-71dx-4bea-809d-d1e7ef839a96".toUUIDOrNull())
    }

}