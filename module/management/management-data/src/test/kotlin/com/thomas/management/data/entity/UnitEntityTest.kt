package com.thomas.management.data.entity

import com.thomas.core.util.StringUtils.randomDocumentNumber
import com.thomas.core.util.StringUtils.randomEmail
import com.thomas.core.util.StringUtils.randomPhone
import com.thomas.core.util.StringUtils.randomRegistrationNumber
import com.thomas.core.util.StringUtils.randomString
import com.thomas.core.util.StringUtils.randomZipcode
import com.thomas.management.data.entity.value.AddressState
import com.thomas.management.data.entity.value.UnitType
import com.thomas.management.data.entity.value.UnitType.LEGAL
import com.thomas.management.data.entity.value.UnitType.NATURAL
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUnitValidationDocumentNumberInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUnitValidationFantasyNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUnitValidationFantasyNameInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUnitValidationUnitNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUnitValidationUnitNameInvalidValue
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID.randomUUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class UnitEntityTest : EntityValidationTest() {

    companion object {
        private const val MIN_NAME_SIZE = 5
        private const val MAX_NAME_SIZE = 250

        private const val INVALID_CHARACTER = "Invalid value"
        private const val MIN_SIZE = "Invalid min size"
        private const val MAX_SIZE = "Invalid max size"
        private const val INVALID_REGISTRATION = "Invalid registration"
        private const val INVALID_DOCUMENT = "Invalid document"
    }

    private val invalidLegalExecutions = mutableListOf<InvalidDataInput<UnitEntity, UnitEntity>>().apply {
        "!@#$%¨&*()_+={}[]ªº?/<>:;|\\\"§".map { it.toString() }.forEach {
            this.add(
                InvalidDataInput(
                    description = "$INVALID_CHARACTER $LEGAL",
                    value = it,
                    execution = {
                        entity.copy(
                            unitType = LEGAL,
                            unitName = "${randomString(MAX_NAME_SIZE - 5)}$it",
                            documentNumber = randomRegistrationNumber(),
                        )
                    },
                    property = UnitEntity::unitName,
                    message = managementUnitValidationUnitNameInvalidValue(),
                )
            )
            this.add(
                InvalidDataInput(
                    description = INVALID_CHARACTER,
                    value = it,
                    execution = { entity.copy(fantasyName = "${randomString(MAX_NAME_SIZE - 5)}$it") },
                    property = UnitEntity::fantasyName,
                    message = managementUnitValidationFantasyNameInvalidValue(),
                )
            )

        }
    }

    private val invalidNaturalExecutions = "9876543210!@#$%¨&*()_+='\"\\|<>,.:?;/^~`´{}[]§¬¢£³²¹".map {
        InvalidDataInput(
            description = "$INVALID_CHARACTER $NATURAL",
            value = it.toString(),
            execution = {
                entity.copy(
                    unitType = NATURAL,
                    unitName = "${randomString(MAX_NAME_SIZE - 5, false)}$it",
                    documentNumber = randomDocumentNumber(),
                )
            },
            property = UnitEntity::unitName,
            message = managementUnitValidationUnitNameInvalidValue(),
        )
    }

    private val minSizeExecutions = mutableListOf<InvalidDataInput<UnitEntity, UnitEntity>>().apply {
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_NAME_SIZE ($MIN_NAME_SIZE)",
                execution = {
                    entity.copy(
                        unitType = LEGAL,
                        unitName = randomString(MIN_NAME_SIZE - 1),
                        documentNumber = randomRegistrationNumber(),
                    )
                },
                property = UnitEntity::unitName,
                message = managementUnitValidationUnitNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_NAME_SIZE ($MIN_NAME_SIZE)",
                execution = {
                    entity.copy(
                        unitType = NATURAL,
                        unitName = randomString(MIN_NAME_SIZE - 1, false),
                        documentNumber = randomDocumentNumber(),
                    )
                },
                property = UnitEntity::unitName,
                message = managementUnitValidationUnitNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_NAME_SIZE ($MIN_NAME_SIZE)",
                execution = { entity.copy(fantasyName = randomString(MIN_NAME_SIZE - 1)) },
                property = UnitEntity::fantasyName,
                message = managementUnitValidationFantasyNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
    }

    private val maxSizeExecutions = mutableListOf<InvalidDataInput<UnitEntity, UnitEntity>>().apply {
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_NAME_SIZE ($MAX_NAME_SIZE)",
                execution = {
                    entity.copy(
                        unitType = LEGAL,
                        unitName = randomString(MAX_NAME_SIZE + 1),
                        documentNumber = randomRegistrationNumber(),
                    )
                },
                property = UnitEntity::unitName,
                message = managementUnitValidationUnitNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_NAME_SIZE ($MAX_NAME_SIZE)",
                execution = {
                    entity.copy(
                        unitType = NATURAL,
                        unitName = randomString(MAX_NAME_SIZE + 1, false),
                        documentNumber = randomDocumentNumber(),
                    )
                },
                property = UnitEntity::unitName,
                message = managementUnitValidationUnitNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_NAME_SIZE ($MAX_NAME_SIZE)",
                execution = { entity.copy(fantasyName = randomString(MAX_NAME_SIZE + 1)) },
                property = UnitEntity::fantasyName,
                message = managementUnitValidationFantasyNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
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
            execution = {
                entity.copy(
                    unitType = LEGAL,
                    documentNumber = it,
                )
            },
            property = UnitEntity::documentNumber,
            message = managementUnitValidationDocumentNumberInvalidValue(),
        )
    }

    private val invalidDocumentExecutions = listOf(
        "517.529.390-02",
        "643815760-40",
        "90.73.50-91",
        "946 26 150-55",
        "530224560420",
        "891551820-99",
        "346.017.430-24",
        "789.940-48",
        "549-009-210-69",
        "111.111.111-11",
        "",
        " ",
    ).map {
        InvalidDataInput(
            description = INVALID_DOCUMENT,
            value = it,
            execution = {
                entity.copy(
                    unitName = randomString(numbers = false),
                    unitType = NATURAL,
                    documentNumber = it,
                )
            },
            property = UnitEntity::documentNumber,
            message = managementUnitValidationDocumentNumberInvalidValue(),
        )
    }

    override fun executions(): List<InvalidDataInput<*, *>> =
        invalidLegalExecutions +
                invalidNaturalExecutions +
                minSizeExecutions +
                maxSizeExecutions +
                invalidRegistrationExecutions +
                invalidDocumentExecutions

    private val entity: UnitEntity
        get() = UnitType.entries.random().let {
            UnitEntity(
                id = randomUUID(),
                unitName = if (it == NATURAL) randomString(numbers = false) else randomString(),
                fantasyName = listOf(null, randomString()).random(),
                documentNumber = if (it == NATURAL) randomDocumentNumber() else randomRegistrationNumber(),
                unitType = it,
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
        }

    @Test
    fun `Valid Unit Entity`() {
        (1..50).forEach { _ ->
            assertDoesNotThrow { entity }
        }
    }

}
