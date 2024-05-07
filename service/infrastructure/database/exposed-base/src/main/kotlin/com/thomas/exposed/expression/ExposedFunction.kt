package com.thomas.exposed.expression

import org.jetbrains.exposed.sql.Expression

fun <T : String?> Expression<T>.unaccentLower(): UnaccentLower<T> = UnaccentLower(this)

fun String.toLikeParameter() = "%$this%"
