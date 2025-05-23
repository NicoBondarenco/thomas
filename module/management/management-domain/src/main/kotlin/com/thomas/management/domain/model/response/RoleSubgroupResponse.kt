package com.thomas.management.domain.model.response

data class RoleSubgroupResponse(
    val subgroupName: String,
    val subgroupLabel: String,
    val subgroupDescription: String,
    val subgroupOrder: Int,
    val subgroupRoles: Set<RoleResponse>,
)