package com.thomas.management.data.entity

import com.thomas.core.extension.isBetween
import com.thomas.core.extension.onlyNumbers
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.entity.EntityValidation
import com.thomas.core.model.general.Gender
import com.thomas.management.data.extension.EMAIL_REGEX
import com.thomas.management.data.extension.PERSON_NAME_REGEX
import com.thomas.management.data.extension.isValidDocumentNumber
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationDocumentNumberInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationFirstNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationFirstNameInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationInvalidEntityErrorMessage
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationLastNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationLastNameInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationMainEmailInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationPhoneNumberInvalidCharacter
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

abstract class UserBaseEntity : BaseEntity<UserBaseEntity>() {

    abstract val firstName: String
    abstract val lastName: String
    abstract val mainEmail: String
    abstract val documentNumber: String
    abstract val phoneNumber: String?
    abstract val profilePhoto: String?
    abstract val birthDate: LocalDate?
    abstract val userGender: Gender?
    abstract val isActive: Boolean
    abstract val creatorId: UUID
    abstract val createdAt: OffsetDateTime
    abstract val updatedAt: OffsetDateTime

    companion object {
        internal const val MIN_NAME_SIZE = 2
        internal const val MAX_NAME_SIZE = 250
    }

    override fun errorMessage(): String = managementUserValidationInvalidEntityErrorMessage()

    override fun validations(): List<EntityValidation<UserBaseEntity>> = listOf(
        EntityValidation(
            UserBaseEntity::firstName.name.toSnakeCase(),
            { managementUserValidationFirstNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE) },
            { it.firstName.length.isBetween(MIN_NAME_SIZE, MAX_NAME_SIZE) }
        ),
        EntityValidation(
            UserBaseEntity::firstName.name.toSnakeCase(),
            { managementUserValidationFirstNameInvalidValue() },
            { PERSON_NAME_REGEX.matches(it.firstName) }
        ),
        EntityValidation(
            UserBaseEntity::lastName.name.toSnakeCase(),
            { managementUserValidationLastNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE) },
            { it.lastName.length.isBetween(MIN_NAME_SIZE, MAX_NAME_SIZE) }
        ),
        EntityValidation(
            UserBaseEntity::lastName.name.toSnakeCase(),
            { managementUserValidationLastNameInvalidValue() },
            { PERSON_NAME_REGEX.matches(it.lastName) }
        ),
        EntityValidation(
            UserBaseEntity::mainEmail.name.toSnakeCase(),
            { managementUserValidationMainEmailInvalidValue() },
            { EMAIL_REGEX.matches(it.mainEmail) }
        ),
        EntityValidation(
            UserBaseEntity::documentNumber.name.toSnakeCase(),
            { managementUserValidationDocumentNumberInvalidValue() },
            { it.documentNumber.isValidDocumentNumber() }
        ),
        EntityValidation(
            UserBaseEntity::phoneNumber.name.toSnakeCase(),
            { managementUserValidationPhoneNumberInvalidCharacter() },
            { it.phoneNumber == it.phoneNumber?.onlyNumbers() && (it.phoneNumber?.trim()?.isNotEmpty() ?: true) }
        )
    )

    override fun toString(): String {
        return """UserBaseEntity(
        firstName='$firstName',
        lastName='$lastName',
        mainEmail='$mainEmail',
        documentNumber='$documentNumber',
        phoneNumber=$phoneNumber,
        profilePhoto=$profilePhoto,
        birthDate=$birthDate,
        userGender=$userGender,
        isActive=$isActive,
        creatorId=$creatorId,
        createdAt=$createdAt,
        updatedAt=$updatedAt
        )
        """.trimIndent()
    }

}
