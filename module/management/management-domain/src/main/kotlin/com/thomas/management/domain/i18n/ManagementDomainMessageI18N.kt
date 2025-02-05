package com.thomas.management.domain.i18n

import com.thomas.core.i18n.BundleResolver
import java.util.UUID

object ManagementDomainMessageI18N : BundleResolver("strings/management-domain") {

    //region SIGNUP

    fun managementSignupValidationSignupDataInvalidData() =
        formattedMessage("management.signup-validation.signup-data.invalid-data")

    fun managementSignupValidationSignupPropertiesSignupDisabled() =
        formattedMessage("management.signup-validation.signup-properties.signup-disabled")

    //endregion SIGNUP

    //region ORGANIZATION

    fun managementOrganizationValidationOrganizationDataInvalidData() =
        formattedMessage("management.organization-validation.organization-data.invalid-data")

    fun managementOrganizationValidationOrganizationDataDuplicatedName() =
        formattedMessage("management.organization-validation.organization-data.duplicated-name")

    fun managementOrganizationValidationOrganizationDataDuplicatedRegistration() =
        formattedMessage("management.organization-validation.organization-data.duplicated-registration")

    fun managementOrganizationSearchNotFoundErrorMessage(id: UUID) =
        formattedMessage("management.organization-search.not-found.error-message", id)

    //endregion ORGANIZATION

    //region UNIT

    fun managementUnitValidationUnitDataInvalidData() =
        formattedMessage("management.unit-validation.unit-data.invalid-data")

    fun managementUnitValidationUnitDataDuplicatedName() =
        formattedMessage("management.unit-validation.unit-data.duplicated-name")

    fun managementUnitValidationUnitDataDuplicatedDocument() =
        formattedMessage("management.unit-validation.unit-data.duplicated-document")

    fun managementUnitValidationOrganizationDataMaxUnit() =
        formattedMessage("management.unit-validation.organization-data.max-unit")

    fun managementUnitSearchNotFoundErrorMessage(id: UUID) =
        formattedMessage("management.unit-search.not-found.error-message", id)

    //endregion UNIT

    //region USER

    fun managementUserValidationUserDataInvalidData() =
        formattedMessage("management.user-validation.user-data.invalid-data")

    fun managementUserValidationUserDataDuplicatedDocument() =
        formattedMessage("management.user-validation.user-data.duplicated-document")

    fun managementUserValidationUserDataDuplicatedEmail() =
        formattedMessage("management.user-validation.user-data.duplicated-email")

    fun managementUserValidationOrganizationDataMaxUser() =
        formattedMessage("management.user-validation.organization-data.max-user")

    fun managementUserValidationGroupDataNotFound(ids: Set<UUID>) =
        formattedMessage("management.user-validation.group-data.not-found", ids.joinToString(", ") { it.toString() })

    fun managementUserValidationUnitDataNotFound(ids: Set<UUID>) =
        formattedMessage("management.user-validation.unit-data.not-found", ids.joinToString(", ") { it.toString() })

    fun managementUserValidationUserDataInvalidPassword() =
        formattedMessage("management.user-validation.user-data.invalid-password")

    fun managementUserSearchNotFoundErrorMessage(id: UUID) =
        formattedMessage("management.user-search.not-found.error-message", id)

    //endregion USER

    //region PASSWORD RESET

    fun managementResetPasswordResetTokenInvalidToken() =
        formattedMessage("management.reset-password.reset-token.invalid-token")

    fun managementResetPasswordResetTokenExpiredToken() =
        formattedMessage("management.reset-password.reset-token.expired-token")

    //endregion PASSWORD RESET

    //region GROUP

    fun managementGroupValidationGroupDataInvalidData() =
        formattedMessage("management.group-validation.group-data.invalid-data")

    fun managementGroupValidationGroupDataDuplicatedName() =
        formattedMessage("management.group-validation.group-data.duplicated-name")

    fun managementGroupValidationUnitDataNotFound(ids: Set<UUID>) =
        formattedMessage("management.group-validation.unit-data.not-found", ids.joinToString(", ") { it.toString() })

    fun managementGroupSearchNotFoundErrorMessage(id: UUID) =
        formattedMessage("management.group-search.not-found.error-message", id)

    //endregion GROUP

    //region AUTHENTICATION

    fun managementAuthenticationLoginCredentialDataInvalidCredentials() =
        formattedMessage("management.authentication-login.credential-data.invalid-credentials")

    fun managementAuthenticationLoginOrganizationDataInactiveOrganization() =
        formattedMessage("management.authentication-login.organization-data.inactive-organization")

    fun managementAuthenticationLoginUserDataInactiveUser() =
        formattedMessage("management.authentication-login.user-data.inactive-user")

    fun managementAuthenticationRefreshRefreshTokenInvalidToken() =
        formattedMessage("management.authentication-refresh.refresh-token.invalid-token")

    //endregion AUTHENTICATION

}
