package com.thomas.core.extension

import java.text.MessageFormat
import java.util.ResourceBundle

fun ResourceBundle.formattedMessage(key: String, vararg arguments: Any): String =
    MessageFormat.format(this.getString(key), *arguments)
