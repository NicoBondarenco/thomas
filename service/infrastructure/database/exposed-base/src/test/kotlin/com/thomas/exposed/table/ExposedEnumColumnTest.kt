package com.thomas.exposed.table

import com.thomas.exposed.model.simple.EnumerationTable
import com.thomas.exposed.model.simple.EnumerationValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class ExposedEnumColumnTest {

    @ParameterizedTest
    @EnumSource(EnumerationValue::class)
    fun `Transform non nullable String Enum`(value: EnumerationValue) {
        assertEquals(value.name, EnumerationTable.valueEnum.toColumn(value))
        assertEquals(value, EnumerationTable.valueEnum.toReal(value.name))
    }

    @ParameterizedTest
    @EnumSource(EnumerationValue::class)
    fun `Transform nullable String Enum`(value: EnumerationValue) {
        assertEquals(value.name, EnumerationTable.nullableEnum.toColumn(value))
        assertEquals(value, EnumerationTable.nullableEnum.toReal(value.name))
    }

    @Test
    fun `Transform nullable String Enum with null values`() {
        assertNull(EnumerationTable.nullableEnum.toColumn(null))
        assertNull(EnumerationTable.nullableEnum.toReal(null))
    }

}
