package com.thomas.core.aspect

import org.apache.logging.log4j.Level
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MethodLogLevelTest {

    @Test
    fun `Validate log level conversion`() {
        assertEquals(Level.ERROR, MethodLogLevel.ERROR.level)
        assertEquals(Level.WARN, MethodLogLevel.WARN.level)
        assertEquals(Level.INFO, MethodLogLevel.INFO.level)
        assertEquals(Level.DEBUG, MethodLogLevel.DEBUG.level)
        assertEquals(Level.TRACE, MethodLogLevel.TRACE.level)
    }

    @Test
    fun `Validate log level names`() {
        MethodLogLevel.entries.forEach {
            assertEquals(it.name, it.level.name())
        }
    }

}