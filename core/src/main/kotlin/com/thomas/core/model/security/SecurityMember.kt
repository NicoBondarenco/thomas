package com.thomas.core.model.security

import java.util.UUID

data class SecurityMember(
    val memberId: UUID,
    val memberName: String,
    val memberRoles: Set<SecurityMemberRole> = setOf(),
)
