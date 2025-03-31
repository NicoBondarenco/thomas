package com.thomas.management.domain.validation

import com.thomas.core.model.entity.DeferredEntityValidation
import com.thomas.core.model.entity.DeferredEntityValidationContext.Companion.VT
import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.data.entity.UserSimpleEntity
import com.thomas.management.data.repository.UserRepository
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationGroupDataNotFound
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationOrganizationDataMaxUser
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUnitDataNotFound
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUserDataDuplicatedDocument
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUserDataDuplicatedEmail
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUserDataInvalidPassword
import java.util.UUID

private const val PASSWORD_LENGTH = 8
private val PASSWORD_UPPER = ".*[A-Z].*".toRegex()
private val PASSWORD_LOWER = ".*[a-z].*".toRegex()
private val PASSWORD_SYMBOLS = "\"'!@#$%&*()_-+=§`´[]{}^~,.<>;:/?|\\".toSet()
private val PASSWORD_NUMBERS = ".*[0-9].*".toRegex()

private fun String.isValidPassword(): Boolean = length >= PASSWORD_LENGTH &&
        PASSWORD_UPPER.matches(this) &&
        PASSWORD_LOWER.matches(this) &&
        PASSWORD_NUMBERS.matches(this) &&
        this.toList().intersect(PASSWORD_SYMBOLS).isNotEmpty()

fun UserRepository.sameEmailSignup() = DeferredEntityValidation<UserSimpleEntity>(
    field = UserSimpleEntity::mainEmail,
    message = { managementUserValidationUserDataDuplicatedEmail() },
    validate = { !this.hasAnotherWithEmail(it.id, it.mainEmail) },
    context = VT,
)

fun UserRepository.sameEmail() = DeferredEntityValidation<UserCompleteEntity>(
    field = UserSimpleEntity::mainEmail,
    message = { managementUserValidationUserDataDuplicatedEmail() },
    validate = { !this.hasAnotherWithEmail(it.id, it.mainEmail) },
    context = VT,
)

fun UserRepository.sameDocument() = DeferredEntityValidation<UserCompleteEntity>(
    field = UserSimpleEntity::documentNumber,
    message = { managementUserValidationUserDataDuplicatedDocument() },
    validate = { !this.hasAnotherWithDocument(it.id, it.userOrganization.id, it.documentNumber) },
    context = VT,
)

fun UserRepository.maxUsers() = DeferredEntityValidation<UserCompleteEntity>(
    field = UserSimpleEntity::userOrganization,
    message = { managementUserValidationOrganizationDataMaxUser() },
    validate = { !this.limitReached(it.id, it.userOrganization.id) },
    context = VT,
)

fun userGroupsFound(
    userGroups: Set<UUID>,
) = DeferredEntityValidation<UserCompleteEntity>(
    field = UserCompleteEntity::userGroups,
    message = { entity ->
        managementUserValidationGroupDataNotFound(
            userGroups.subtract(entity.userGroups.map { it.id }.toSet())
        )
    },
    validate = { it.userGroups.size == userGroups.size },
)

fun userUnitsFound(
    userUnits: Map<UUID, Set<SecurityUnitRole>>,
) = DeferredEntityValidation<UserCompleteEntity>(
    field = UserCompleteEntity::userUnits,
    message = { entity ->
        managementUserValidationUnitDataNotFound(
            userUnits.keys.subtract(entity.userUnits.map { it.roleUnit.id }.toSet())
        )
    },
    validate = { it.userUnits.size == userUnits.size },
)

fun validPassword(password: String) = DeferredEntityValidation<UserSimpleEntity>(
    field = UserSimpleEntity::passwordHash,
    message = { managementUserValidationUserDataInvalidPassword() },
    validate = { password.isValidPassword() },
)

