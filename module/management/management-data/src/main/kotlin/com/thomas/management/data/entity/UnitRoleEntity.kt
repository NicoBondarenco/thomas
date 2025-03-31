package com.thomas.management.data.entity

import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.security.SecurityUnitRole
import java.util.UUID
import java.util.UUID.randomUUID

data class UnitRoleEntity(
    override val id: UUID = randomUUID(),
    val roleUnit: UnitEntity,
    val roleList: Set<SecurityUnitRole>,
) : BaseEntity<UnitRoleEntity>()