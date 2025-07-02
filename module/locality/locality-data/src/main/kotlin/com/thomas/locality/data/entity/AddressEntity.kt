package com.thomas.locality.data.entity

import com.thomas.core.extension.LETTERS_ONLY_REGEX_VALUE
import com.thomas.core.extension.isBetween
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.entity.EntityValidation
import com.thomas.core.model.general.AddressState
import com.thomas.locality.data.entity.info.CreationInfo
import com.thomas.locality.data.i18n.LocalityDataMessageI18N.localityAddressValidationAddressCityInvalidLength
import com.thomas.locality.data.i18n.LocalityDataMessageI18N.localityAddressValidationAddressCityInvalidValue
import com.thomas.locality.data.i18n.LocalityDataMessageI18N.localityAddressValidationAddressComplementInvalidLength
import com.thomas.locality.data.i18n.LocalityDataMessageI18N.localityAddressValidationAddressComplementInvalidValue
import com.thomas.locality.data.i18n.LocalityDataMessageI18N.localityAddressValidationAddressNeighborhoodInvalidLength
import com.thomas.locality.data.i18n.LocalityDataMessageI18N.localityAddressValidationAddressNeighborhoodInvalidValue
import com.thomas.locality.data.i18n.LocalityDataMessageI18N.localityAddressValidationAddressStreetInvalidLength
import com.thomas.locality.data.i18n.LocalityDataMessageI18N.localityAddressValidationAddressStreetInvalidValue
import com.thomas.locality.data.i18n.LocalityDataMessageI18N.localityAddressValidationAddressUnitInvalidLength
import com.thomas.locality.data.i18n.LocalityDataMessageI18N.localityAddressValidationCityCodeInvalidLength
import com.thomas.locality.data.i18n.LocalityDataMessageI18N.localityAddressValidationFederalCodeInvalidLength
import com.thomas.locality.data.i18n.LocalityDataMessageI18N.localityAddressValidationInvalidEntityErrorMessage
import com.thomas.locality.data.i18n.LocalityDataMessageI18N.localityAddressValidationPhoneCodeInvalidLength
import com.thomas.locality.data.i18n.LocalityDataMessageI18N.localityAddressValidationPhoneCodeInvalidValue
import com.thomas.locality.data.i18n.LocalityDataMessageI18N.localityAddressValidationReferenceCodeInvalidLength
import com.thomas.locality.data.i18n.LocalityDataMessageI18N.localityAddressValidationZipcodeNumberInvalidValue
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID
import java.util.UUID.randomUUID

data class AddressEntity(
    override val id: UUID = randomUUID(),
    val zipcodeNumber: String,
    val addressStreet: String?,
    val addressComplement: String?,
    val addressUnit: String?,
    val addressNeighborhood: String?,
    val addressCity: String,
    val addressState: AddressState,
    val cityCode: String?,
    val referenceCode: String?,
    val phoneCode: String?,
    val federalCode: String?,
    override val createdAt: OffsetDateTime = now(UTC),
    override val updatedAt: OffsetDateTime = now(UTC),
) : BaseEntity<AddressEntity>(), CreationInfo {

    companion object {
        private const val MIN_STREET_SIZE = 3
        private const val MAX_STREET_SIZE = 250
        private const val MIN_COMPLEMENT_SIZE = 3
        private const val MAX_COMPLEMENT_SIZE = 250
        private const val MIN_UNIT_SIZE = 1
        private const val MAX_UNIT_SIZE = 250
        private const val MIN_NEIGHBORHOOD_SIZE = 2
        private const val MAX_NEIGHBORHOOD_SIZE = 250
        private const val MIN_CITY_SIZE = 3
        private const val MAX_CITY_SIZE = 250
        private const val MIN_CODE_SIZE = 1
        private const val MAX_CODE_SIZE = 250
        private const val MIN_REFERENCE_SIZE = 1
        private const val MAX_REFERENCE_SIZE = 250
        private const val MIN_PHONE_SIZE = 1
        private const val MAX_PHONE_SIZE = 3
        private const val MIN_FEDERAL_SIZE = 1
        private const val MAX_FEDERAL_SIZE = 250

        private val ZIPCODE_NUMBER_REGEX = "[0-9]{8}".toRegex()
        private val ADDRESS_STREET_REGEX = "[${LETTERS_ONLY_REGEX_VALUE}0-9\\-.;\\\\.,'ªº*() ]+".toRegex()
        private val ADDRESS_COMPLEMENT_REGEX = "[${LETTERS_ONLY_REGEX_VALUE}0-9\\-.,;\\\\.'/ªº*() ]+".toRegex()
        private val ADDRESS_NEIGHBORHOOD_REGEX = "[${LETTERS_ONLY_REGEX_VALUE}0-9\\-.,;\\\\.'/ªº*() ]+".toRegex()
        private val ADDRESS_CITY_REGEX = "[$LETTERS_ONLY_REGEX_VALUE\\- ]+".toRegex()
        private val PHONE_NUMBER_REGEX = "[0-9]{1,3}".toRegex()
    }

    init {
        validate()
    }

    override fun errorMessage(): String = localityAddressValidationInvalidEntityErrorMessage()

    override fun validations(): List<EntityValidation<AddressEntity>> = listOf(
        EntityValidation(
            AddressEntity::zipcodeNumber.name.toSnakeCase(),
            { localityAddressValidationZipcodeNumberInvalidValue() },
            { ZIPCODE_NUMBER_REGEX.matches(it.zipcodeNumber) }
        ),
        EntityValidation(
            AddressEntity::addressStreet.name.toSnakeCase(),
            { localityAddressValidationAddressStreetInvalidValue() },
            { it.addressStreet == null || ADDRESS_STREET_REGEX.matches(it.addressStreet) }
        ),
        EntityValidation(
            AddressEntity::addressStreet.name.toSnakeCase(),
            { localityAddressValidationAddressStreetInvalidLength() },
            { it.addressStreet == null || it.addressStreet.length.isBetween(MIN_STREET_SIZE, MAX_STREET_SIZE) }
        ),
        EntityValidation(
            AddressEntity::addressComplement.name.toSnakeCase(),
            { localityAddressValidationAddressComplementInvalidValue() },
            { it.addressComplement == null || ADDRESS_COMPLEMENT_REGEX.matches(it.addressComplement) }
        ),
        EntityValidation(
            AddressEntity::addressComplement.name.toSnakeCase(),
            { localityAddressValidationAddressComplementInvalidLength() },
            { it.addressComplement == null || it.addressComplement.length.isBetween(MIN_COMPLEMENT_SIZE, MAX_COMPLEMENT_SIZE) }
        ),
        EntityValidation(
            AddressEntity::addressUnit.name.toSnakeCase(),
            { localityAddressValidationAddressUnitInvalidLength() },
            { it.addressUnit == null || it.addressUnit.length.isBetween(MIN_UNIT_SIZE, MAX_UNIT_SIZE) }
        ),
        EntityValidation(
            AddressEntity::addressNeighborhood.name.toSnakeCase(),
            { localityAddressValidationAddressNeighborhoodInvalidValue() },
            { it.addressNeighborhood == null || ADDRESS_NEIGHBORHOOD_REGEX.matches(it.addressNeighborhood) }
        ),
        EntityValidation(
            AddressEntity::addressNeighborhood.name.toSnakeCase(),
            { localityAddressValidationAddressNeighborhoodInvalidLength() },
            { it.addressNeighborhood == null || it.addressNeighborhood.length.isBetween(MIN_NEIGHBORHOOD_SIZE, MAX_NEIGHBORHOOD_SIZE) }
        ),
        EntityValidation(
            AddressEntity::addressCity.name.toSnakeCase(),
            { localityAddressValidationAddressCityInvalidValue() },
            { ADDRESS_CITY_REGEX.matches(it.addressCity) }
        ),
        EntityValidation(
            AddressEntity::addressCity.name.toSnakeCase(),
            { localityAddressValidationAddressCityInvalidLength() },
            { it.addressCity.length.isBetween(MIN_CITY_SIZE, MAX_CITY_SIZE) }
        ),
        EntityValidation(
            AddressEntity::cityCode.name.toSnakeCase(),
            { localityAddressValidationCityCodeInvalidLength() },
            { it.cityCode == null || it.cityCode.length.isBetween(MIN_CODE_SIZE, MAX_CODE_SIZE) }
        ),
        EntityValidation(
            AddressEntity::referenceCode.name.toSnakeCase(),
            { localityAddressValidationReferenceCodeInvalidLength() },
            { it.referenceCode == null || it.referenceCode.length.isBetween(MIN_REFERENCE_SIZE, MAX_REFERENCE_SIZE) }
        ),
        EntityValidation(
            AddressEntity::phoneCode.name.toSnakeCase(),
            { localityAddressValidationPhoneCodeInvalidValue() },
            { it.phoneCode == null || PHONE_NUMBER_REGEX.matches(it.phoneCode) }
        ),
        EntityValidation(
            AddressEntity::phoneCode.name.toSnakeCase(),
            { localityAddressValidationPhoneCodeInvalidLength() },
            { it.phoneCode == null || it.phoneCode.length.isBetween(MIN_PHONE_SIZE, MAX_PHONE_SIZE) }
        ),
        EntityValidation(
            AddressEntity::federalCode.name.toSnakeCase(),
            { localityAddressValidationFederalCodeInvalidLength() },
            { it.federalCode == null || it.federalCode.length.isBetween(MIN_FEDERAL_SIZE, MAX_FEDERAL_SIZE) }
        ),
    )

}