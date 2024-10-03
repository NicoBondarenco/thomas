package com.thomas.management.domain.model.request

import com.thomas.core.model.security.SecurityRole

data class GroupUpdateRequest(
    val groupName: String,
    val groupDescription: String?,
    val isActive: Boolean,
    val groupRoles: Set<SecurityRole>,
)
