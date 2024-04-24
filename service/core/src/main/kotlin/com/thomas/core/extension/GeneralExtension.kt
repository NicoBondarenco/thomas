package com.thomas.core.extension

fun <T, R : Throwable> T.throws(block: (T) -> R): Nothing = throw block(this)
