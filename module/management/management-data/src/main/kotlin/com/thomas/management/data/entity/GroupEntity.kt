package com.thomas.management.data.entity

import com.thomas.core.extension.isBetween
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.entity.EntityValidation
import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.management.data.entity.info.BasicInfo
import com.thomas.management.data.extension.LEGAL_NAME_REGEX
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationGroupDescriptionInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationGroupDescriptionInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationGroupNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationGroupNameInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementGroupValidationInvalidEntityErrorMessage

abstract class GroupEntity : BaseEntity<GroupEntity>(), BasicInfo {

    abstract val groupName: String
    abstract val groupDescription: String?
    abstract val groupOrganization: OrganizationEntity
    abstract val organizationRoles: Set<SecurityOrganizationRole>

    companion object {
        private const val MIN_NAME_SIZE = 5
        private const val MAX_NAME_SIZE = 250
        private const val MIN_DESCRIPTION_SIZE = 5
        private const val MAX_DESCRIPTION_SIZE = 1000
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
            { it.groupDescription == null || it.groupDescription!!.length.isBetween(MIN_DESCRIPTION_SIZE, MAX_DESCRIPTION_SIZE) }
        ),
        EntityValidation(
            GroupEntity::groupDescription.name.toSnakeCase(),
            { managementGroupValidationGroupDescriptionInvalidValue() },
            { it.groupDescription == null || LEGAL_NAME_REGEX.matches(it.groupDescription!!) }
        ),
    )

}
