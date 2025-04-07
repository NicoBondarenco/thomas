package com.thomas.management.data.neo4j.model.mapper

import com.thomas.management.data.entity.PasswordResetEntity
import com.thomas.management.data.neo4j.model.node.PasswordResetNode


fun PasswordResetEntity.toPasswordResetNode() = PasswordResetNode(
    id = this.id,
    userId = this.userId,
    resetToken = this.resetToken,
    expiresOn = this.expiresOn.toZonedDateTime(),
    createdAt = this.createdAt.toZonedDateTime(),
    updatedAt = this.updatedAt.toZonedDateTime(),
)

fun PasswordResetNode.toPasswordResetEntity() = PasswordResetEntity(
    id = this.id,
    userId = this.userId,
    resetToken = this.resetToken,
    expiresOn = this.expiresOn.toOffsetDateTime(),
    createdAt = this.createdAt.toOffsetDateTime(),
    updatedAt = this.updatedAt.toOffsetDateTime(),
)
