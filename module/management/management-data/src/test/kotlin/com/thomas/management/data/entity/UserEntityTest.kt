package com.thomas.management.data.entity

import com.thomas.core.model.general.UserType.ADMINISTRATOR
import com.thomas.core.model.general.UserType.MASTER
import com.thomas.core.model.security.SecurityOrganizationRole.MASTER_ROLE
import com.thomas.core.model.security.SecurityOrganizationRole.ORGANIZATION_ALL
import com.thomas.core.util.StringUtils.randomString
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationDocumentNumberInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationFirstNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationFirstNameInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationLastNameInvalidLength
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationLastNameInvalidValue
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationUserTypeAdministratorUser
import com.thomas.management.data.i18n.ManagementDataMessageI18N.managementUserValidationUserTypeMasterUser
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
        private const val INVALID_TYPE = "Invalid type"
    }

    private val simpleInvalidNameExecutions = mutableListOf<InvalidDataInput<UserSimpleEntity, UserSimpleEntity>>().apply {
        "9876543210!@#$%¨&*()_+='\"\\|<>,.:?;/^~`´{}[]§¬¢£³²¹".map {
            it.toString()
        }.forEach {
            this.add(
                InvalidDataInput(
                    description = INVALID_CHARACTER,
                    value = it,
                    execution = {
                        userEntity.copy(
                            firstName = "${randomString(MAX_NAME_SIZE - 5, false)}$it",
                        )
                    },
                    property = UserSimpleEntity::firstName,
                    message = managementUserValidationFirstNameInvalidValue(),
                )
            )
            this.add(
                InvalidDataInput(
                    description = INVALID_CHARACTER,
                    value = it,
                    execution = {
                        userEntity.copy(
                            lastName = "${randomString(MAX_NAME_SIZE - 5, false)}$it",
                        )
                    },
                    property = UserSimpleEntity::lastName,
                    message = managementUserValidationLastNameInvalidValue(),
                )
            )
        }
    }

    private val simpleMinSizeExecutions = mutableListOf<InvalidDataInput<UserSimpleEntity, UserSimpleEntity>>().apply {
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_NAME_SIZE ($MIN_NAME_SIZE)",
                execution = {
                    userEntity.copy(
                        firstName = randomString(MIN_NAME_SIZE - 1, false),
                    )
                },
                property = UserSimpleEntity::firstName,
                message = managementUserValidationFirstNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_NAME_SIZE ($MIN_NAME_SIZE)",
                execution = {
                    userEntity.copy(
                        lastName = randomString(MIN_NAME_SIZE - 1, false),
                    )
                },
                property = UserSimpleEntity::lastName,
                message = managementUserValidationLastNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
    }

    private val simpleMaxSizeExecutions = mutableListOf<InvalidDataInput<UserSimpleEntity, UserSimpleEntity>>().apply {
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_NAME_SIZE ($MAX_NAME_SIZE)",
                execution = {
                    userEntity.copy(
                        firstName = randomString(MAX_NAME_SIZE + 1, false),
                    )
                },
                property = UserSimpleEntity::firstName,
                message = managementUserValidationFirstNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_NAME_SIZE ($MAX_NAME_SIZE)",
                execution = {
                    userEntity.copy(
                        lastName = randomString(MAX_NAME_SIZE + 1, false),
                    )
                },
                property = UserSimpleEntity::lastName,
                message = managementUserValidationLastNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
    }

    private val completeInvalidNameExecutions = mutableListOf<InvalidDataInput<UserCompleteEntity, UserCompleteEntity>>().apply {
        "9876543210!@#$%¨&*()_+='\"\\|<>,.:?;/^~`´{}[]§¬¢£³²¹".map {
            it.toString()
        }.forEach {
            this.add(
                InvalidDataInput(
                    description = INVALID_CHARACTER,
                    value = it,
                    execution = {
                        userCompleteEntity.copy(
                            firstName = "${randomString(MAX_NAME_SIZE - 5, false)}$it",
                        )
                    },
                    property = UserCompleteEntity::firstName,
                    message = managementUserValidationFirstNameInvalidValue(),
                )
            )
            this.add(
                InvalidDataInput(
                    description = INVALID_CHARACTER,
                    value = it,
                    execution = {
                        userCompleteEntity.copy(
                            lastName = "${randomString(MAX_NAME_SIZE - 5, false)}$it",
                        )
                    },
                    property = UserCompleteEntity::lastName,
                    message = managementUserValidationLastNameInvalidValue(),
                )
            )
        }
    }

    private val completeMinSizeExecutions = mutableListOf<InvalidDataInput<UserCompleteEntity, UserCompleteEntity>>().apply {
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_NAME_SIZE ($MIN_NAME_SIZE)",
                execution = {
                    userCompleteEntity.copy(
                        firstName = randomString(MIN_NAME_SIZE - 1, false),
                    )
                },
                property = UserCompleteEntity::firstName,
                message = managementUserValidationFirstNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MIN_SIZE,
                value = "MIN_NAME_SIZE ($MIN_NAME_SIZE)",
                execution = {
                    userCompleteEntity.copy(
                        lastName = randomString(MIN_NAME_SIZE - 1, false),
                    )
                },
                property = UserCompleteEntity::lastName,
                message = managementUserValidationLastNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
    }

    private val completeMaxSizeExecutions = mutableListOf<InvalidDataInput<UserCompleteEntity, UserCompleteEntity>>().apply {
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_NAME_SIZE ($MAX_NAME_SIZE)",
                execution = {
                    userCompleteEntity.copy(
                        firstName = randomString(MAX_NAME_SIZE + 1, false),
                    )
                },
                property = UserCompleteEntity::firstName,
                message = managementUserValidationFirstNameInvalidLength(MIN_NAME_SIZE, MAX_NAME_SIZE),
            )
        )
        this.add(
            InvalidDataInput(
                description = MAX_SIZE,
                value = "MAX_NAME_SIZE ($MAX_NAME_SIZE)",
                execution = {
                    userCompleteEntity.copy(
                        lastName = randomString(MAX_NAME_SIZE + 1, false),
                    )
                },
                property = UserCompleteEntity::lastName,
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
                userEntity.copy(
                    documentNumber = it,
                )
            },
            property = UserSimpleEntity::documentNumber,
            message = managementUserValidationDocumentNumberInvalidValue(),
        )
        InvalidDataInput(
            description = INVALID_DOCUMENT,
            value = it,
            execution = {
                userCompleteEntity.copy(
                    documentNumber = it,
                )
            },
            property = UserCompleteEntity::documentNumber,
            message = managementUserValidationDocumentNumberInvalidValue(),
        )
    }

    private val invalidTypeExecutions = mapOf(
        MASTER to managementUserValidationUserTypeMasterUser(),
        ADMINISTRATOR to managementUserValidationUserTypeAdministratorUser(),
    ).map {
        InvalidDataInput(
            description = INVALID_TYPE,
            value = it.key.name,
            execution = {
                userEntity.copy(
                    userType = it.key,
                    organizationRoles = setOf()
                )
            },
            property = UserSimpleEntity::userType,
            message = it.value,
        )
        InvalidDataInput(
            description = INVALID_TYPE,
            value = it.key.name,
            execution = {
                userCompleteEntity.copy(
                    userType = it.key,
                    organizationRoles = setOf()
                )
            },
            property = UserCompleteEntity::userType,
            message = it.value,
        )
    }

    override fun executions(): List<InvalidDataInput<*, *>> =
        simpleInvalidNameExecutions +
                simpleMinSizeExecutions +
                simpleMaxSizeExecutions +
                invalidDocumentExecutions +
                completeInvalidNameExecutions +
                completeMinSizeExecutions +
                completeMaxSizeExecutions +
                invalidTypeExecutions

    @Test
    fun `Valid User Entity`() {
        (1..50).forEach { _ ->
            assertDoesNotThrow { userEntity }
            assertDoesNotThrow { userCompleteEntity }
        }
    }

    @Test
    fun `Valid User Type Master`() {
        assertDoesNotThrow {
            userEntity.copy(
                userType = MASTER,
                organizationRoles = setOf(MASTER_ROLE)
            )
        }
        assertDoesNotThrow {
            userCompleteEntity.copy(
                userType = MASTER,
                organizationRoles = setOf(MASTER_ROLE)
            )
        }
    }

    @Test
    fun `Valid User Type Administrator`() {
        assertDoesNotThrow {
            userEntity.copy(
                userType = ADMINISTRATOR,
                organizationRoles = setOf(ORGANIZATION_ALL)
            )
        }
        assertDoesNotThrow {
            userCompleteEntity.copy(
                userType = ADMINISTRATOR,
                organizationRoles = setOf(ORGANIZATION_ALL)
            )
        }
    }

}