package com.thomas.management.data.entity

import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.security.SecurityUnitRole
import java.util.UUID
import java.util.UUID.randomUUID

data class UserUnitEntity(
    override val id: UUID = randomUUID(),
    val userUnit: UnitEntity,
    val userRoles: Set<SecurityUnitRole>,
) : BaseEntity<UserUnitEntity>()