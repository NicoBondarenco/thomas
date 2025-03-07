package com.thomas.management.data.neo4j.util

import com.thomas.management.data.entity.UnitEntity
import java.util.UUID

data class UnitFindOneData(
    val id: UUID,
    val organizationId: UUID,
    val node: UnitEntity?,
)