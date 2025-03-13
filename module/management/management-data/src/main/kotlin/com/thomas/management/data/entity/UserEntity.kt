package com.thomas.management.data.entity

import com.thomas.core.extension.isBetween
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.entity.EntityValidation
import com.thomas.core.model.general.Gender
import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.management.data.entity.info.BasicInfo
import com.thomas.management.data.entity.info.ContactInfo
import com.thomas.management.data.extension.NATURAL_NAME_REGEX
import com.thomas.management.data.extension.isValidDocumentNumber
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationDocumentNumberInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationFirstNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationFirstNameInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationInvalidEntityErrorMessage
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationLastNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationLastNameInvalidValue
import java.time.LocalDate

abstract class UserEntity : BaseEntity<UserEntity>(), ContactInfo, BasicInfo {

    abstract val firstName: String
    abstract val lastName: String
    abstract val documentNumber: String
    abstract val profilePhoto: String?
    abstract val userGender: Gender?
    abstract val birthDate: LocalDate?
    abstract val passwordSalt: String
    abstract val passwordHash: String
    abstract val userOrganization: OrganizationEntity
    abstract val organizationRoles: Set<SecurityOrganizationRole>

    companion object {
        internal const val MIN_NAME_SIZE = 2
        internal const val MAX_NAME_SIZE = 250
    }

    override fun errorMessage(): String = managementUserValidationInvalidEntityErrorMessage()

    override fun validations(): List<EntityValidation<UserEntity>> = listOf<EntityValidation<UserEntity>>(
        EntityValidation(
            UserEntity::firstName.name.toSnakeCase(),
            { managementUserValidationFirstNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE) },
            { it.firstName.length.isBetween(MIN_NAME_SIZE, MAX_NAME_SIZE) }
        ),
        EntityValidation(
            UserEntity::firstName.name.toSnakeCase(),
            { managementUserValidationFirstNameInvalidValue() },
            { NATURAL_NAME_REGEX.matches(it.firstName) }
        ),
        EntityValidation(
            UserEntity::lastName.name.toSnakeCase(),
            { managementUserValidationLastNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE) },
            { it.lastName.length.isBetween(MIN_NAME_SIZE, MAX_NAME_SIZE) }
        ),
        EntityValidation(
            UserEntity::lastName.name.toSnakeCase(),
            { managementUserValidationLastNameInvalidValue() },
            { NATURAL_NAME_REGEX.matches(it.lastName) }
        ),
        EntityValidation(
            UserEntity::documentNumber.name.toSnakeCase(),
            { managementUserValidationDocumentNumberInvalidValue() },
            { it.documentNumber.isValidDocumentNumber() }
        ),
    ) + contactInfoValidations()

}
