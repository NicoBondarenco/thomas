package com.thomas.management.domain.adapter

import com.thomas.core.authorization.UnauthorizedUserException
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.entity.EntityValidationException
import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.core.model.security.SecurityOrganizationRole.MASTER_ROLE
import com.thomas.core.model.security.SecurityRole
import com.thomas.core.util.BooleanUtils.randomBoolean
import com.thomas.core.util.StringUtils.randomDocumentNumber
import com.thomas.core.util.StringUtils.randomRegistrationNumber
import com.thomas.core.util.StringUtils.randomString
import com.thomas.management.data.entity.UnitEntity
import com.thomas.management.data.entity.value.UnitType.LEGAL
import com.thomas.management.data.entity.value.UnitType.NATURAL
import com.thomas.management.domain.UnitService
import com.thomas.management.domain.exception.UnitNotFoundException
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUnitSearchNotFoundErrorMessage
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUnitValidationOrganizationDataMaxUnit
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUnitValidationUnitDataDuplicatedDocument
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUnitValidationUnitDataDuplicatedName
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUnitValidationUnitDataInvalidData
import com.thomas.management.domain.mock.organizationRepositoryMock
import com.thomas.management.domain.mock.unitDocuments
import com.thomas.management.domain.mock.unitLimit
import com.thomas.management.domain.mock.unitNames
import com.thomas.management.domain.mock.unitNotFound
import com.thomas.management.domain.mock.unitProducerMock
import com.thomas.management.domain.mock.unitRepositoryMock
import com.thomas.management.domain.model.response.UnitResponse
import com.thomas.management.domain.unitCreateRoles
import com.thomas.management.domain.unitDeleteRoles
import com.thomas.management.domain.unitReadRoles
import com.thomas.management.domain.unitUpdateRoles
import com.thomas.management.domain.util.pageRequestPeriod
import com.thomas.management.domain.util.securityUser
import com.thomas.management.domain.util.unitUpsertRequest
import io.mockk.coVerify
import java.util.UUID.randomUUID
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class UnitServiceAdapterTest : DomainValidationTest() {

    companion object {

        @JvmStatic
        fun readRoles() = unitReadRoles.map {
            Arguments.of(it)
        }

        @JvmStatic
        fun createRoles() = unitCreateRoles.map {
            Arguments.of(it)
        }

        @JvmStatic
        fun updateRoles() = unitUpdateRoles.map {
            Arguments.of(it)
        }

        @JvmStatic
        fun deleteRoles() = unitDeleteRoles.map {
            Arguments.of(it)
        }

    }

    private val unitService: UnitService = UnitServiceAdapter(
        organizationRepository = organizationRepositoryMock,
        unitRepository = unitRepositoryMock,
        unitEventProducer = unitProducerMock,
    )

    private val extraValidations: suspend () -> Unit = {
        coroutineScope {
            coVerify(exactly = 0) { unitProducerMock.unitCreated(any()) }
        }
    }

    override fun errorMessage(): String = managementUnitValidationUnitDataInvalidData()

    override fun executions(): List<InvalidDataInput<*, *>> = mutableListOf<InvalidDataInput<*, UnitResponse>>().apply {

        unitCreateRoles.forEach { role ->

            randomString(numbers = false).also {
                unitNames.add(it)
                this.add(
                    InvalidDataInput(
                        description = "Unit same name create ${role.name}",
                        value = it,
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole)
                            unitService.create(
                                unitUpsertRequest.copy(
                                    unitName = it,
                                    unitType = NATURAL,
                                    documentNumber = randomDocumentNumber(),
                                )
                            )
                        },
                        property = UnitEntity::unitName,
                        message = managementUnitValidationUnitDataDuplicatedName(),
                        extraValidations = extraValidations,
                    )
                )
            }

            randomString().also {
                unitNames.add(it)
                this.add(
                    InvalidDataInput(
                        description = "Unit same name create ${role.name}",
                        value = it,
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole)
                            unitService.create(
                                unitUpsertRequest.copy(
                                    unitName = it,
                                    unitType = LEGAL,
                                    documentNumber = randomRegistrationNumber(),
                                )
                            )
                        },
                        property = UnitEntity::unitName,
                        message = managementUnitValidationUnitDataDuplicatedName(),
                        extraValidations = extraValidations,
                    )
                )
            }

            randomDocumentNumber().also {
                unitDocuments.add(it)
                this.add(
                    InvalidDataInput(
                        description = "Unit same document create ${role.name}",
                        value = it,
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole)
                            unitService.create(
                                unitUpsertRequest.copy(
                                    unitName = randomString(numbers = false),
                                    documentNumber = it,
                                    unitType = NATURAL,
                                )
                            )
                        },
                        property = UnitEntity::documentNumber,
                        message = managementUnitValidationUnitDataDuplicatedDocument(),
                        extraValidations = extraValidations,
                    )
                )
            }

            randomRegistrationNumber().also {
                unitDocuments.add(it)
                this.add(
                    InvalidDataInput(
                        description = "Unit same document create ${role.name}",
                        value = it,
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole)
                            unitService.create(
                                unitUpsertRequest.copy(
                                    unitName = randomString(),
                                    documentNumber = it,
                                    unitType = LEGAL,
                                )
                            )
                        },
                        property = UnitEntity::documentNumber,
                        message = managementUnitValidationUnitDataDuplicatedDocument(),
                        extraValidations = extraValidations,
                    )
                )
            }

            randomUUID().also {
                unitLimit.add(it)
                this.add(
                    InvalidDataInput(
                        description = "Unit limit reached create ${role.name}",
                        value = it.toString(),
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole, it)
                            unitService.create(unitUpsertRequest)
                        },
                        property = UnitEntity::unitOrganization,
                        message = managementUnitValidationOrganizationDataMaxUnit(),
                        extraValidations = extraValidations,
                    )
                )
            }

        }

        unitUpdateRoles.forEach { role ->

            randomString(numbers = false).also {
                unitNames.add(it)
                this.add(
                    InvalidDataInput(
                        description = "Unit same name update ${role.name}",
                        value = it,
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole)
                            unitService.update(
                                randomUUID(),
                                unitUpsertRequest.copy(
                                    unitName = it,
                                    unitType = NATURAL,
                                    documentNumber = randomDocumentNumber(),
                                )
                            )
                        },
                        property = UnitEntity::unitName,
                        message = managementUnitValidationUnitDataDuplicatedName(),
                        extraValidations = extraValidations,
                    )
                )
            }

            randomString().also {
                unitNames.add(it)
                this.add(
                    InvalidDataInput(
                        description = "Unit same name update ${role.name}",
                        value = it,
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole)
                            unitService.update(
                                randomUUID(),
                                unitUpsertRequest.copy(
                                    unitName = it,
                                    unitType = LEGAL,
                                    documentNumber = randomRegistrationNumber(),
                                )
                            )
                        },
                        property = UnitEntity::unitName,
                        message = managementUnitValidationUnitDataDuplicatedName(),
                        extraValidations = extraValidations,
                    )
                )
            }

            randomDocumentNumber().also {
                unitDocuments.add(it)
                this.add(
                    InvalidDataInput(
                        description = "Unit same document update ${role.name}",
                        value = it,
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole)
                            unitService.update(
                                randomUUID(),
                                unitUpsertRequest.copy(
                                    unitName = randomString(numbers = false),
                                    unitType = NATURAL,
                                    documentNumber = it,
                                )
                            )
                        },
                        property = UnitEntity::documentNumber,
                        message = managementUnitValidationUnitDataDuplicatedDocument(),
                        extraValidations = extraValidations,
                    )
                )
            }

            randomRegistrationNumber().also {
                unitDocuments.add(it)
                this.add(
                    InvalidDataInput(
                        description = "Unit same document update ${role.name}",
                        value = it,
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole)
                            unitService.update(
                                randomUUID(),
                                unitUpsertRequest.copy(
                                    unitName = randomString(),
                                    unitType = LEGAL,
                                    documentNumber = it,
                                )
                            )
                        },
                        property = UnitEntity::documentNumber,
                        message = managementUnitValidationUnitDataDuplicatedDocument(),
                        extraValidations = extraValidations,
                    )
                )
            }

            randomUUID().also {
                unitLimit.add(it)
                this.add(
                    InvalidDataInput(
                        description = "Unit limit reached update ${role.name}",
                        value = it.toString(),
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole, it)
                            unitService.update(randomUUID(), unitUpsertRequest)
                        },
                        property = UnitEntity::unitOrganization,
                        message = managementUnitValidationOrganizationDataMaxUnit(),
                        extraValidations = extraValidations,
                    )
                )
            }

        }

    }

    @Test
    fun `Unit create without role`() = runTest(StandardTestDispatcher()) {
        currentUser = securityUser
        assertThrows<UnauthorizedUserException> {
            unitService.create(unitUpsertRequest)
        }
    }

    @Test
    fun `Unit update without role`() = runTest(StandardTestDispatcher()) {
        currentUser = securityUser
        assertThrows<UnauthorizedUserException> {
            unitService.update(randomUUID(), unitUpsertRequest)
        }
    }

    @Test
    fun `Unit delete without role`() = runTest(StandardTestDispatcher()) {
        currentUser = securityUser
        assertThrows<UnauthorizedUserException> {
            unitService.delete(randomUUID())
        }
    }

    @Test
    fun `Unit one without role`() = runTest(StandardTestDispatcher()) {
        currentUser = securityUser
        assertThrows<UnauthorizedUserException> {
            unitService.one(randomUUID())
        }
    }

    @Test
    fun `Unit page without role`() = runTest(StandardTestDispatcher()) {
        currentUser = securityUser
        assertThrows<UnauthorizedUserException> {
            unitService.page(null, null, pageRequestPeriod)
        }
    }

    @ParameterizedTest
    @MethodSource("createRoles")
    fun `Create unit success`(
        role: SecurityRole<*, *, *>,
    ) = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(role as SecurityOrganizationRole)
        unitService.create(unitUpsertRequest)
    }

    @ParameterizedTest
    @MethodSource("updateRoles")
    fun `Update unit success`(
        role: SecurityRole<*, *, *>,
    ) = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(role as SecurityOrganizationRole)
        unitService.update(randomUUID(), unitUpsertRequest)
    }

    @ParameterizedTest
    @MethodSource("deleteRoles")
    fun `Delete unit success`(
        role: SecurityRole<*, *, *>,
    ) = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(role as SecurityOrganizationRole)
        unitService.delete(randomUUID())
    }

    @ParameterizedTest
    @MethodSource("readRoles")
    fun `Page with parameters`(
        role: SecurityRole<*, *, *>,
    ) = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(role as SecurityOrganizationRole)
        unitService.page(randomString(), randomBoolean(), pageRequestPeriod)
    }

    @ParameterizedTest
    @MethodSource("readRoles")
    fun `Page without parameters`(
        role: SecurityRole<*, *, *>,
    ) = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(role as SecurityOrganizationRole)
        unitService.page(null, null, pageRequestPeriod)
    }

    @ParameterizedTest
    @MethodSource("readRoles")
    fun `Find one`(
        role: SecurityRole<*, *, *>,
    ) = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(role as SecurityOrganizationRole)
        unitService.one(randomUUID())
    }

    @ParameterizedTest
    @MethodSource("readRoles")
    fun `Find one not found`(
        role: SecurityRole<*, *, *>,
    ) = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(role as SecurityOrganizationRole)
        randomUUID().apply {
            unitNotFound.add(this)
            val exception = assertThrows<UnitNotFoundException> {
                unitService.one(this)
            }
            assertEquals(managementUnitSearchNotFoundErrorMessage(this), exception.message)
        }
    }

    @Test
    fun `All errors create`() = runTest(StandardTestDispatcher()) {
        val existentName = randomString(numbers = false).apply { unitNames.add(this) }
        val existentDocument = randomDocumentNumber().apply { unitDocuments.add(this) }
        val maxUnits = randomUUID().apply { unitLimit.add(this) }

        userWithOrganizationRole(MASTER_ROLE, maxUnits)

        val exception = assertThrows<EntityValidationException> {
            unitService.create(
                unitUpsertRequest.copy(
                    unitName = existentName,
                    documentNumber = existentDocument,
                    unitType = NATURAL,
                )
            )
        }
        assertEquals(managementUnitValidationUnitDataInvalidData(), exception.message)
        val details = (exception.detail as? Map<String, List<String>>)!!

        assertEquals(3, details.size, details.errorListMessage())
        mapOf(
            UnitEntity::unitName to managementUnitValidationUnitDataDuplicatedName(),
            UnitEntity::documentNumber to managementUnitValidationUnitDataDuplicatedDocument(),
            UnitEntity::unitOrganization to managementUnitValidationOrganizationDataMaxUnit(),
        ).forEach { entry ->
            val field = entry.key.name.toSnakeCase()
            assertTrue(details.containsKey(field))
            assertEquals(1, details[field]!!.size)
            assertEquals(entry.value, details[field]!!.first())
        }
    }

    @Test
    fun `All errors update`() = runTest(StandardTestDispatcher()) {
        val existentName = randomString(numbers = false).apply { unitNames.add(this) }
        val existentDocument = randomDocumentNumber().apply { unitDocuments.add(this) }
        val maxUnits = randomUUID().apply { unitLimit.add(this) }

        userWithOrganizationRole(MASTER_ROLE, maxUnits)

        val exception = assertThrows<EntityValidationException> {
            unitService.update(
                randomUUID(),
                unitUpsertRequest.copy(
                    unitName = existentName,
                    documentNumber = existentDocument,
                    unitType = NATURAL,
                )
            )
        }
        assertEquals(managementUnitValidationUnitDataInvalidData(), exception.message)
        val details = (exception.detail as? Map<String, List<String>>)!!

        assertEquals(3, details.size, details.errorListMessage())
        mapOf(
            UnitEntity::unitName to managementUnitValidationUnitDataDuplicatedName(),
            UnitEntity::documentNumber to managementUnitValidationUnitDataDuplicatedDocument(),
            UnitEntity::unitOrganization to managementUnitValidationOrganizationDataMaxUnit(),
        ).forEach { entry ->
            val field = entry.key.name.toSnakeCase()
            assertTrue(details.containsKey(field))
            assertEquals(1, details[field]!!.size)
            assertEquals(entry.value, details[field]!!.first())
        }
    }

}
