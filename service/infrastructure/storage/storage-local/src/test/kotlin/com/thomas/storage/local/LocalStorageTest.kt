package com.thomas.storage.local

import com.thomas.storage.Storage
import com.thomas.storage.data.FileResource
import com.thomas.storage.data.FileResult
import com.thomas.storage.local.exception.LocalStorageException
import com.thomas.storage.local.i18n.LocalStorageMessageI18n.localStorageFileResourceExistentFileCannotOverwrite
import com.thomas.storage.local.i18n.LocalStorageMessageI18n.localStorageFileResourceWriteFileErrorWriting
import com.thomas.storage.local.properties.LocalStorageProperties
import io.mockk.mockk
import java.io.InputStream
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.deleteRecursively
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.assertThrows

@TestInstance(PER_CLASS)
class LocalStorageTest {

    private val testPath = "${System.getProperty("java.io.tmpdir")}\\test"
    private val properties = LocalStorageProperties(testPath)
    private val storage: Storage = LocalStorage(properties)

    @BeforeAll
    @OptIn(ExperimentalPathApi::class)
    fun beforeAll() {
        Paths.get(testPath).deleteRecursively()
    }

    @AfterAll
    @OptIn(ExperimentalPathApi::class)
    fun afterAll() {
        Paths.get(testPath).deleteRecursively()
    }

    @Test
    fun `WHEN file don't exists THEN should save the file`() {
        val stream = LocalStorageTest::class.java.getResourceAsStream("/files/kotlin.png")!!
        val resource = FileResource("/", "kotlin", "png", stream, false)
        val result = FileResult(resource.path, resource.name, resource.extension, false)
        assertEquals(result, storage.saveFile(resource))
    }

    @Test
    fun `WHEN file exists AND it's marked to overwrite THEN should replace the file`() {
        val stream = LocalStorageTest::class.java.getResourceAsStream("/files/java.png")!!
        val resource = FileResource("/", "java", "png", stream, true)
        storage.saveFile(resource)
        val result = FileResult(resource.path, resource.name, resource.extension, true)
        assertEquals(result, storage.saveFile(resource))
    }

    @Test
    fun `WHEN file exists AND it's not marked to overwrite THEN should throws LocalStorageException`() {
        val stream = LocalStorageTest::class.java.getResourceAsStream("/files/intellij.png")!!
        val resource = FileResource("/img", "intellij", "png", stream, false)
        storage.saveFile(resource)
        val exception = assertThrows<LocalStorageException> {
            storage.saveFile(resource)
        }

        assertEquals(localStorageFileResourceExistentFileCannotOverwrite(resource.fullPath), exception.message)
    }

    @Test
    fun `WHEN an error occurs while writing the file THEN should throws LocalStorageException`() {
        val stream = mockk<InputStream>()
        val resource = FileResource("/img", "error", "png", stream, false)
        val exception = assertThrows<LocalStorageException> {
            storage.saveFile(resource)
        }

        assertEquals(localStorageFileResourceWriteFileErrorWriting(resource.fullPath), exception.message)
    }

}
