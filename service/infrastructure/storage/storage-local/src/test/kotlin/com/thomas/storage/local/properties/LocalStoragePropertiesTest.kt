package com.thomas.storage.local.properties

import com.thomas.storage.local.exception.LocalStorageException
import java.nio.file.InvalidPathException
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class LocalStoragePropertiesTest {

    companion object {

        @JvmStatic
        fun validPaths() = listOf(
            Arguments.of("C:/storage/pics//"),
            Arguments.of("C:/storage/pics/"),
            Arguments.of("C:/storage/pics"),
            Arguments.of("C:/storage/user.pics"),
            Arguments.of("C:/"),
            Arguments.of("C:"),
            Arguments.of("/app/storage/"),
            Arguments.of("/app/storage"),
            Arguments.of("/app"),
            Arguments.of("/"),
            Arguments.of("C:\\storage\\pics\\"),
            Arguments.of("C:\\storage\\pics\\\\"),
            Arguments.of("C:\\storage\\pics"),
            Arguments.of("C:\\storage\\user.pics"),
            Arguments.of("C:\\"),
            Arguments.of("C:"),
            Arguments.of("\\app\\storage\\"),
            Arguments.of("\\app\\storage"),
            Arguments.of("\\app"),
            Arguments.of("\\"),
        )

        @JvmStatic
        fun invalidPaths() = listOf(
            Arguments.of(">C:/storage/pics/"),
            Arguments.of("C:/sto:rage/pics/"),
            Arguments.of("C:/storage\""),
            Arguments.of("C:/storage>/pics"),
            Arguments.of("C:/<storage>"),
            Arguments.of("C:/storage?/pics"),
            Arguments.of("C:/storage/*pics"),
            Arguments.of(">/storage/pics/"),
            Arguments.of("/sto:rage/pics/"),
            Arguments.of("/storage\""),
            Arguments.of("/storage>/pics"),
            Arguments.of(":/storage/pics"),
            Arguments.of("/<storage>"),
            Arguments.of("/storage?/pics"),
            Arguments.of("/storage/*pics"),
            Arguments.of(">C:\\storage\\pics\\"),
            Arguments.of("C:\\sto:rage\\pics\\"),
            Arguments.of("C:\\storage\""),
            Arguments.of("C:\\storage>\\pics"),
            Arguments.of("C:\\<storage>"),
            Arguments.of("C:\\storage?\\pics"),
            Arguments.of("C:\\storage\\*pics"),
            Arguments.of(">\\storage\\pics\\"),
            Arguments.of("\\sto:rage\\pics\\"),
            Arguments.of("\\storage\""),
            Arguments.of("\\storage>\\pics"),
            Arguments.of(":\\storage\\pics"),
            Arguments.of("\\<storage>"),
            Arguments.of("\\storage?\\pics"),
            Arguments.of("\\storage\\*pics"),
        )

        @JvmStatic
        fun invalidPathsChars() = "'!@#$%¨&()+=[]{};ºª¬§¢£".toCharArray().let {
            it.map {
                Arguments.of("C:/storage$it/pics/")
            } + it.map {
                Arguments.of("/storage$it/pics/")
            } + it.map {
                Arguments.of("\\storage$it\\pic")
            } + it.map {
                Arguments.of("C:\\storage$it\\pics")
            }
        }

    }

    @ParameterizedTest
    @MethodSource("validPaths")
    fun `WHEN base path is valid THEN should not throws exception`(path: String) {
        assertDoesNotThrow { LocalStorageProperties(path) }
    }

    @ParameterizedTest
    @MethodSource("invalidPaths")
    fun `WHEN base path is invalid THEN should throws LocalStorageException`(path: String) {
        val exception = assertThrows<LocalStorageException> { LocalStorageProperties(path) }
        assertEquals("Local Storage base path is invalid", exception.message)
        assertEquals(InvalidPathException::class, exception.cause!!::class)
    }

    @ParameterizedTest
    @MethodSource("invalidPathsChars")
    fun `WHEN base path has invalid characters THEN should throws LocalStorageException`(path: String) {
        val exception = assertThrows<LocalStorageException> { LocalStorageProperties(path) }
        assertEquals("Local Storage base path contains invalid characters", exception.message)
    }

}
