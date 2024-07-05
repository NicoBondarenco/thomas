package com.thomas.management.domain.model.request

import com.thomas.core.model.security.SecurityRole


data class GroupCreateRequest(
    val groupName: String,
    val groupDescription: String?,
    val isActive: Boolean,
    val groupRoles: List<SecurityRole> = listOf(),
)
