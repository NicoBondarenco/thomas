package com.thomas.management.domain.model.response

data class RoleResponse(
    val roleName: String,
    val roleLabel: String,
    val roleDescription: String,
    val roleCode: Int,
    val roleOrder: Int,
)