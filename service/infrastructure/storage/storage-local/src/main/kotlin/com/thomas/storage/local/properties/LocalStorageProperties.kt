package com.thomas.storage.local.properties

import com.thomas.core.extension.throws
import com.thomas.storage.local.exception.LocalStorageException
import java.nio.file.InvalidPathException
import java.nio.file.Paths

data class LocalStorageProperties(
    val basePath: String
) {

    companion object {
        private val INVALID_CHARS = "'!@#$%¨&()+=[]{};ºª¬§¢£".toCharArray()
    }

    init {
        validate()
    }

    private fun validate() {
        validatePathReference()
        validatePathCharacters()
    }

    private fun validatePathReference() = try {
        Paths.get(basePath)
    } catch (ex: InvalidPathException) {
        throw LocalStorageException("Local Storage base path is invalid", ex)
    }

    private fun validatePathCharacters() = basePath.toCharArray().toSet().takeIf {
        INVALID_CHARS.intersect(it).isNotEmpty()
    }?.throws { LocalStorageException("Local Storage base path contains invalid characters") }

}
