package com.thomas.core.security

import java.util.UUID

data class SecurityGroup(
    val groupId: UUID,
    val groupName: String,
    val groupRoles: List<SecurityRole>,
)
