package com.thomas.management.data.entity

import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.extension.isBetween
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.entity.EntityValidation
import com.thomas.core.model.security.SecurityRole
import com.thomas.management.data.extension.GROUP_NAME_REGEX
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationGroupNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationGroupNameInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationInvalidEntityErrorMessage
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID
import java.util.UUID.randomUUID

data class GroupEntity(
    override val id: UUID = randomUUID(),
    val groupName: String,
    val groupDescription: String? = null,
    val isActive: Boolean = true,
    val creatorId: UUID = currentUser.userId,
    val createdAt: OffsetDateTime = now(UTC),
    val updatedAt: OffsetDateTime = now(UTC),
    val groupRoles: List<SecurityRole>,
) : BaseEntity<GroupEntity>() {

    companion object {
        internal const val MIN_NAME_SIZE = 5
        internal const val MAX_NAME_SIZE = 250
    }

    init {
        validate()
    }

    override fun errorMessage(): String = managementGroupValidationInvalidEntityErrorMessage()

    override fun validations(): List<EntityValidation<GroupEntity>> = listOf(
        EntityValidation(
            GroupEntity::groupName.name.toSnakeCase(),
            { managementGroupValidationGroupNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE) },
            { it.groupName.length.isBetween(MIN_NAME_SIZE, MAX_NAME_SIZE) }
        ),
        EntityValidation(
            GroupEntity::groupName.name.toSnakeCase(),
            { managementGroupValidationGroupNameInvalidValue() },
            { GROUP_NAME_REGEX.matches(it.groupName) }
        )
    )

}
