package com.thomas.management.data.neo4j.util

data class EntitySameData(
    val result: Boolean,
    val action: suspend () -> Boolean
)