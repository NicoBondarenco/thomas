package com.thomas.database.neo4j.operator

internal fun String.negate() = "NOT(${this.trim()})"

internal fun String.unaccentLower() = "thomas.unaccentLower($this)"

internal fun String.toLike() = ".*$this.*"

