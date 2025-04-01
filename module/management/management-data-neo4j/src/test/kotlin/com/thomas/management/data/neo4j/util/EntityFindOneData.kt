package com.thomas.management.data.neo4j.util

import java.util.UUID

data class EntityFindOneData<K, E>(
    val field: K,
    val organizationId: UUID,
    val entity: E?,
)