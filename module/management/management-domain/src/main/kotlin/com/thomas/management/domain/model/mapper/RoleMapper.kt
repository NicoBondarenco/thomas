package com.thomas.management.domain.model.mapper

import com.thomas.core.model.security.SecurityRole
import com.thomas.core.model.security.SecurityRoleGroup
import com.thomas.core.model.security.SecurityRoleSubgroup
import com.thomas.management.domain.model.response.RoleGroupResponse
import com.thomas.management.domain.model.response.RoleResponse
import com.thomas.management.domain.model.response.RoleSubgroupResponse


fun Collection<SecurityRoleGroup<*, *, *>>.toRoleGroupResponses(): Set<RoleGroupResponse> =
    this.map { it.toRoleGroupResponse() }.filterEmptySubgroups().toSet()

fun Collection<RoleGroupResponse>.filterEmptySubgroups(): Set<RoleGroupResponse> = this.filter {
    it.groupSubgroups.isNotEmpty()
}.toSet()

fun SecurityRoleGroup<*, *, *>.toRoleGroupResponse(): RoleGroupResponse =
    RoleGroupResponse(
        groupName = this.groupName,
        groupLabel = this.name,
        groupDescription = this.groupDescription,
        groupOrder = this.groupOrder,
        groupSubgroups = this.subgroups().toRoleSubgroupResponses().filterEmptyRoles()
    )

fun Set<RoleSubgroupResponse>.filterEmptyRoles(): Set<RoleSubgroupResponse> = this.filter {
    it.subgroupRoles.isNotEmpty()
}.toSet()

fun Set<SecurityRoleSubgroup<*, *, *>>.toRoleSubgroupResponses(): Set<RoleSubgroupResponse> =
    this.map { it.toRoleSubgroupResponse() }.toSet()

fun SecurityRoleSubgroup<*, *, *>.toRoleSubgroupResponse(): RoleSubgroupResponse =
    RoleSubgroupResponse(
        subgroupName = this.subgroupName,
        subgroupLabel = this.name,
        subgroupDescription = this.subgroupDescription,
        subgroupOrder = this.subgroupOrder,
        subgroupRoles = this.roles().toRoleResponses()
    )

fun Set<SecurityRole<*, *, *>>.toRoleResponses(): Set<RoleResponse> =
    this.filter { it.roleDisplayable }.map { it.toRoleResponse() }.toSet()

fun SecurityRole<*, *, *>.toRoleResponse(): RoleResponse =
    RoleResponse(
        roleName = this.roleName,
        roleLabel = this.name,
        roleDescription = this.roleDescription,
        roleCode = this.roleCode,
        roleOrder = this.roleOrder
    )
