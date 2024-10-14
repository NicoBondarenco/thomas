package com.thomas.core.data

import java.util.UUID

data class MemberTestData(
    val id: UUID,
    val memberName: String,
    val memberDescription: String?,
    val isActive: Boolean,
)
