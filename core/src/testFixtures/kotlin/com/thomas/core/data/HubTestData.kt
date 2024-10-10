package com.thomas.core.data

import java.util.UUID

data class HubTestData(
    val id: UUID,
    val hubName: String,
    val hubDescription: String?,
    val isActive: Boolean,
)
