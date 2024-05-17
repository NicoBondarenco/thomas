package com.thomas.storage.i18n

import com.thomas.core.i18n.BundleResolver

internal object StorageMessageI18n : BundleResolver("strings/storage-strings") {

    fun validationFileResourceEntityInvalidEntity(): String =
        getFormattedMessage("validation.file-resource.entity.invalid-entity")

    fun validationFileResourcePathInvalidValue(): String =
        getFormattedMessage("validation.file-resource.path.invalid-value")

    fun validationFileResourcePathInvalidSize(min: Int, max: Int): String =
        getFormattedMessage("validation.file-resource.path.invalid-size", min, max)

    fun validationFileResourceNameInvalidValue(): String =
        getFormattedMessage("validation.file-resource.name.invalid-value")

    fun validationFileResourceNameInvalidSize(min: Int, max: Int): String =
        getFormattedMessage("validation.file-resource.name.invalid-size", min, max)

    fun validationFileResourceExtensionInvalidValue(): String =
        getFormattedMessage("validation.file-resource.extension.invalid-value")

    fun validationFileResourceExtensionInvalidSize(min: Int, max: Int): String =
        getFormattedMessage("validation.file-resource.extension.invalid-size", min, max)

}
