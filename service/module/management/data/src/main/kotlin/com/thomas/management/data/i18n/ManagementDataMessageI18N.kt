package com.thomas.management.data.i18n

import com.thomas.core.i18n.BundleResolver


object ManagementDataMessageI18N : BundleResolver("strings/management-data") {

    //region USER

    fun managementUserValidationInvalidEntityErrorMessage() =
        getFormattedMessage("management.user-validation.invalid-entity.error-message")

    fun managementUserValidationFirstNameInvalidLength(min: Int, max: Int) =
        getFormattedMessage("management.user-validation.first-name.invalid-length", min, max)

    fun managementUserValidationFirstNameInvalidValue() =
        getFormattedMessage("management.user-validation.first-name.invalid-value")

    fun managementUserValidationLastNameInvalidLength(min: Int, max: Int) =
        getFormattedMessage("management.user-validation.last-name.invalid-length", min, max)

    fun managementUserValidationLastNameInvalidValue() =
        getFormattedMessage("management.user-validation.last-name.invalid-value")

    fun managementUserValidationMainEmailInvalidValue() =
        getFormattedMessage("management.user-validation.main-email.invalid-value")

    fun managementUserValidationDocumentNumberInvalidValue() =
        getFormattedMessage("management.user-validation.document-number.invalid-value")

    fun managementUserValidationPhoneNumberInvalidCharacter() =
        getFormattedMessage("management.user-validation.phone-number.invalid-character")

    fun managementUserValidationMainEmailDuplicatedValue() =
        getFormattedMessage("management.user-validation.main-email.duplicated-value")

    //endregion USER

    //region GROUP

    fun managementGroupValidationInvalidEntityErrorMessage() =
        getFormattedMessage("management.group-validation.invalid-entity.error-message")

    fun managementGroupValidationGroupNameInvalidLength(min: Int, max: Int) =
        getFormattedMessage("management.group-validation.group-name.invalid-length", min, max)

    fun managementGroupValidationGroupNameInvalidValue() =
        getFormattedMessage("management.group-validation.group-name.invalid-value")

    //endregion GROUP

}
