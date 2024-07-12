package com.thomas.management.data.entity

import com.thomas.core.extension.isBetween
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.entity.EntityValidation
import com.thomas.management.data.extension.GROUP_NAME_REGEX
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationGroupNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationGroupNameInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationInvalidEntityErrorMessage
import java.time.OffsetDateTime
import java.util.UUID

abstract class GroupBaseEntity : BaseEntity<GroupBaseEntity>() {

    abstract val groupName: String
    abstract val groupDescription: String?
    abstract val isActive: Boolean
    abstract val creatorId: UUID
    abstract val createdAt: OffsetDateTime
    abstract val updatedAt: OffsetDateTime

    companion object {
        internal const val MIN_NAME_SIZE = 5
        internal const val MAX_NAME_SIZE = 250
    }

    override fun errorMessage(): String = managementGroupValidationInvalidEntityErrorMessage()

    override fun validations(): List<EntityValidation<GroupBaseEntity>> = listOf(
        EntityValidation(
            GroupBaseEntity::groupName.name.toSnakeCase(),
            { managementGroupValidationGroupNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE) },
            { it.groupName.length.isBetween(MIN_NAME_SIZE, MAX_NAME_SIZE) }
        ),
        EntityValidation(
            GroupBaseEntity::groupName.name.toSnakeCase(),
            { managementGroupValidationGroupNameInvalidValue() },
            { GROUP_NAME_REGEX.matches(it.groupName) }
        )
    )

}
