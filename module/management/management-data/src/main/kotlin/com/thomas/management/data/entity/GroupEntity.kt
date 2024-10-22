package com.thomas.management.data.entity

import com.thomas.core.extension.isBetween
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.entity.EntityValidation
import com.thomas.management.data.entity.info.BasicInfo
import com.thomas.management.data.extension.LEGAL_NAME_REGEX
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationGroupDescriptionInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationGroupDescriptionInvalidValue
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
    val groupDescription: String?,
    val groupOrganization: OrganizationEntity,
    override val isActive: Boolean = true,
    override val createdAt: OffsetDateTime = now(UTC),
    override val updatedAt: OffsetDateTime = now(UTC),
): BaseEntity<GroupEntity>(), BasicInfo {

    companion object {
        private const val MIN_NAME_SIZE = 5
        private const val MAX_NAME_SIZE = 250
        private const val MIN_DESCRIPTION_SIZE = 5
        private const val MAX_DESCRIPTION_SIZE = 1000
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
            { LEGAL_NAME_REGEX.matches(it.groupName) }
        ),
        EntityValidation(
            GroupEntity::groupDescription.name.toSnakeCase(),
            { managementGroupValidationGroupDescriptionInvalidLength(MIN_DESCRIPTION_SIZE, MAX_DESCRIPTION_SIZE) },
            { it.groupDescription == null || it.groupDescription.length.isBetween(MIN_DESCRIPTION_SIZE, MAX_DESCRIPTION_SIZE) }
        ),
        EntityValidation(
            GroupEntity::groupDescription.name.toSnakeCase(),
            { managementGroupValidationGroupDescriptionInvalidValue() },
            { it.groupDescription == null || LEGAL_NAME_REGEX.matches(it.groupDescription) }
        ),
    )

}
