package com.thomas.management.data.entity

import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.model.security.SecurityRole
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID
import java.util.UUID.randomUUID

data class GroupCompleteEntity(
    override val id: UUID = randomUUID(),
    override val groupName: String,
    override val groupDescription: String? = null,
    override val isActive: Boolean = true,
    override val creatorId: UUID = currentUser.userId,
    override val createdAt: OffsetDateTime = now(UTC),
    override val updatedAt: OffsetDateTime = now(UTC),
    val groupRoles: Set<SecurityRole>,
) : GroupBaseEntity() {

    init {
        validate()
    }

}
