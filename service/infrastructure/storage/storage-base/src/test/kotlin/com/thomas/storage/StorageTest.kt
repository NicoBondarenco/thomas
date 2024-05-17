package com.thomas.storage

import com.thomas.storage.data.FileResource
import com.thomas.storage.data.FileResult
import io.mockk.mockk
import java.io.InputStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StorageTest {

    private val content = mockk<InputStream>()

    private val storage = object : Storage {

        override fun saveFile(
            resource: FileResource
        ): FileResult = FileResult(
            path = resource.path,
            name = resource.name,
            extension = resource.extension,
            overwrote = resource.overwrite,
        )
    }

    @Test
    fun `Storage save file returns save result`() {
        val resource = FileResource("path", "test", "txt", content, true)
        val expected = FileResult(
            path = resource.path,
            name = resource.name,
            extension = resource.extension,
            overwrote = resource.overwrite,
        )
        val result = storage.saveFile(resource)
        assertEquals(expected, result)
    }

}
