package com.thomas.management.domain.adapter

import com.thomas.core.aspect.AspectClass
import com.thomas.core.authorization.authorized
import com.thomas.core.context.SessionContextHolder.currentOrganization
import com.thomas.core.model.security.SecurityOrganizationRoleGroup
import com.thomas.core.model.security.SecurityUnitRoleGroup
import com.thomas.management.data.repository.UnitRepository
import com.thomas.management.domain.RoleService
import com.thomas.management.domain.model.mapper.toRoleGroupResponses
import com.thomas.management.domain.model.mapper.toUnitRoleResponses
import com.thomas.management.domain.model.response.RoleGroupResponse
import com.thomas.management.domain.model.response.UnitRoleResponse

@AspectClass
class RoleServiceAdapter(
    private val unitRepository: UnitRepository
) : RoleService {

    override suspend fun organizationRoles(): Set<RoleGroupResponse> = authorized {
        SecurityOrganizationRoleGroup.entries.toRoleGroupResponses()
    }

    override suspend fun unitRoles(): Set<UnitRoleResponse> = authorized {
        unitRepository.allByOrganization(currentOrganization).filter {
            it.isActive
        }.takeIf {
            it.isNotEmpty()
        }?.let {
            val roles = SecurityUnitRoleGroup.entries.toRoleGroupResponses()
            it.toUnitRoleResponses(roles)
        }?: setOf()
    }

}
