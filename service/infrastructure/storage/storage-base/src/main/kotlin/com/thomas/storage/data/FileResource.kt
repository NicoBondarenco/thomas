package com.thomas.storage.data

import com.thomas.core.extension.throws
import com.thomas.storage.exception.FileResourceException
import com.thomas.storage.extension.FILE_EXTENSION_REGEX
import com.thomas.storage.extension.FILE_NAME_REGEX
import com.thomas.storage.extension.FILE_PATH_REGEX
import com.thomas.storage.i18n.StorageMessageI18n.validationFileResourceEntityInvalidEntity
import com.thomas.storage.i18n.StorageMessageI18n.validationFileResourceExtensionInvalidSize
import com.thomas.storage.i18n.StorageMessageI18n.validationFileResourceExtensionInvalidValue
import com.thomas.storage.i18n.StorageMessageI18n.validationFileResourceNameInvalidSize
import com.thomas.storage.i18n.StorageMessageI18n.validationFileResourceNameInvalidValue
import com.thomas.storage.i18n.StorageMessageI18n.validationFileResourcePathInvalidSize
import com.thomas.storage.i18n.StorageMessageI18n.validationFileResourcePathInvalidValue
import java.io.InputStream

data class FileResource(
    val path: String = "",
    val name: String,
    val extension: String,
    val content: InputStream,
    val overwrite: Boolean,
) {

    companion object {
        private const val MIN_FILE_NAME_SIZE = 1
        private const val MAX_FILE_NAME_SIZE = 70
        private const val MIN_FILE_EXTENSION_SIZE = 1
        private const val MAX_FILE_EXTENSION_SIZE = 10
        private const val MIN_PATH_FOLDER_SIZE = 1
        private const val MAX_PATH_FOLDER_SIZE = 40
    }

    init {
        validate()
    }

    private fun validate() {
        listOfNotNull(
            validatePathRegex(),
            validatePathSize(),
            validateNameRegex(),
            validateNameSize(),
            validateExtensionRegex(),
            validateExtensionSize(),
        ).takeIf {
            it.isNotEmpty()
        }?.throws {
            FileResourceException(validationFileResourceEntityInvalidEntity(), it)
        }
    }

    private val normalizedPath: String
        get() = "/${this.path.replace("\\", "/")}/".let {
            var result = it
            while (result.contains("//")) {
                result = result.replace("//", "/")
            }
            result
        }

    private val pathFolders: List<String>
        get() = normalizedPath.takeIf {
            it.length > 1
        }?.let {
            it.substring(1, it.length - 1)
        }?.split("/") ?: listOf()

    private val pathIsRoot: Boolean
        get() = normalizedPath == "/"

    val fullPath: String
        get() = "/${(pathFolders + "$name.$extension").joinToString("/")}"

    private fun validatePathRegex(): String? =
        validationFileResourcePathInvalidValue().takeIf {
            !pathIsRoot && pathFolders.any { f ->
                f.length >= MIN_PATH_FOLDER_SIZE && !f.matches(FILE_PATH_REGEX)
            }
        }

    private fun validatePathSize(): String? =
        validationFileResourcePathInvalidSize(MIN_PATH_FOLDER_SIZE, MAX_PATH_FOLDER_SIZE).takeIf {
            !pathIsRoot && pathFolders.any { f ->
                f.length > MAX_PATH_FOLDER_SIZE
            }
        }

    private fun validateNameRegex(): String? =
        validationFileResourceNameInvalidValue().takeIf {
            name.length >= MIN_FILE_NAME_SIZE && !name.matches(FILE_NAME_REGEX)
        }

    private fun validateNameSize(): String? =
        validationFileResourceNameInvalidSize(MIN_FILE_NAME_SIZE, MAX_FILE_NAME_SIZE).takeIf {
            name.length !in MIN_FILE_NAME_SIZE..MAX_FILE_NAME_SIZE
        }

    private fun validateExtensionRegex(): String? =
        validationFileResourceExtensionInvalidValue().takeIf {
            extension.length >= MIN_FILE_EXTENSION_SIZE && !extension.matches(FILE_EXTENSION_REGEX)
        }

    private fun validateExtensionSize(): String? =
        validationFileResourceExtensionInvalidSize(MIN_FILE_EXTENSION_SIZE, MAX_FILE_EXTENSION_SIZE).takeIf {
            extension.length !in MIN_FILE_EXTENSION_SIZE..MAX_FILE_EXTENSION_SIZE
        }

}
