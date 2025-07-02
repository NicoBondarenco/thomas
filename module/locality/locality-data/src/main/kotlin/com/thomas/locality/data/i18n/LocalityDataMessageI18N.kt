package com.thomas.locality.data.i18n

import com.thomas.core.i18n.BundleResolver

object LocalityDataMessageI18N : BundleResolver("strings/locality-data") {

    //region ADDRESS

    fun localityAddressValidationInvalidEntityErrorMessage() =
        formattedMessage("locality.address-validation.invalid-entity.error-message")

    fun localityAddressValidationZipcodeNumberInvalidValue() =
        formattedMessage("locality.address-validation.zipcode-number.invalid-value")

    fun localityAddressValidationAddressStreetInvalidValue() =
        formattedMessage("locality.address-validation.address-street.invalid-value")

    fun localityAddressValidationAddressStreetInvalidLength() =
        formattedMessage("locality.address-validation.address-street.invalid-length")

    fun localityAddressValidationAddressComplementInvalidValue() =
        formattedMessage("locality.address-validation.address-complement.invalid-value")

    fun localityAddressValidationAddressComplementInvalidLength() =
        formattedMessage("locality.address-validation.address-complement.invalid-length")

    fun localityAddressValidationAddressUnitInvalidLength() =
        formattedMessage("locality.address-validation.address-unit.invalid-length")

    fun localityAddressValidationAddressNeighborhoodInvalidValue() =
        formattedMessage("locality.address-validation.address-neighborhood.invalid-value")

    fun localityAddressValidationAddressNeighborhoodInvalidLength() =
        formattedMessage("locality.address-validation.address-neighborhood.invalid-length")

    fun localityAddressValidationAddressCityInvalidValue() =
        formattedMessage("locality.address-validation.address-city.invalid-value")

    fun localityAddressValidationAddressCityInvalidLength() =
        formattedMessage("locality.address-validation.address-city.invalid-length")

    fun localityAddressValidationCityCodeInvalidLength() =
        formattedMessage("locality.address-validation.city-code.invalid-length")

    fun localityAddressValidationReferenceCodeInvalidLength() =
        formattedMessage("locality.address-validation.reference-code.invalid-length")

    fun localityAddressValidationPhoneCodeInvalidValue() =
        formattedMessage("locality.address-validation.phone-code.invalid-value")

    fun localityAddressValidationPhoneCodeInvalidLength() =
        formattedMessage("locality.address-validation.phone-code.invalid-length")

    fun localityAddressValidationFederalCodeInvalidLength() =
        formattedMessage("locality.address-validation.federal-code.invalid-length")


    //endregion ADDRESS

}
