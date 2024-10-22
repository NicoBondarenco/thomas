package com.thomas.management.data.entity

import com.thomas.core.util.StringUtils.randomEmail
import com.thomas.core.util.StringUtils.randomPhone
import com.thomas.core.util.StringUtils.randomRegistrationNumber
import com.thomas.core.util.StringUtils.randomString
import com.thomas.core.util.StringUtils.randomZipcode
import com.thomas.management.data.entity.value.AddressState
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementOrganizationValidationFantasyNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementOrganizationValidationFantasyNameInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementOrganizationValidationOrganizationNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementOrganizationValidationOrganizationNameInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementOrganizationValidationRegistrationNumberInvalidValue
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID.randomUUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class OrganizationEntityTest : EntityValidationTest() {

    companion object {
        private const val MIN_NAME_SIZE = 5
        private const val MAX_NAME_SIZE = 250

        private const val INVALID_CHARACTER = "Invalid value"
        private const val MIN_SIZE = "Invalid min size"
        private const val MAX_SIZE = "Invalid max size"
        private const val INVALID_REGISTRATION = "Invalid registration"
    }

    private val invalidCharacterExecutions = mutableListOf<InvalidDataInput<OrganizationEntity, OrganizationEntity>>().apply {
        "!@#$%¨&*()_+={}[]ªº?/<>:;|\\\"§".map { it.toString() }.forEach {
            this.add(
                InvalidDataInput(
                    description = INVALID_CHARACTER,
                    value = it,
                    execution = { entity.copy(organizationName = "${randomString(MAX_NAME_SIZE - 5)}$it") },
                    property = OrganizationEntity::organizationName,
                    message = managementOrganizationValidationOrganizationNameInvalidValue(),
                )
            )
            this.add(
                InvalidDataInput(
                    description = INVALID_CHARACTER,
                    value = it,
                    execution = { entity.copy(fantasyName = "${randomString(MAX_NAME_SIZE - 5)}$it") },
                    property = OrganizationEntity::fantasyName,
                    message = managementOrganizationValidationFantasyNameInvalidValue(),
                )
            )

        }
    }

    private val minSizeExecutions = mutableListOf<InvalidDataInput<OrganizationEntity, OrganizationEntity>>().apply {
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_NAME_SIZE ($MIN_NAME_SIZE)",
                execution = { entity.copy(organizationName = randomString(MIN_NAME_SIZE - 1)) },
                property = OrganizationEntity::organizationName,
                message = managementOrganizationValidationOrganizationNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_NAME_SIZE ($MIN_NAME_SIZE)",
                execution = { entity.copy(fantasyName = randomString(MIN_NAME_SIZE - 1)) },
                property = OrganizationEntity::fantasyName,
                message = managementOrganizationValidationFantasyNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
    }

    private val maxSizeExecutions = mutableListOf<InvalidDataInput<OrganizationEntity, OrganizationEntity>>().apply {
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_NAME_SIZE ($MAX_NAME_SIZE)",
                execution = { entity.copy(organizationName = randomString(MAX_NAME_SIZE + 1)) },
                property = OrganizationEntity::organizationName,
                message = managementOrganizationValidationOrganizationNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_NAME_SIZE ($MAX_NAME_SIZE)",
                execution = { entity.copy(fantasyName = randomString(MAX_NAME_SIZE + 1)) },
                property = OrganizationEntity::fantasyName,
                message = managementOrganizationValidationFantasyNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
    }

    private val invalidRegistrationExecutions = listOf(
        "71.506.168/0001-11",
        "64.731.857000196",
        "06432714/0001-70",
        "12.345.678/0001-95",
        "98765432000109",
        "11.222333000181",
        "1234567800019",
        "987654320001091",
        "71.5.16/0001-1",
        "11111111111111",
        " ",
        "",
    ).map {
        InvalidDataInput(
            description = INVALID_REGISTRATION,
            value = it,
            execution = { entity.copy(registrationNumber = it) },
            property = OrganizationEntity::registrationNumber,
            message = managementOrganizationValidationRegistrationNumberInvalidValue(),
        )
    }

    override fun executions(): List<InvalidDataInput<*, *>> = invalidCharacterExecutions + minSizeExecutions + maxSizeExecutions + invalidRegistrationExecutions

    private val entity: OrganizationEntity
        get() = OrganizationEntity(
            id = randomUUID(),
            organizationName = randomString(),
            fantasyName = listOf(null, randomString()).random(),
            registrationNumber = randomRegistrationNumber(),
            mainEmail = randomEmail(),
            mainPhone = randomPhone(),
            addressZipcode = randomZipcode(),
            addressStreet = randomString(),
            addressNumber = randomString(),
            addressComplement = listOf(null, randomString()).random(),
            addressNeighborhood = randomString(),
            addressCity = randomString(numbers = false),
            addressState = AddressState.entries.random(),
            isActive = listOf(true, false).random(),
            createAt = now(UTC),
            updatedAt = now(UTC),
        )

    @Test
    fun `Valid Organization Entity`() {
        (1..50).forEach { _ ->
            assertDoesNotThrow { entity }
        }
    }

}
