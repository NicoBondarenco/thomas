package com.thomas.storage.local.i18n

import com.thomas.core.i18n.BundleResolver

internal object LocalStorageMessageI18n : BundleResolver("strings/local-storage-strings") {

    fun localStorageFileResourceExistentFileCannotOverwrite(absolutePath: String): String =
        getFormattedMessage("local-storage.file-resource.existent-file.cannot-overwrite", absolutePath)

    fun localStorageFileResourceWriteFileErrorWriting(absolutePath: String): String =
        getFormattedMessage("local-storage.file-resource.write-file.error-writing", absolutePath)

}
