package com.thomas.management.domain

import com.thomas.management.domain.model.response.RoleGroupResponse
import com.thomas.management.domain.model.response.UnitRoleResponse

interface RoleService {

    suspend fun organizationRoles(): Set<RoleGroupResponse>

    suspend fun unitRoles(): Set<UnitRoleResponse>

}
