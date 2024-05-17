package com.thomas.storage.local

import com.thomas.storage.Storage
import com.thomas.storage.data.FileResource
import com.thomas.storage.data.FileResult
import com.thomas.storage.local.exception.LocalStorageException
import com.thomas.storage.local.i18n.LocalStorageMessageI18n.localStorageFileResourceExistentFileCannotOverwrite
import com.thomas.storage.local.i18n.LocalStorageMessageI18n.localStorageFileResourceWriteFileErrorWriting
import com.thomas.storage.local.properties.LocalStorageProperties
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

class LocalStorage(
    private val properties: LocalStorageProperties,
) : Storage {

    override fun saveFile(
        resource: FileResource
    ): FileResult {
        val path = resource.absolutePath()
        val exists = path.exists()
        path.writeFile(resource)
        return resource.toFileResult(exists)
    }

    @Suppress("TooGenericExceptionCaught")
    private fun Path.writeFile(resource: FileResource) = try {
        this.parent.createDirectories()
        this.toFile().writeBytes(resource.content.readBytes())
    } catch (ex: Exception) {
        throw LocalStorageException(localStorageFileResourceWriteFileErrorWriting(resource.fullPath), ex)
    }

    private fun FileResource.toFileResult(
        existent: Boolean,
    ) = FileResult(
        path = this.path,
        name = this.name,
        extension = this.extension,
        overwrote = existent && this.overwrite,
    )

    private fun FileResource.absolutePath(): Path = "${properties.basePath}/${this.fullPath}"
        .replace("\\", "/")
        .replace("//", "/")
        .let { Paths.get(it) }
        .takeIf { it.canSave(this.overwrite) }
        ?: throw LocalStorageException(localStorageFileResourceExistentFileCannotOverwrite(this.fullPath))

    private fun Path.canSave(overwrite: Boolean) = !this.exists() || overwrite

}
