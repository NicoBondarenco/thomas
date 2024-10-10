package com.thomas.core.model.security

import java.util.UUID

data class SecurityHub(
    val hubId: UUID,
    val hubName: String,
    val hubRoles: Set<SecurityRole> = setOf(),
)
