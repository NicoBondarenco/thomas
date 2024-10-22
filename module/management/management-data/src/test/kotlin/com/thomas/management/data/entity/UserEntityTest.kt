package com.thomas.management.data.entity

import com.thomas.core.model.general.Gender
import com.thomas.core.util.StringUtils.randomDocumentNumber
import com.thomas.core.util.StringUtils.randomEmail
import com.thomas.core.util.StringUtils.randomPhone
import com.thomas.core.util.StringUtils.randomString
import com.thomas.management.data.entity.generator.OrganizationGenerator.generateOrganizationEntity
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationDocumentNumberInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationFirstNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationFirstNameInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationLastNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationLastNameInvalidValue
import java.time.LocalDate
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID.randomUUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class UserEntityTest : EntityValidationTest() {

    companion object {
        private const val MIN_NAME_SIZE = 2
        private const val MAX_NAME_SIZE = 250

        private const val INVALID_CHARACTER = "Invalid value"
        private const val MIN_SIZE = "Invalid min size"
        private const val MAX_SIZE = "Invalid max size"
        private const val INVALID_DOCUMENT = "Invalid document"
    }

    private val invalidNameExecutions = mutableListOf<InvalidDataInput<UserEntity, UserEntity>>().apply {
        "9876543210!@#$%¨&*()_+='\"\\|<>,.:?;/^~`´{}[]§¬¢£³²¹".map {
            it.toString()
        }.forEach {
            this.add(
                InvalidDataInput(
                    description = INVALID_CHARACTER,
                    value = it,
                    execution = {
                        entity.copy(
                            firstName = "${randomString(MAX_NAME_SIZE - 5, false)}$it",
                        )
                    },
                    property = UserEntity::firstName,
                    message = managementUserValidationFirstNameInvalidValue(),
                )
            )
            this.add(
                InvalidDataInput(
                    description = INVALID_CHARACTER,
                    value = it,
                    execution = {
                        entity.copy(
                            lastName = "${randomString(MAX_NAME_SIZE - 5, false)}$it",
                        )
                    },
                    property = UserEntity::lastName,
                    message = managementUserValidationLastNameInvalidValue(),
                )
            )
        }
    }

    private val minSizeExecutions = mutableListOf<InvalidDataInput<UserEntity, UserEntity>>().apply {
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_NAME_SIZE ($MIN_NAME_SIZE)",
                execution = {
                    entity.copy(
                        firstName = randomString(MIN_NAME_SIZE - 1, false),
                    )
                },
                property = UserEntity::firstName,
                message = managementUserValidationFirstNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_NAME_SIZE ($MIN_NAME_SIZE)",
                execution = {
                    entity.copy(
                        lastName = randomString(MIN_NAME_SIZE - 1, false),
                    )
                },
                property = UserEntity::lastName,
                message = managementUserValidationLastNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
    }

    private val maxSizeExecutions = mutableListOf<InvalidDataInput<UserEntity, UserEntity>>().apply {
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_NAME_SIZE ($MAX_NAME_SIZE)",
                execution = {
                    entity.copy(
                        firstName = randomString(MAX_NAME_SIZE + 1, false),
                    )
                },
                property = UserEntity::firstName,
                message = managementUserValidationFirstNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_NAME_SIZE ($MAX_NAME_SIZE)",
                execution = {
                    entity.copy(
                        lastName = randomString(MAX_NAME_SIZE + 1, false),
                    )
                },
                property = UserEntity::lastName,
                message = managementUserValidationLastNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
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
                    documentNumber = it,
                )
            },
            property = UserEntity::documentNumber,
            message = managementUserValidationDocumentNumberInvalidValue(),
        )
    }

    override fun executions(): List<InvalidDataInput<*, *>> =
        invalidNameExecutions +
                minSizeExecutions +
                maxSizeExecutions +
                invalidDocumentExecutions

    private val entity: UserEntity
        get() = UserEntity(
            id = randomUUID(),
            firstName = randomString(numbers = false),
            lastName = randomString(numbers = false),
            documentNumber = randomDocumentNumber(),
            profilePhoto = listOf(null, "https://profile-photo.com").random(),
            userGender = Gender.entries.random(),
            birthDate = LocalDate.now(),
            userOrganization = generateOrganizationEntity(),
            mainEmail = randomEmail(),
            mainPhone = randomPhone(),
            isActive = listOf(true, false).random(),
            createdAt = now(UTC),
            updatedAt = now(UTC),
        )

    @Test
    fun `Valid User Entity`() {
        (1..50).forEach { _ ->
            assertDoesNotThrow { entity }
        }
    }

}