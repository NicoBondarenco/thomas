package com.thomas.management.domain.validation

import com.thomas.core.model.entity.DeferredEntityValidation
import com.thomas.core.model.entity.DeferredEntityValidationContext.Companion.VT
import com.thomas.core.model.security.SecurityUnitRole
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.data.entity.UserEntity
import com.thomas.management.data.repository.UserRepository
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationGroupDataNotFound
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationOrganizationDataMaxUser
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUnitDataNotFound
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUserDataDuplicatedDocument
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUserDataDuplicatedEmail
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUserDataInvalidPassword
import java.util.UUID

private val PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_-])[A-Za-z\\d@$!%*?&_-]{8,}$".toRegex()

fun UserRepository.sameEmailSignup() = DeferredEntityValidation<UserEntity>(
    field = UserEntity::mainEmail,
    message = { managementUserValidationUserDataDuplicatedEmail() },
    validate = { !this.hasAnotherWithEmail(it.id, it.mainEmail) },
    context = VT,
)

fun UserRepository.sameEmail() = DeferredEntityValidation<UserCompleteEntity>(
    field = UserEntity::mainEmail,
    message = { managementUserValidationUserDataDuplicatedEmail() },
    validate = { !this.hasAnotherWithEmail(it.id, it.userData.mainEmail) },
    context = VT,
)

fun UserRepository.sameDocument() = DeferredEntityValidation<UserCompleteEntity>(
    field = UserEntity::documentNumber,
    message = { managementUserValidationUserDataDuplicatedDocument() },
    validate = { !this.hasAnotherWithDocument(it.id, it.userData.userOrganization.id, it.userData.documentNumber) },
    context = VT,
)

fun UserRepository.maxUsers() = DeferredEntityValidation<UserCompleteEntity>(
    field = UserEntity::userOrganization,
    message = { managementUserValidationOrganizationDataMaxUser() },
    validate = { !this.limitReached(it.id, it.userData.userOrganization.id) },
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
            userUnits.keys.subtract(entity.userUnits.keys.map { it.id }.toSet())
        )
    },
    validate = { it.userUnits.size == userUnits.size },
)

fun validPassword(password: String) = DeferredEntityValidation<UserEntity>(
    field = UserEntity::passwordHash,
    message = { managementUserValidationUserDataInvalidPassword() },
    validate = { PASSWORD_REGEX.matches(password) },
)

