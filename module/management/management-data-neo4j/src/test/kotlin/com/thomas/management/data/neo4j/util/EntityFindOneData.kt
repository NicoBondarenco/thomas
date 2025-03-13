package com.thomas.management.data.neo4j.util

import com.thomas.core.model.entity.BaseEntity
import java.util.UUID

data class EntityFindOneData<E : BaseEntity<E>>(
    val id: UUID,
    val organizationId: UUID,
    val entity: E?,
)