package com.thomas.management.data.neo4j.util

import java.util.UUID

data class EntityFindOneData<E>(
    val id: UUID,
    val organizationId: UUID,
    val entity: E?,
)