package com.thomas.database.komapper.extension

fun String.toLike() = "%$this%"

fun String.toLikeStart() = "$this%"

fun String.toLikeEnd() = "%$this"
