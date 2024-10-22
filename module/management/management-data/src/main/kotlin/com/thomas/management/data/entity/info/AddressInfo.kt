package com.thomas.management.data.entity.info

import com.thomas.core.extension.LETTERS_ONLY_REGEX_VALUE
import com.thomas.core.extension.isBetween
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.entity.EntityValidation
import com.thomas.management.data.entity.value.AddressState
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementAddressValidationAddressCityInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementAddressValidationAddressCityInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementAddressValidationAddressComplementInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementAddressValidationAddressComplementInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementAddressValidationAddressNeighborhoodInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementAddressValidationAddressNeighborhoodInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementAddressValidationAddressNumberInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementAddressValidationAddressNumberInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementAddressValidationAddressStreetInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementAddressValidationAddressStreetInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementAddressValidationAddressZipcodeInvalidValue

interface AddressInfo {

    companion object {
        private const val MIN_STREET_SIZE = 5
        private const val MAX_STREET_SIZE = 250
        private const val MIN_NUMBER_SIZE = 1
        private const val MAX_NUMBER_SIZE = 10
        private const val MIN_COMPLEMENT_SIZE = 5
        private const val MAX_COMPLEMENT_SIZE = 100
        private const val MIN_NEIGHBORHOOD_SIZE = 3
        private const val MAX_NEIGHBORHOOD_SIZE = 250
        private const val MIN_CITY_SIZE = 3
        private const val MAX_CITY_SIZE = 250
        private val ADDRESS_MANES_REGEX = "[${LETTERS_ONLY_REGEX_VALUE}0-9\\-.,' ]+".toRegex()
        private val ADDRESS_CITY_REGEX = "[${LETTERS_ONLY_REGEX_VALUE}\\- ]+".toRegex()
        private val ZIPCODE_NUMBER_REGEX = "[0-9]{8}".toRegex()
    }

    val addressZipcode: String
    val addressStreet: String
    val addressNumber: String
    val addressComplement: String?
    val addressNeighborhood: String
    val addressCity: String
    val addressState: AddressState

    fun <T> addressInfoValidations(): List<EntityValidation<T>> where T : AddressInfo, T : BaseEntity<T> = listOf(
        EntityValidation(
            AddressInfo::addressZipcode.name.toSnakeCase(),
            { managementAddressValidationAddressZipcodeInvalidValue() },
            { ZIPCODE_NUMBER_REGEX.matches(it.addressZipcode) }
        ),
        EntityValidation(
            AddressInfo::addressStreet.name.toSnakeCase(),
            { managementAddressValidationAddressStreetInvalidValue() },
            { ADDRESS_MANES_REGEX.matches(it.addressStreet) }
        ),
        EntityValidation(
            AddressInfo::addressStreet.name.toSnakeCase(),
            { managementAddressValidationAddressStreetInvalidLength(MIN_STREET_SIZE, MAX_STREET_SIZE) },
            { it.addressStreet.length.isBetween(MIN_STREET_SIZE, MAX_STREET_SIZE) }
        ),
        EntityValidation(
            AddressInfo::addressNumber.name.toSnakeCase(),
            { managementAddressValidationAddressNumberInvalidValue() },
            { it.addressNumber.isEmpty() || ADDRESS_MANES_REGEX.matches(it.addressNumber) }
        ),
        EntityValidation(
            AddressInfo::addressNumber.name.toSnakeCase(),
            { managementAddressValidationAddressNumberInvalidLength(MIN_NUMBER_SIZE, MAX_NUMBER_SIZE) },
            { it.addressNumber.length.isBetween(MIN_NUMBER_SIZE, MAX_NUMBER_SIZE) }
        ),
        EntityValidation(
            AddressInfo::addressComplement.name.toSnakeCase(),
            { managementAddressValidationAddressComplementInvalidValue() },
            { it.addressComplement == null || ADDRESS_MANES_REGEX.matches(it.addressComplement!!) }
        ),
        EntityValidation(
            AddressInfo::addressComplement.name.toSnakeCase(),
            { managementAddressValidationAddressComplementInvalidLength(MIN_COMPLEMENT_SIZE, MAX_COMPLEMENT_SIZE) },
            { it.addressComplement == null || it.addressComplement!!.length.isBetween(MIN_COMPLEMENT_SIZE, MAX_COMPLEMENT_SIZE) }
        ),
        EntityValidation(
            AddressInfo::addressNeighborhood.name.toSnakeCase(),
            { managementAddressValidationAddressNeighborhoodInvalidValue() },
            { ADDRESS_MANES_REGEX.matches(it.addressNeighborhood) }
        ),
        EntityValidation(
            AddressInfo::addressNeighborhood.name.toSnakeCase(),
            { managementAddressValidationAddressNeighborhoodInvalidLength(MIN_NEIGHBORHOOD_SIZE, MAX_NEIGHBORHOOD_SIZE) },
            { it.addressNeighborhood.length.isBetween(MIN_NEIGHBORHOOD_SIZE, MAX_NEIGHBORHOOD_SIZE) }
        ),
        EntityValidation(
            AddressInfo::addressCity.name.toSnakeCase(),
            { managementAddressValidationAddressCityInvalidValue() },
            { ADDRESS_CITY_REGEX.matches(it.addressCity) }
        ),
        EntityValidation(
            AddressInfo::addressCity.name.toSnakeCase(),
            { managementAddressValidationAddressCityInvalidLength(MIN_CITY_SIZE, MAX_CITY_SIZE) },
            { it.addressCity.length.isBetween(MIN_CITY_SIZE, MAX_CITY_SIZE) }
        ),
    )

}
