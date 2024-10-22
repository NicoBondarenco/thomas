package com.thomas.management.data.entity.info

import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.entity.EntityValidation
import com.thomas.core.model.entity.EntityValidationException
import com.thomas.core.util.StringUtils.randomString
import com.thomas.core.util.StringUtils.randomZipcode
import com.thomas.management.data.entity.EntityValidationTest
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
import java.util.UUID
import java.util.UUID.randomUUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class AddressInfoTest : EntityValidationTest() {

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

        private const val INVALID_CHARACTER = "Invalid value"
        private const val MIN_SIZE = "Invalid min size"
        private const val MAX_SIZE = "Invalid max size"

        @JvmStatic
        fun invalidZipcodes() = listOf(
            Arguments.of("215095855"),
            Arguments.of("5682130"),
            Arguments.of("5120145a"),
            Arguments.of("6540@846"),
            Arguments.of(""),
            Arguments.of(" "),
            Arguments.of("89562-421"),
        )

    }

    private val entity: TestAddressInfo
        get() = TestAddressInfo(
            addressZipcode = randomZipcode(),
            addressStreet = randomString(),
            addressNumber = randomString(4),
            addressComplement = listOf(null, randomString()).random(),
            addressNeighborhood = randomString(numbers = false),
            addressCity = randomString(numbers = false),
            addressState = AddressState.entries.random(),
        )

    private val invalidCharacterExecutions = mutableListOf<InvalidDataInput<AddressInfo, TestAddressInfo>>().apply {
        "!@#$%¨&*()_+={}[]ªº?/<>:;|\\\"§".map { it.toString() }.forEach {
            this.add(
                InvalidDataInput(
                    description = INVALID_CHARACTER,
                    value = it,
                    execution = { entity.copy(addressStreet = "${randomString(MAX_STREET_SIZE - 5)}$it") },
                    property = AddressInfo::addressStreet,
                    message = managementAddressValidationAddressStreetInvalidValue(),
                )
            )
            this.add(
                InvalidDataInput(
                    description = INVALID_CHARACTER,
                    value = it,
                    execution = { entity.copy(addressNumber = "${randomString(MAX_NUMBER_SIZE - 5)}$it") },
                    property = AddressInfo::addressNumber,
                    message = managementAddressValidationAddressNumberInvalidValue(),
                )
            )
            this.add(
                InvalidDataInput(
                    description = INVALID_CHARACTER,
                    value = it,
                    execution = { entity.copy(addressComplement = "${randomString(MAX_COMPLEMENT_SIZE - 5)}$it") },
                    property = AddressInfo::addressComplement,
                    message = managementAddressValidationAddressComplementInvalidValue(),
                )
            )
            this.add(
                InvalidDataInput(
                    description = INVALID_CHARACTER,
                    value = it,
                    execution = { entity.copy(addressNeighborhood = "${randomString(MAX_NEIGHBORHOOD_SIZE - 5)}$it") },
                    property = AddressInfo::addressNeighborhood,
                    message = managementAddressValidationAddressNeighborhoodInvalidValue(),
                )
            )
            this.add(
                InvalidDataInput(
                    description = INVALID_CHARACTER,
                    value = it,
                    execution = { entity.copy(addressCity = "${randomString(MAX_CITY_SIZE - 5, false)}$it") },
                    property = AddressInfo::addressCity,
                    message = managementAddressValidationAddressCityInvalidValue(),
                )
            )
        }
    }

    private val minSizeExecutions = mutableListOf<InvalidDataInput<AddressInfo, TestAddressInfo>>().apply {
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_STREET_SIZE ($MIN_STREET_SIZE)",
                execution = { entity.copy(addressStreet = randomString(MIN_STREET_SIZE - 1)) },
                property = AddressInfo::addressStreet,
                message = managementAddressValidationAddressStreetInvalidLength(MIN_STREET_SIZE, MAX_STREET_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_NUMBER_SIZE ($MIN_NUMBER_SIZE)",
                execution = { entity.copy(addressNumber = randomString(MIN_NUMBER_SIZE - 1)) },
                property = AddressInfo::addressNumber,
                message = managementAddressValidationAddressNumberInvalidLength(MIN_NUMBER_SIZE, MAX_NUMBER_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_COMPLEMENT_SIZE ($MIN_COMPLEMENT_SIZE)",
                execution = { entity.copy(addressComplement = randomString(MIN_COMPLEMENT_SIZE - 1)) },
                property = AddressInfo::addressComplement,
                message = managementAddressValidationAddressComplementInvalidLength(MIN_COMPLEMENT_SIZE, MAX_COMPLEMENT_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_NEIGHBORHOOD_SIZE ($MIN_NEIGHBORHOOD_SIZE)",
                execution = { entity.copy(addressNeighborhood = randomString(MIN_NEIGHBORHOOD_SIZE - 1)) },
                property = AddressInfo::addressNeighborhood,
                message = managementAddressValidationAddressNeighborhoodInvalidLength(MIN_NEIGHBORHOOD_SIZE, MAX_NEIGHBORHOOD_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_CITY_SIZE ($MIN_CITY_SIZE)",
                execution = { entity.copy(addressCity = randomString(MIN_CITY_SIZE - 1, false)) },
                property = AddressInfo::addressCity,
                message = managementAddressValidationAddressCityInvalidLength(MIN_CITY_SIZE, MAX_CITY_SIZE),
            )
        )
    }

    private val maxSizeExecutions = mutableListOf<InvalidDataInput<AddressInfo, TestAddressInfo>>().apply {
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_STREET_SIZE ($MAX_STREET_SIZE)",
                execution = { entity.copy(addressStreet = randomString(MAX_STREET_SIZE + 1)) },
                property = AddressInfo::addressStreet,
                message = managementAddressValidationAddressStreetInvalidLength(MIN_STREET_SIZE, MAX_STREET_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_NUMBER_SIZE ($MAX_NUMBER_SIZE)",
                execution = { entity.copy(addressNumber = randomString(MAX_NUMBER_SIZE + 1)) },
                property = AddressInfo::addressNumber,
                message = managementAddressValidationAddressNumberInvalidLength(MIN_NUMBER_SIZE, MAX_NUMBER_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_COMPLEMENT_SIZE ($MAX_COMPLEMENT_SIZE)",
                execution = { entity.copy(addressComplement = randomString(MAX_COMPLEMENT_SIZE + 1)) },
                property = AddressInfo::addressComplement,
                message = managementAddressValidationAddressComplementInvalidLength(MIN_COMPLEMENT_SIZE, MAX_COMPLEMENT_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_NEIGHBORHOOD_SIZE ($MAX_NEIGHBORHOOD_SIZE)",
                execution = { entity.copy(addressNeighborhood = randomString(MAX_NEIGHBORHOOD_SIZE + 1)) },
                property = AddressInfo::addressNeighborhood,
                message = managementAddressValidationAddressNeighborhoodInvalidLength(MIN_NEIGHBORHOOD_SIZE, MAX_NEIGHBORHOOD_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_CITY_SIZE ($MAX_CITY_SIZE)",
                execution = { entity.copy(addressCity = randomString(MAX_CITY_SIZE + 1, false)) },
                property = AddressInfo::addressCity,
                message = managementAddressValidationAddressCityInvalidLength(MIN_CITY_SIZE, MAX_CITY_SIZE),
            )
        )
    }

    override fun executions(): List<InvalidDataInput<*, *>> = invalidCharacterExecutions + minSizeExecutions + maxSizeExecutions

    @Test
    fun `Valid Address Info`() {
        (1..50).forEach { _ ->
            assertDoesNotThrow { entity }
        }
    }

    @ParameterizedTest
    @MethodSource("invalidZipcodes")
    fun `Invalid zipcode`(zipcode: String) {
        validateException(
            assertThrows<EntityValidationException> {
                entity.copy(addressZipcode = zipcode)
            },
            AddressInfo::addressZipcode,
            managementAddressValidationAddressZipcodeInvalidValue(),
        )
    }

    private data class TestAddressInfo(
        override val id: UUID = randomUUID(),
        override val addressZipcode: String,
        override val addressStreet: String,
        override val addressNumber: String,
        override val addressComplement: String? = null,
        override val addressNeighborhood: String,
        override val addressCity: String,
        override val addressState: AddressState,
    ) : BaseEntity<TestAddressInfo>(), AddressInfo {

        init {
            validate()
        }

        override fun validations(): List<EntityValidation<TestAddressInfo>> = addressInfoValidations()

    }

}
