package com.thomas.management.domain.i18n

import com.thomas.core.i18n.BundleResolver

object ManagementDomainMessageI18N : BundleResolver("strings/management-domain") {

    //region SIGNUP

    fun managementSignupValidationSignupDataInvalidData() =
        formattedMessage("management.signup-validation.signup-data.invalid-data")

    fun managementSignupValidationSignupPropertiesSignupDisabled() =
        formattedMessage("management.signup-validation.signup-properties.signup-disabled")

    //endregion SIGNUP

    //region ORGANIZATION

    fun managementOrganizationValidationOrganizationDataDuplicatedName() =
        formattedMessage("management.organization-validation.organization-data.duplicated-name")

    fun managementOrganizationValidationOrganizationDataDuplicatedRegistration() =
        formattedMessage("management.organization-validation.organization-data.duplicated-registration")

    //endregion ORGANIZATION

    //region USER

    fun managementUserValidationUserDataDuplicatedDocument() =
        formattedMessage("management.user-validation.user-data.duplicated-document")

    fun managementUserValidationUserDataDuplicatedEmail() =
        formattedMessage("management.user-validation.user-data.duplicated-email")

    //endregion USER

}
