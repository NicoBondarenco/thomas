package com.thomas.storage.data

data class FileResult(
    val path: String,
    val name: String,
    val extension: String,
    val overwrote: Boolean,
)
