package com.thomas.storage.extension

import com.thomas.core.extension.LETTERS_ONLY_REGEX_VALUE

@Suppress("MaximumLineLength", "MaxLineLength")
internal val FILE_NAME_REGEX = "^[${LETTERS_ONLY_REGEX_VALUE}0-9](?:[${LETTERS_ONLY_REGEX_VALUE}0-9\\-_. ]?[${LETTERS_ONLY_REGEX_VALUE}0-9]+)*$".toRegex()

@Suppress("MaximumLineLength", "MaxLineLength")
internal val FILE_PATH_REGEX = "^[${LETTERS_ONLY_REGEX_VALUE}0-9](?:[${LETTERS_ONLY_REGEX_VALUE}0-9\\-_. ]?[${LETTERS_ONLY_REGEX_VALUE}0-9]+)*$".toRegex()

internal val FILE_EXTENSION_REGEX = "[a-zA_Z0-9]+".toRegex()
