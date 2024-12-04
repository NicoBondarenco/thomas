package com.thomas.management.domain.adapter

import com.thomas.core.authorization.UnauthorizedUserException
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.model.security.SecurityOrganizationRole.MASTER_ROLE
import com.thomas.core.util.BooleanUtils.randomBoolean
import com.thomas.core.util.StringUtils.randomRegistrationNumber
import com.thomas.core.util.StringUtils.randomString
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.domain.OrganizationService
import com.thomas.management.domain.exception.OrganizationNotFoundException
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementOrganizationSearchNotFoundErrorMessage
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementOrganizationValidationOrganizationDataDuplicatedName
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementOrganizationValidationOrganizationDataDuplicatedRegistration
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementOrganizationValidationOrganizationDataInvalidData
import com.thomas.management.domain.mock.organizationNames
import com.thomas.management.domain.mock.organizationNotFound
import com.thomas.management.domain.mock.organizationProducerMock
import com.thomas.management.domain.mock.organizationRegistrations
import com.thomas.management.domain.mock.organizationRepositoryMock
import com.thomas.management.domain.model.response.OrganizationResponse
import com.thomas.management.domain.util.organizationUpsertRequest
import com.thomas.management.domain.util.pageRequestPeriod
import com.thomas.management.domain.util.securityUser
import io.mockk.coVerify
import java.util.UUID.randomUUID
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class OrganizationServiceAdapterTest : DomainValidationTest() {

    private val organizationService: OrganizationService = OrganizationServiceAdapter(
        organizationRepository = organizationRepositoryMock,
        organizationEventProducer = organizationProducerMock,
    )

    private val extraValidations: suspend () -> Unit = {
        coroutineScope {
            coVerify(exactly = 0) { organizationProducerMock.organizationCreated(any()) }
        }
    }

    override fun errorMessage(): String = managementOrganizationValidationOrganizationDataInvalidData()

    override fun executions(): List<InvalidDataInput<*, *>> = mutableListOf<InvalidDataInput<*, OrganizationResponse>>().apply {
        val existentNameCreate = randomString().apply {
            organizationNames.add(this)
        }
        val existentNameUpdate = randomString().apply {
            organizationNames.add(this)
        }
        val existentRegistrationCreate = randomRegistrationNumber().apply {
            organizationRegistrations.add(this)
        }
        val existentRegistrationUpdate = randomRegistrationNumber().apply {
            organizationRegistrations.add(this)
        }

        this.add(
            InvalidDataInput(
                description = "Organization same name create",
                value = existentNameCreate,
                execution = {
                    userWithOrganizationRole(MASTER_ROLE)
                    organizationService.create(
                        organizationUpsertRequest.copy(
                            organizationName = existentNameCreate
                        )
                    )
                },
                property = OrganizationEntity::organizationName,
                message = managementOrganizationValidationOrganizationDataDuplicatedName(),
                extraValidations = extraValidations,
            )
        )
        this.add(
            InvalidDataInput(
                description = "Organization same registration create",
                value = existentRegistrationCreate,
                execution = {
                    userWithOrganizationRole(MASTER_ROLE)
                    organizationService.create(
                        organizationUpsertRequest.copy(
                            registrationNumber = existentRegistrationCreate
                        )
                    )
                },
                property = OrganizationEntity::registrationNumber,
                message = managementOrganizationValidationOrganizationDataDuplicatedRegistration(),
                extraValidations = extraValidations,
            )
        )
        this.add(
            InvalidDataInput(
                description = "Organization same name update",
                value = existentNameUpdate,
                execution = {
                    userWithOrganizationRole(MASTER_ROLE)
                    organizationService.update(
                        randomUUID(),
                        organizationUpsertRequest.copy(
                            organizationName = existentNameCreate
                        )
                    )
                },
                property = OrganizationEntity::organizationName,
                message = managementOrganizationValidationOrganizationDataDuplicatedName(),
                extraValidations = extraValidations,
            )
        )
        this.add(
            InvalidDataInput(
                description = "Organization same registration update",
                value = existentRegistrationUpdate,
                execution = {
                    userWithOrganizationRole(MASTER_ROLE)
                    organizationService.update(
                        randomUUID(),
                        organizationUpsertRequest.copy(
                            registrationNumber = existentRegistrationUpdate
                        )
                    )
                },
                property = OrganizationEntity::registrationNumber,
                message = managementOrganizationValidationOrganizationDataDuplicatedRegistration(),
                extraValidations = extraValidations,
            )
        )
    }

    @Test
    fun `Organization create without role`() = runTest(StandardTestDispatcher()) {
        currentUser = securityUser
        assertThrows<UnauthorizedUserException> {
            organizationService.create(organizationUpsertRequest)
        }
    }

    @Test
    fun `Organization update without role`() = runTest(StandardTestDispatcher()) {
        currentUser = securityUser
        assertThrows<UnauthorizedUserException> {
            organizationService.update(randomUUID(), organizationUpsertRequest)
        }
    }

    @Test
    fun `Organization one without role`() = runTest(StandardTestDispatcher()) {
        currentUser = securityUser
        assertThrows<UnauthorizedUserException> {
            organizationService.one(randomUUID())
        }
    }

    @Test
    fun `Organization page without role`() = runTest(StandardTestDispatcher()) {
        currentUser = securityUser
        assertThrows<UnauthorizedUserException> {
            organizationService.page(null, null, pageRequestPeriod)
        }
    }

    @Test
    fun `Create organization success`() = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(MASTER_ROLE)
        organizationService.create(organizationUpsertRequest)
    }

    @Test
    fun `Update organization success`() = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(MASTER_ROLE)
        organizationService.update(randomUUID(), organizationUpsertRequest)
    }

    @Test
    fun `Page with parameters`() = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(MASTER_ROLE)
        organizationService.page(randomString(), randomBoolean(), pageRequestPeriod)
    }

    @Test
    fun `Page without parameters`() = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(MASTER_ROLE)
        organizationService.page(null, null, pageRequestPeriod)
    }

    @Test
    fun `Find one`() = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(MASTER_ROLE)
        organizationService.one(randomUUID())
    }

    @Test
    fun `Find one not found`() = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(MASTER_ROLE)
        randomUUID().apply {
            organizationNotFound.add(this)
            val exception = assertThrows<OrganizationNotFoundException> {
                organizationService.one(this)
            }
            assertEquals(managementOrganizationSearchNotFoundErrorMessage(this), exception.message)
        }
    }

}
