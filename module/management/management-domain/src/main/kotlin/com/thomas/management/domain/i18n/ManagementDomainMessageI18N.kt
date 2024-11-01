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

    fun managementUserValidationUserDataDuplicatedDocument() =
        formattedMessage("management.user-validation.user-data.duplicated-document")

    fun managementUserValidationUserDataDuplicatedEmail() =
        formattedMessage("management.user-validation.user-data.duplicated-email")

    //endregion USER

}
