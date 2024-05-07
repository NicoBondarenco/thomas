package com.thomas.exposed.properties

import com.thomas.exposed.exception.ExposedException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ExposedAuditoryPropertiesTest {

    @Test
    fun `WHEN tables package is empty THEN should throws ExposedException`() {
        val exception = assertThrows<ExposedException> { ExposedAuditoryProperties() }
        assertEquals("Auditory tables package cannot be null nor empty", exception.message)
    }

}
