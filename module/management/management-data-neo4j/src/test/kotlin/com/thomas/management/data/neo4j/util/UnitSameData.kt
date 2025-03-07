package com.thomas.management.data.neo4j.util

data class UnitSameData(
    val result: Boolean,
    val action: suspend () -> Boolean
)