package com.thomas.management.spring.controller

import com.thomas.management.domain.RoleService
import com.thomas.management.domain.model.response.RoleGroupResponse
import com.thomas.management.domain.model.response.UnitRoleResponse
import com.thomas.management.spring.controller.ManagementPath.PRIVATE_API_V1_ROLE
import com.thomas.management.spring.controller.ManagementPath.PUBLIC_API_V1_ROLE_ORGANIZATION
import com.thomas.management.spring.controller.ManagementPath.PUBLIC_API_V1_ROLE_UNIT
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PRIVATE_API_V1_ROLE)
class RoleController(
    private val roleService: RoleService,
) {

    @GetMapping(PUBLIC_API_V1_ROLE_ORGANIZATION)
    suspend fun organizationRoles(): ResponseEntity<Set<RoleGroupResponse>> = ok(roleService.organizationRoles())

    @GetMapping(PUBLIC_API_V1_ROLE_UNIT)
    suspend fun unitRoles(): ResponseEntity<Set<UnitRoleResponse>> = ok(roleService.unitRoles())

}
