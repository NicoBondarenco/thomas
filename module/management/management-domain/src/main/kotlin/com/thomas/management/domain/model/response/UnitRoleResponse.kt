package com.thomas.management.domain.model.response

import java.util.UUID

data class UnitRoleResponse(
    val id: UUID,
    val unitName: String,
    val fantasyName: String?,
    val unitRoles: Set<RoleGroupResponse>,
)