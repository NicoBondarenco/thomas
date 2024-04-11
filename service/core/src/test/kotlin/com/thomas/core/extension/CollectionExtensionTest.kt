package com.thomas.core.extension

import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class CollectionExtensionTest {

    @Test
    fun `When element is not in list, should add element`(){
        val uuid = UUID.randomUUID().toString()
        val new = UUID.randomUUID().toString()
        val list = mutableListOf(uuid)
        assertEquals(1, list.size)
        assertTrue(list.addIfAbsent(new))
        assertEquals(2, list.size)
    }

    @Test
    fun `When element is in list, should not add element`(){
        val uuid = UUID.randomUUID().toString()
        val list = mutableListOf(uuid)
        assertEquals(1, list.size)
        assertFalse(list.addIfAbsent(uuid))
        assertEquals(1, list.size)
    }

}