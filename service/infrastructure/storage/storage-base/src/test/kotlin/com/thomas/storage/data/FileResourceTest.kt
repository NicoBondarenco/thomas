package com.thomas.storage.data

import com.thomas.storage.exception.FileResourceException
import com.thomas.storage.i18n.StorageMessageI18n.validationFileResourceEntityInvalidEntity
import com.thomas.storage.i18n.StorageMessageI18n.validationFileResourceExtensionInvalidSize
import com.thomas.storage.i18n.StorageMessageI18n.validationFileResourceExtensionInvalidValue
import com.thomas.storage.i18n.StorageMessageI18n.validationFileResourceNameInvalidSize
import com.thomas.storage.i18n.StorageMessageI18n.validationFileResourceNameInvalidValue
import com.thomas.storage.i18n.StorageMessageI18n.validationFileResourcePathInvalidSize
import com.thomas.storage.i18n.StorageMessageI18n.validationFileResourcePathInvalidValue
import io.mockk.mockk
import java.io.InputStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class FileResourceTest {

    companion object {

        @JvmStatic
        fun validFiles(): List<Arguments> = listOf(
            Arguments.of("", "file", "txt", "/file.txt"),
            Arguments.of("/", "file", "txt", "/file.txt"),
            Arguments.of("/path", "file", "txt", "/path/file.txt"),
            Arguments.of("path/second", "file", "txt", "/path/second/file.txt"),
            Arguments.of("path/second/", "file", "txt", "/path/second/file.txt"),
            Arguments.of("/Âçãóìñ 123/1/qwer-ty/Bõà_a 1", "Âçãóìñ 123_file-TÊí", "4com", "/Âçãóìñ 123/1/qwer-ty/Bõà_a 1/Âçãóìñ 123_file-TÊí.4com"),
            Arguments.of("/1", "1", "1", "/1/1.1"),
            Arguments.of("/a", "a", "a", "/a/a.a"),
            Arguments.of("\\", "file", "txt", "/file.txt"),
            Arguments.of("\\path", "file", "txt", "/path/file.txt"),
            Arguments.of("path\\second", "file", "txt", "/path/second/file.txt"),
            Arguments.of("path\\second\\", "file", "txt", "/path/second/file.txt"),
            Arguments.of("\\Âçãóìñ 1\\1\\qwer-ty\\Bõà_as 1", "Âçãóìñ 123_f-TÊí", "4c", "/Âçãóìñ 1/1/qwer-ty/Bõà_as 1/Âçãóìñ 123_f-TÊí.4c"),
            Arguments.of("\\1", "1", "1", "/1/1.1"),
            Arguments.of("\\a", "a", "a", "/a/a.a"),
        )

        @JvmStatic
        fun invalidFilePathsRegex(): List<Arguments> = listOf(
            Arguments.of("/path/sec:ond"),
            Arguments.of("path/second>"),
            Arguments.of("/pa;th"),
            Arguments.of("/path]/"),
            Arguments.of("/path/second/%another"),
            Arguments.of("\\path\\sec:ond"),
            Arguments.of("path\\second>"),
            Arguments.of("\\pa;th"),
            Arguments.of("\\path]\\"),
            Arguments.of("\\path\\second\\%another"),
        )

        @JvmStatic
        fun invalidFilePathsBlank(): List<Arguments> = listOf(
            Arguments.of("/path/ /"),
            Arguments.of(" "),
            Arguments.of("/ /path"),
            Arguments.of("/path/ /second"),
            Arguments.of("\\path\\ \\"),
            Arguments.of(" "),
            Arguments.of("\\ \\path"),
            Arguments.of("\\path\\ \\second"),
        )

        @JvmStatic
        fun invalidFileNamesRegex(): List<Arguments> = listOf(
            Arguments.of("sec:ond"),
            Arguments.of("second>"),
            Arguments.of("fi;le"),
            Arguments.of("file]"),
            Arguments.of("/file"),
        )

        @JvmStatic
        fun invalidFileExtensionRegex(): List<Arguments> = listOf(
            Arguments.of("açtion"),
            Arguments.of("actíon"),
            Arguments.of("sec:ond"),
            Arguments.of("second>"),
            Arguments.of("ex;tension"),
            Arguments.of("ext]"),
            Arguments.of("/extension"),
            Arguments.of("exten.sion"),
        )

    }

    private val content = mockk<InputStream>()

    @ParameterizedTest
    @MethodSource("validFiles")
    fun `WHEN a file name, extension and path are valid THEN should not throws exception`(
        path: String,
        name: String,
        extension: String,
        full: String
    ) {
        val file = assertDoesNotThrow { FileResource(path, name, extension, content, true) }
        assertEquals(full, file.fullPath)
    }

    @ParameterizedTest
    @MethodSource("invalidFilePathsRegex")
    fun `WHEN a file path folder contains invalid characters THEN should throws FileResourceException`(
        path: String,
    ) {
        val exception = assertThrows<FileResourceException> { FileResource(path, "file", "txt", content, true) }
        assertEquals(validationFileResourceEntityInvalidEntity(), exception.message)
        val errors = (exception.detail as List<String>)
        assertEquals(1, errors.size)
        assertEquals(validationFileResourcePathInvalidValue(), errors.first())
    }

    @ParameterizedTest
    @MethodSource("invalidFilePathsBlank")
    fun `WHEN a file path folder is blank THEN should throws FileResourceException`(
        path: String,
    ) {
        val exception = assertThrows<FileResourceException> { FileResource(path, "file", "txt", content, true) }
        assertEquals(validationFileResourceEntityInvalidEntity(), exception.message)
        val errors = (exception.detail as List<String>)
        assertEquals(1, errors.size)
        assertEquals(validationFileResourcePathInvalidValue(), errors.first())
    }

    @Test
    fun `WHEN a file path folder has more than 40 characters THEN should throws FileResourceException`() {
        val exception = assertThrows<FileResourceException> {
            FileResource("/98765432109876543210987654321098765432100", "file", "txt", content, true)
        }
        assertEquals(validationFileResourceEntityInvalidEntity(), exception.message)
        val errors = (exception.detail as List<String>)
        assertEquals(1, errors.size)
        assertEquals(validationFileResourcePathInvalidSize(1, 40), errors.first())
    }

    @ParameterizedTest
    @MethodSource("invalidFileNamesRegex")
    fun `WHEN a file name contains invalid characters THEN should throws FileResourceException`(name: String) {
        val exception = assertThrows<FileResourceException> { FileResource("path", name, "txt", content, true) }
        assertEquals(validationFileResourceEntityInvalidEntity(), exception.message)
        val errors = (exception.detail as List<String>)
        assertEquals(1, errors.size)
        assertEquals(validationFileResourceNameInvalidValue(), errors.first())
    }

    @Test
    fun `WHEN a file name is blank THEN should throws FileResourceException`() {
        val exception = assertThrows<FileResourceException> { FileResource("path", " ", "txt", content, true) }
        assertEquals(validationFileResourceEntityInvalidEntity(), exception.message)
        val errors = (exception.detail as List<String>)
        assertEquals(1, errors.size)
        assertEquals(validationFileResourceNameInvalidValue(), errors.first())
    }

    @Test
    fun `WHEN a file name has less than 1 character THEN should throws FileResourceException`() {
        val exception = assertThrows<FileResourceException> { FileResource("path", "", "txt", content, true) }
        assertEquals(validationFileResourceEntityInvalidEntity(), exception.message)
        val errors = (exception.detail as List<String>)
        assertEquals(1, errors.size)
        assertEquals(validationFileResourceNameInvalidSize(1, 70), errors.first())
    }

    @Test
    fun `WHEN a file name has more than 70 characters THEN should throws FileResourceException`() {
        val exception = assertThrows<FileResourceException> {
            FileResource("path", "a".repeat(71), "txt", content, true)
        }
        assertEquals(validationFileResourceEntityInvalidEntity(), exception.message)
        val errors = (exception.detail as List<String>)
        assertEquals(1, errors.size)
        assertEquals(validationFileResourceNameInvalidSize(1, 70), errors.first())
    }

    @ParameterizedTest
    @MethodSource("invalidFileExtensionRegex")
    fun `WHEN a file extension contains invalid characters THEN should throws FileResourceException`(extension: String) {
        val exception = assertThrows<FileResourceException> { FileResource("path", "file", extension, content, true) }
        assertEquals(validationFileResourceEntityInvalidEntity(), exception.message)
        val errors = (exception.detail as List<String>)
        assertEquals(1, errors.size)
        assertEquals(validationFileResourceExtensionInvalidValue(), errors.first())
    }

    @Test
    fun `WHEN a file extension is blank THEN should throws FileResourceException`() {
        val exception = assertThrows<FileResourceException> { FileResource("path", "file", " ", content, true) }
        assertEquals(validationFileResourceEntityInvalidEntity(), exception.message)
        val errors = (exception.detail as List<String>)
        assertEquals(1, errors.size)
        assertEquals(validationFileResourceExtensionInvalidValue(), errors.first())
    }

    @Test
    fun `WHEN a file extension has less than 1 character THEN should throws FileResourceException`() {
        val exception = assertThrows<FileResourceException> { FileResource("path", "file", "", content, true) }
        assertEquals(validationFileResourceEntityInvalidEntity(), exception.message)
        val errors = (exception.detail as List<String>)
        assertEquals(1, errors.size)
        assertEquals(validationFileResourceExtensionInvalidSize(1, 10), errors.first())
    }

    @Test
    fun `WHEN a file extension has more than 10 characters THEN should throws FileResourceException`() {
        val exception = assertThrows<FileResourceException> {
            FileResource("path", "file", "a".repeat(11), content, true)
        }
        assertEquals(validationFileResourceEntityInvalidEntity(), exception.message)
        val errors = (exception.detail as List<String>)
        assertEquals(1, errors.size)
        assertEquals(validationFileResourceExtensionInvalidSize(1, 10), errors.first())
    }

}
