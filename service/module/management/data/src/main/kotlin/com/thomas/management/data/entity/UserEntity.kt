package com.thomas.management.data.entity


import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.extension.isBetween
import com.thomas.core.extension.onlyNumbers
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.entity.EntityValidation
import com.thomas.core.model.general.Gender
import com.thomas.core.model.general.UserProfile
import com.thomas.core.model.general.UserProfile.COMMON
import com.thomas.core.model.security.SecurityRole
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
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID
import java.util.UUID.randomUUID

data class UserEntity(
    override val id: UUID = randomUUID(),
    val firstName: String,
    val lastName: String,
    val mainEmail: String,
    val documentNumber: String,
    val phoneNumber: String? = null,
    val profileType: UserProfile = COMMON,
    val profilePhoto: String? = null,
    val birthDate: LocalDate? = null,
    val userGender: Gender? = null,
    val isActive: Boolean = true,
    val creatorId: UUID = currentUser.userId,
    val addressId: UUID? = null,
    val createdAt: OffsetDateTime = now(UTC),
    val updatedAt: OffsetDateTime = now(UTC),
    val userRoles: MutableList<SecurityRole>,
) : BaseEntity<UserEntity>() {

    companion object {
        internal const val MIN_NAME_SIZE = 2
        internal const val MAX_NAME_SIZE = 250
    }

    init {
        validate()
    }

    override fun errorMessage(): String = managementUserValidationInvalidEntityErrorMessage()

    override fun validations(): List<EntityValidation<UserEntity>> = listOf(
        EntityValidation(
            UserEntity::firstName.name.toSnakeCase(),
            { managementUserValidationFirstNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE) },
            { it.firstName.length.isBetween(MIN_NAME_SIZE, MAX_NAME_SIZE) }
        ),
        EntityValidation(
            UserEntity::firstName.name.toSnakeCase(),
            { managementUserValidationFirstNameInvalidValue() },
            { PERSON_NAME_REGEX.matches(it.firstName) }
        ),
        EntityValidation(
            UserEntity::lastName.name.toSnakeCase(),
            { managementUserValidationLastNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE) },
            { it.lastName.length.isBetween(MIN_NAME_SIZE, MAX_NAME_SIZE) }
        ),
        EntityValidation(
            UserEntity::lastName.name.toSnakeCase(),
            { managementUserValidationLastNameInvalidValue() },
            { PERSON_NAME_REGEX.matches(it.lastName) }
        ),
        EntityValidation(
            UserEntity::mainEmail.name.toSnakeCase(),
            { managementUserValidationMainEmailInvalidValue() },
            { EMAIL_REGEX.matches(it.mainEmail) }
        ),
        EntityValidation(
            UserEntity::documentNumber.name.toSnakeCase(),
            { managementUserValidationDocumentNumberInvalidValue() },
            { it.documentNumber.isValidDocumentNumber() }
        ),
        EntityValidation(
            UserEntity::phoneNumber.name.toSnakeCase(),
            { managementUserValidationPhoneNumberInvalidCharacter() },
            { it.phoneNumber == it.phoneNumber?.onlyNumbers() && (it.phoneNumber?.trim()?.isNotEmpty() ?: true) }
        )
    )

}
