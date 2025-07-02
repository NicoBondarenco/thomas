package com.thomas.database.komapper.extension

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class ExpressionExtensionTest {

    @Nested
    @DisplayName("String.toLike() tests")
    inner class ToLikeTests {

        @Test
        @DisplayName("Should wrap string with % on both sides")
        fun `should wrap string with percent on both sides`() {
            val input = "test"

            val result = input.toLike()

            assertEquals("%test%", result)
        }

        @Test
        @DisplayName("Should handle empty string")
        fun `should handle empty string`() {
            val input = ""

            val result = input.toLike()

            assertEquals("%%", result)
        }

        @Test
        @DisplayName("Should handle single character")
        fun `should handle single character`() {
            val input = "a"

            val result = input.toLike()

            assertEquals("%a%", result)
        }

        @Test
        @DisplayName("Should handle string with spaces")
        fun `should handle string with spaces`() {
            val input = "hello world"

            val result = input.toLike()

            assertEquals("%hello world%", result)
        }

        @Test
        @DisplayName("Should handle string with special characters")
        fun `should handle string with special characters`() {
            val input = "test@#$%^&*()"

            val result = input.toLike()

            assertEquals("%test@#$%^&*()%", result)
        }

        @Test
        @DisplayName("Should handle string already containing percent signs")
        fun `should handle string already containing percent signs`() {
            val input = "%existing%"

            val result = input.toLike()

            assertEquals("%%existing%%", result)
        }

        @Test
        @DisplayName("Should handle Portuguese characters with accents")
        fun `should handle portuguese characters with accents`() {
            val input = "João José"

            val result = input.toLike()

            assertEquals("%João José%", result)
        }

        @ParameterizedTest
        @ValueSource(strings = ["test", "TEST", "Test", "tEsT"])
        @DisplayName("Should work with different case variations")
        fun `should work with different case variations`(input: String) {
            val result = input.toLike()

            assertTrue(result.startsWith("%"))
            assertTrue(result.endsWith("%"))
            assertEquals("%$input%", result)
        }

        @ParameterizedTest
        @CsvSource(
            "'hello', '%hello%'",
            "'world', '%world%'",
            "'123', '%123%'",
            "'a b c', '%a b c%'",
            "'_underscore', '%_underscore%'"
        )
        @DisplayName("Should produce expected results for various inputs")
        fun `should produce expected results for various inputs`(input: String, expected: String) {
            val result = input.toLike()

            assertEquals(expected, result)
        }
    }

    @Nested
    @DisplayName("String.toLikeStart() tests")
    inner class ToLikeStartTests {

        @Test
        @DisplayName("Should add % only at the end")
        fun `should add percent only at the end`() {
            val input = "test"

            val result = input.toLikeStart()

            assertEquals("test%", result)
        }

        @Test
        @DisplayName("Should handle empty string")
        fun `should handle empty string`() {
            val input = ""

            val result = input.toLikeStart()

            assertEquals("%", result)
        }

        @Test
        @DisplayName("Should handle single character")
        fun `should handle single character`() {
            val input = "a"

            val result = input.toLikeStart()

            assertEquals("a%", result)
        }

        @Test
        @DisplayName("Should handle string with spaces")
        fun `should handle string with spaces`() {
            val input = "hello world"

            val result = input.toLikeStart()

            assertEquals("hello world%", result)
        }

        @Test
        @DisplayName("Should handle string with special characters")
        fun `should handle string with special characters`() {
            val input = "test@#$%^&*()"

            val result = input.toLikeStart()

            assertEquals("test@#$%^&*()%", result)
        }

        @Test
        @DisplayName("Should handle string already ending with percent")
        fun `should handle string already ending with percent`() {
            val input = "existing%"

            val result = input.toLikeStart()

            assertEquals("existing%%", result)
        }

        @Test
        @DisplayName("Should handle Portuguese characters with accents")
        fun `should handle portuguese characters with accents`() {
            val input = "Maria"

            val result = input.toLikeStart()

            assertEquals("Maria%", result)
        }

        @ParameterizedTest
        @ValueSource(strings = ["prefix", "PREFIX", "Prefix", "pReFixx"])
        @DisplayName("Should work with different case variations")
        fun `should work with different case variations`(input: String) {
            val result = input.toLikeStart()

            assertFalse(result.startsWith("%"))
            assertTrue(result.endsWith("%"))
            assertEquals("$input%", result)
        }

        @ParameterizedTest
        @CsvSource(
            "'start', 'start%'",
            "'begin', 'begin%'",
            "'prefix', 'prefix%'",
            "'123', '123%'",
            "'João', 'João%'"
        )
        @DisplayName("Should produce expected results for various inputs")
        fun `should produce expected results for various inputs`(input: String, expected: String) {
            val result = input.toLikeStart()

            assertEquals(expected, result)
        }
    }

    @Nested
    @DisplayName("String.toLikeEnd() tests")
    inner class ToLikeEndTests {

        @Test
        @DisplayName("Should add % only at the beginning")
        fun `should add percent only at the beginning`() {
            val input = "test"

            val result = input.toLikeEnd()

            assertEquals("%test", result)
        }

        @Test
        @DisplayName("Should handle empty string")
        fun `should handle empty string`() {
            val input = ""

            val result = input.toLikeEnd()

            assertEquals("%", result)
        }

        @Test
        @DisplayName("Should handle single character")
        fun `should handle single character`() {
            val input = "a"

            val result = input.toLikeEnd()

            assertEquals("%a", result)
        }

        @Test
        @DisplayName("Should handle string with spaces")
        fun `should handle string with spaces`() {
            val input = "hello world"

            val result = input.toLikeEnd()

            assertEquals("%hello world", result)
        }

        @Test
        @DisplayName("Should handle string with special characters")
        fun `should handle string with special characters`() {
            val input = "test@#$%^&*()"

            val result = input.toLikeEnd()

            assertEquals("%test@#$%^&*()", result)
        }

        @Test
        @DisplayName("Should handle string already starting with percent")
        fun `should handle string already starting with percent`() {
            val input = "%existing"

            val result = input.toLikeEnd()

            assertEquals("%%existing", result)
        }

        @Test
        @DisplayName("Should handle Portuguese characters with accents")
        fun `should handle portuguese characters with accents`() {
            val input = "José"

            val result = input.toLikeEnd()

            assertEquals("%José", result)
        }

        @ParameterizedTest
        @ValueSource(strings = ["suffix", "SUFFIX", "Suffix", "sUfFiX"])
        @DisplayName("Should work with different case variations")
        fun `should work with different case variations`(input: String) {
            val result = input.toLikeEnd()

            assertTrue(result.startsWith("%"))
            assertFalse(result.endsWith("%"))
            assertEquals("%$input", result)
        }

        @ParameterizedTest
        @CsvSource(
            "'end', '%end'",
            "'finish', '%finish'",
            "'suffix', '%suffix'",
            "'123', '%123'",
            "'Maria', '%Maria'"
        )
        @DisplayName("Should produce expected results for various inputs")
        fun `should produce expected results for various inputs`(input: String, expected: String) {
            val result = input.toLikeEnd()

            assertEquals(expected, result)
        }
    }

    @Nested
    @DisplayName("Comparison tests between methods")
    inner class ComparisonTests {

        @Test
        @DisplayName("toLike should be combination of toLikeStart and toLikeEnd patterns")
        fun `toLike should be combination of toLikeStart and toLikeEnd patterns`() {
            val input = "test"

            val toLikeResult = input.toLike()
            val toLikeStartResult = input.toLikeStart()
            val toLikeEndResult = input.toLikeEnd()

            assertEquals("%test%", toLikeResult)
            assertEquals("test%", toLikeStartResult)
            assertEquals("%test", toLikeEndResult)

            assertTrue(toLikeResult.startsWith("%"))
            assertTrue(toLikeResult.endsWith("%"))
            assertTrue(toLikeResult.contains(input))
        }

        @Test
        @DisplayName("All methods should preserve original string content")
        fun `all methods should preserve original string content`() {
            val input = "preserve me"

            val toLikeResult = input.toLike()
            val toLikeStartResult = input.toLikeStart()
            val toLikeEndResult = input.toLikeEnd()

            assertTrue(toLikeResult.contains(input))
            assertTrue(toLikeStartResult.contains(input))
            assertTrue(toLikeEndResult.contains(input))
        }

        @Test
        @DisplayName("Methods should handle same input consistently")
        fun `methods should handle same input consistently`() {
            val inputs = listOf("test", "", "a", "João Silva", "123@#$")

            inputs.forEach { input ->
                val toLikeResult = input.toLike()
                val toLikeStartResult = input.toLikeStart()
                val toLikeEndResult = input.toLikeEnd()

                assertEquals("%$input%", toLikeResult, "toLike failed for input: $input")
                assertEquals("$input%", toLikeStartResult, "toLikeStart failed for input: $input")
                assertEquals("%$input", toLikeEndResult, "toLikeEnd failed for input: $input")
            }
        }
    }

    @Nested
    @DisplayName("Edge cases and special scenarios")
    inner class EdgeCasesTests {

        @Test
        @DisplayName("Should handle strings with multiple consecutive spaces")
        fun `should handle strings with multiple consecutive spaces`() {
            val input = "hello    world"

            assertEquals("%hello    world%", input.toLike())
            assertEquals("hello    world%", input.toLikeStart())
            assertEquals("%hello    world", input.toLikeEnd())
        }

        @Test
        @DisplayName("Should handle strings with newlines and tabs")
        fun `should handle strings with newlines and tabs`() {
            val input = "hello\n\tworld"

            assertEquals("%hello\n\tworld%", input.toLike())
            assertEquals("hello\n\tworld%", input.toLikeStart())
            assertEquals("%hello\n\tworld", input.toLikeEnd())
        }

        @Test
        @DisplayName("Should handle very long strings")
        fun `should handle very long strings`() {
            val input = "a".repeat(1000)

            val toLikeResult = input.toLike()
            val toLikeStartResult = input.toLikeStart()
            val toLikeEndResult = input.toLikeEnd()

            assertEquals(1002, toLikeResult.length)
            assertEquals(1001, toLikeStartResult.length)
            assertEquals(1001, toLikeEndResult.length)
        }

        @Test
        @DisplayName("Should handle strings with only special characters")
        fun `should handle strings with only special characters`() {
            val input = "!@#$%^&*()_+-=[]{}|;:,.<>?"

            assertEquals("%$input%", input.toLike())
            assertEquals("$input%", input.toLikeStart())
            assertEquals("%$input", input.toLikeEnd())
        }

        @Test
        @DisplayName("Should handle Unicode characters")
        fun `should handle unicode characters`() {
            val input = "🚀🎉💻🔥⭐"

            assertEquals("%🚀🎉💻🔥⭐%", input.toLike())
            assertEquals("🚀🎉💻🔥⭐%", input.toLikeStart())
            assertEquals("%🚀🎉💻🔥⭐", input.toLikeEnd())
        }
    }

    @Nested
    @DisplayName("SQL LIKE pattern usage simulation")
    inner class SqlLikeSimulationTests {

        @Test
        @DisplayName("toLike should simulate contains behavior")
        fun `toLike should simulate contains behavior`() {
            val searchTerm = "Silva"
            val pattern = searchTerm.toLike()

            val databaseValues = listOf(
                "João Silva Santos",
                "Maria Silva",
                "Silva João",
                "Pedro Santos",
                "Ana Silva Costa"
            )

            val matches = databaseValues.filter { it.contains(searchTerm) }

            assertEquals("%Silva%", pattern)
            assertEquals(4, matches.size)
            assertTrue(matches.contains("João Silva Santos"))
            assertTrue(matches.contains("Maria Silva"))
            assertTrue(matches.contains("Silva João"))
            assertTrue(matches.contains("Ana Silva Costa"))
            assertFalse(matches.contains("Pedro Santos"))
        }

        @Test
        @DisplayName("toLikeStart should simulate starts with behavior")
        fun `toLikeStart should simulate starts with behavior`() {
            val searchTerm = "João"
            val pattern = searchTerm.toLikeStart()

            val databaseValues = listOf(
                "João Silva",
                "João Pedro",
                "Maria João",
                "Pedro João",
                "José Silva"
            )

            val matches = databaseValues.filter { it.startsWith(searchTerm) }

            assertEquals("João%", pattern)
            assertEquals(2, matches.size)
            assertTrue(matches.contains("João Silva"))
            assertTrue(matches.contains("João Pedro"))
            assertFalse(matches.contains("Maria João"))
            assertFalse(matches.contains("Pedro João"))
        }

        @Test
        @DisplayName("toLikeEnd should simulate ends with behavior")
        fun `toLikeEnd should simulate ends with behavior`() {
            val searchTerm = "Silva"
            val pattern = searchTerm.toLikeEnd()

            val databaseValues = listOf(
                "João Silva",
                "Maria Silva",
                "Silva João",
                "Pedro Silva Santos",
                "Ana Costa Silva"
            )

            val matches = databaseValues.filter { it.endsWith(searchTerm) }

            assertEquals("%Silva", pattern)
            assertEquals(3, matches.size)
            assertTrue(matches.contains("João Silva"))
            assertTrue(matches.contains("Maria Silva"))
            assertTrue(matches.contains("Ana Costa Silva"))
            assertFalse(matches.contains("Silva João"))
            assertFalse(matches.contains("Pedro Silva Santos"))
        }
    }
}