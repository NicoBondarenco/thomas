package com.thomas.management.data.extension

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class StringExtensionTest {

    companion object {
        @JvmStatic
        fun documentNumbers() = listOf(
            Arguments.of("94626115055", true),
            Arguments.of("530.224560-42", true),
            Arguments.of("891 551 820 97", true),
            Arguments.of("346-007-430-24", true),
            Arguments.of("789449940-48", true),
            Arguments.of("549 649_210-69", true),
            Arguments.of("567.048.320-09", true),
            Arguments.of("946 26 150-55", false),
            Arguments.of("530224560420", false),
            Arguments.of("891551820-99", false),
            Arguments.of("346.017.430-24", false),
            Arguments.of("789.940-48", false),
            Arguments.of("549-009-210-69", false),
            Arguments.of("111.111.111-11", false),
            Arguments.of("", false),
            Arguments.of(" ", false),
        )
    }

    @ParameterizedTest
    @MethodSource("documentNumbers")
    fun `Given a document number it should validate correctly`(value: String, expected: Boolean) {
        assertEquals(expected, value.isValidDocumentNumber())
    }

}