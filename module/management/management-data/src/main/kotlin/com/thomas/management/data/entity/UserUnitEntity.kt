package com.thomas.management.data.entity

import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.security.SecurityUnitRole
import java.util.UUID
import java.util.UUID.randomUUID

data class UserUnitEntity(
    override val id: UUID = randomUUID(),
    val groupUnit: UnitEntity,
    val groupRoles: Set<SecurityUnitRole>,
) : BaseEntity<UserUnitEntity>()