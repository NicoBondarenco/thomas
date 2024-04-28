package com.thomas.authentication.jwt.auth0.data

import com.thomas.core.model.security.SecurityRole
import java.util.UUID

data class GroupAuthentication(
    val id: UUID,
    val groupName: String,
    val groupDescription: String?,
    val isActive: Boolean,
    val groupRoles: List<SecurityRole>,
)
