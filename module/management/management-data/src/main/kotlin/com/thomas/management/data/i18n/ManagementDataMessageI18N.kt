package com.thomas.management.data.i18n

import com.thomas.core.i18n.BundleResolver

object ManagementDataMessageI18N : BundleResolver("strings/management-data") {

    //region CONTACT INFO

    fun managementContactValidationMainEmailInvalidValue() =
        formattedMessage("management.contact-validation.main-email.invalid-value")

    fun managementContactValidationMainPhoneInvalidValue() =
        formattedMessage("management.contact-validation.main-phone.invalid-value")

    //endregion CONTACT INFO

    //region ADDRESS INFO

    fun managementAddressValidationAddressZipcodeInvalidValue() =
        formattedMessage("management.address-validation.address-zipcode.invalid-value")

    fun managementAddressValidationAddressStreetInvalidValue() =
        formattedMessage("management.address-validation.address-street.invalid-value")

    fun managementAddressValidationAddressStreetInvalidLength(min: Int, max: Int) =
        formattedMessage("management.address-validation.address-street.invalid-length", min, max)

    fun managementAddressValidationAddressNumberInvalidValue() =
        formattedMessage("management.address-validation.address-number.invalid-value")

    fun managementAddressValidationAddressNumberInvalidLength(min: Int, max: Int) =
        formattedMessage("management.address-validation.address-number.invalid-length", min, max)

    fun managementAddressValidationAddressComplementInvalidValue() =
        formattedMessage("management.address-validation.address-complement.invalid-value")

    fun managementAddressValidationAddressComplementInvalidLength(min: Int, max: Int) =
        formattedMessage("management.address-validation.address-complement.invalid-length", min, max)

    fun managementAddressValidationAddressNeighborhoodInvalidValue() =
        formattedMessage("management.address-validation.address-neighborhood.invalid-value")

    fun managementAddressValidationAddressNeighborhoodInvalidLength(min: Int, max: Int) =
        formattedMessage("management.address-validation.address-neighborhood.invalid-length", min, max)

    fun managementAddressValidationAddressCityInvalidValue() =
        formattedMessage("management.address-validation.address-city.invalid-value")

    fun managementAddressValidationAddressCityInvalidLength(min: Int, max: Int) =
        formattedMessage("management.address-validation.address-city.invalid-length", min, max)

    //endregion ADDRESS INFO

    //region ORGANIZATION

    fun managementOrganizationValidationInvalidEntityErrorMessage() =
        formattedMessage("management.organization-validation.invalid-entity.error-message")

    fun managementOrganizationValidationOrganizationNameInvalidLength(min: Int, max: Int) =
        formattedMessage("management.organization-validation.organization-name.invalid-length", min, max)

    fun managementOrganizationValidationOrganizationNameInvalidValue() =
        formattedMessage("management.organization-validation.organization-name.invalid-value")

    fun managementOrganizationValidationFantasyNameInvalidLength(min: Int, max: Int) =
        formattedMessage("management.organization-validation.fantasy-name.invalid-length", min, max)

    fun managementOrganizationValidationFantasyNameInvalidValue() =
        formattedMessage("management.organization-validation.fantasy-name.invalid-value")

    fun managementOrganizationValidationRegistrationNumberInvalidValue() =
        formattedMessage("management.organization-validation.registration-number.invalid-value")

    //endregion ORGANIZATION

    //region UNIT

    fun managementUnitValidationInvalidEntityErrorMessage() =
        formattedMessage("management.unit-validation.invalid-entity.error-message")

    fun managementUnitValidationUnitNameInvalidLength(min: Int, max: Int) =
        formattedMessage("management.unit-validation.unit-name.invalid-length", min, max)

    fun managementUnitValidationUnitNameInvalidValue() =
        formattedMessage("management.unit-validation.unit-name.invalid-value")

    fun managementUnitValidationFantasyNameInvalidLength(min: Int, max: Int) =
        formattedMessage("management.unit-validation.fantasy-name.invalid-length", min, max)

    fun managementUnitValidationFantasyNameInvalidValue() =
        formattedMessage("management.unit-validation.fantasy-name.invalid-value")

    fun managementUnitValidationDocumentNumberInvalidValue() =
        formattedMessage("management.unit-validation.document-number.invalid-value")

    //endregion UNIT

    //region USER

    fun managementUserValidationInvalidEntityErrorMessage() =
        formattedMessage("management.user-validation.invalid-entity.error-message")

    fun managementUserValidationFirstNameInvalidLength(min: Int, max: Int) =
        formattedMessage("management.user-validation.first-name.invalid-length", min, max)

    fun managementUserValidationFirstNameInvalidValue() =
        formattedMessage("management.user-validation.first-name.invalid-value")

    fun managementUserValidationLastNameInvalidLength(min: Int, max: Int) =
        formattedMessage("management.user-validation.last-name.invalid-length", min, max)

    fun managementUserValidationLastNameInvalidValue() =
        formattedMessage("management.user-validation.last-name.invalid-value")

    fun managementUserValidationDocumentNumberInvalidValue() =
        formattedMessage("management.user-validation.document-number.invalid-value")

    //endregion USER

    //region GROUP

    fun managementGroupValidationInvalidEntityErrorMessage() =
        formattedMessage("management.group-validation.invalid-entity.error-message")

    fun managementGroupValidationGroupNameInvalidLength(min: Int, max: Int) =
        formattedMessage("management.group-validation.group-name.invalid-length", min, max)

    fun managementGroupValidationGroupNameInvalidValue() =
        formattedMessage("management.group-validation.group-name.invalid-value")

    fun managementGroupValidationGroupDescriptionInvalidLength(min: Int, max: Int) =
        formattedMessage("management.group-validation.group-description.invalid-length", min, max)

    fun managementGroupValidationGroupDescriptionInvalidValue() =
        formattedMessage("management.group-validation.group-description.invalid-value")

    //endregion GROUP

}
