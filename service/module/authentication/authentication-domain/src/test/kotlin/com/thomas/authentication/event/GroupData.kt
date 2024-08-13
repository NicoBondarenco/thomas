package com.thomas.authentication.event

import com.thomas.authentication.data.entity.GroupAuthenticationEntity
import com.thomas.core.random.randomSecurityRoles
import com.thomas.core.random.randomString
import com.thomas.management.message.event.GroupDeletedEvent
import com.thomas.management.message.event.GroupUpsertedEvent
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID.randomUUID

val groupAuthenticationEntity: GroupAuthenticationEntity
    get() = GroupAuthenticationEntity(
        id = randomUUID(),
        groupName = randomString(10),
        groupDescription = randomString(10),
        isActive = listOf(true, false).random(),
        createdAt = now(UTC),
        updatedAt = now(UTC),
        groupRoles = randomSecurityRoles().toSet(),
    )

val groupUpsertedEvent: GroupUpsertedEvent
    get() = GroupUpsertedEvent(
        id = randomUUID(),
        groupName = randomString(10),
        groupDescription = randomString(10),
        isActive = listOf(true, false).random(),
        createdAt = now(UTC),
        updatedAt = now(UTC),
        groupRoles = randomSecurityRoles().toSet(),
    )

val groupDeletedEvent: GroupDeletedEvent
    get() = GroupDeletedEvent(
        id = randomUUID(),
    )
