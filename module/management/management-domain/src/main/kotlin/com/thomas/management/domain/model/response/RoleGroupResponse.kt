package com.thomas.management.domain.model.response

data class RoleGroupResponse(
    val groupName: String,
    val groupLabel: String,
    val groupDescription: String,
    val groupOrder: Int,
    val groupSubgroups: Set<RoleSubgroupResponse>,
)