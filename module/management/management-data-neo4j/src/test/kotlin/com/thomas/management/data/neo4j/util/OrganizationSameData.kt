package com.thomas.management.data.neo4j.util

data class OrganizationSameData(
    val result: Boolean,
    val action: suspend () -> Boolean
)