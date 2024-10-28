package com.thomas.management.domain.adapter

import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.entity.EntityValidationException
import com.thomas.core.util.StringUtils.randomEmail
import com.thomas.core.util.StringUtils.randomRegistrationNumber
import com.thomas.core.util.StringUtils.randomString
import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.UserEntity
import com.thomas.management.domain.SignupService
import com.thomas.management.domain.exception.SignupDisabledException
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementOrganizationValidationOrganizationDataDuplicatedName
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementOrganizationValidationOrganizationDataDuplicatedRegistration
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementSignupValidationSignupDataInvalidData
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementSignupValidationSignupPropertiesSignupDisabled
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUserDataDuplicatedEmail
import com.thomas.management.domain.mock.hasherMock
import com.thomas.management.domain.mock.organizationNames
import com.thomas.management.domain.mock.organizationProducerMock
import com.thomas.management.domain.mock.organizationRegistration
import com.thomas.management.domain.mock.organizationRepositoryMock
import com.thomas.management.domain.mock.signupRepositoryMock
import com.thomas.management.domain.mock.userEmails
import com.thomas.management.domain.mock.userProducerMock
import com.thomas.management.domain.mock.userRepositoryMock
import com.thomas.management.domain.model.response.SignupResponse
import com.thomas.management.domain.properties.SignupProperties
import com.thomas.management.domain.util.signupRequest
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SignupServiceAdapterTest : DomainValidationTest() {

    companion object {
        private var SIGNUP_ENABLED = true
    }

    private val signupProperties: SignupProperties = mockk<SignupProperties>().apply {
        every { signupEnabled } answers { SIGNUP_ENABLED }
    }

    private val signupService: SignupService = SignupServiceAdapter(
        signupRepository = signupRepositoryMock,
        organizationRepository = organizationRepositoryMock,
        userRepository = userRepositoryMock,
        signupProperties = signupProperties,
        hasher = hasherMock,
        organizationEventProducer = organizationProducerMock,
        userEventProducer = userProducerMock,
    )

    private val extraValidations: suspend () -> Unit = {
        coroutineScope {
            coVerify(exactly = 0) { organizationProducerMock.organizationCreated(any()) }
            coVerify(exactly = 0) { userProducerMock.userCreated(any()) }
        }
    }

    override fun executions(): List<InvalidDataInput<*, *>> = mutableListOf<InvalidDataInput<*, SignupResponse>>().apply {
        val existentName = randomString().apply {
            organizationNames.add(this)
        }
        val existentRegistration = randomRegistrationNumber().apply {
            organizationRegistration.add(this)
        }
        val existentEmail = randomEmail().apply {
            userEmails.add(this)
        }
        this.add(
            InvalidDataInput(
                description = "Organization same name",
                value = existentName,
                execution = {
                    signupService.signup(
                        signupRequest.let {
                            it.copy(
                                organizationData = it.organizationData.copy(
                                    organizationName = existentName
                                )
                            )
                        }
                    )
                },
                property = OrganizationEntity::organizationName,
                message = managementOrganizationValidationOrganizationDataDuplicatedName(),
                extraValidations = extraValidations,
            )
        )
        this.add(
            InvalidDataInput(
                description = "Organization same registration",
                value = existentRegistration,
                execution = {
                    signupService.signup(
                        signupRequest.let {
                            it.copy(
                                organizationData = it.organizationData.copy(
                                    registrationNumber = existentRegistration
                                )
                            )
                        }
                    )
                },
                property = OrganizationEntity::registrationNumber,
                message = managementOrganizationValidationOrganizationDataDuplicatedRegistration(),
                extraValidations = extraValidations,
            )
        )
        this.add(
            InvalidDataInput(
                description = "User same email",
                value = existentEmail,
                execution = {
                    signupService.signup(
                        signupRequest.let {
                            it.copy(
                                userData = it.userData.copy(
                                    mainEmail = existentEmail
                                )
                            )
                        }
                    )
                },
                property = UserEntity::mainEmail,
                message = managementUserValidationUserDataDuplicatedEmail(),
                extraValidations = extraValidations,
            )
        )
    }

    override fun errorMessage(): String = managementSignupValidationSignupDataInvalidData()

    @BeforeEach
    override fun beforeEach() {
        super.beforeEach()
        organizationNames.clear()
        organizationRegistration.clear()
        userEmails.clear()
        SIGNUP_ENABLED = true
    }

    @Test
    fun `Success signup`() = runTest(StandardTestDispatcher()) {
        signupService.signup(signupRequest)
        coVerify(exactly = 1) { organizationProducerMock.organizationCreated(any()) }
        coVerify(exactly = 1) { userProducerMock.userCreated(any()) }
    }

    @Test
    fun `Disabled signup`() = runTest(StandardTestDispatcher()) {
        SIGNUP_ENABLED = false
        val exception = assertThrows<SignupDisabledException> {
            signupService.signup(signupRequest)
        }
        assertEquals(managementSignupValidationSignupPropertiesSignupDisabled(), exception.message)
        coVerify(exactly = 0) { organizationProducerMock.organizationCreated(any()) }
        coVerify(exactly = 0) { userProducerMock.userCreated(any()) }
    }

    @Test
    fun `All errors`() = runTest(StandardTestDispatcher()) {
        val existentName = randomString().apply {
            organizationNames.add(this)
        }
        val existentRegistration = randomRegistrationNumber().apply {
            organizationRegistration.add(this)
        }
        val existentEmail = randomEmail().apply {
            userEmails.add(this)
        }
        val exception = assertThrows<EntityValidationException> {
            signupService.signup(
                signupRequest.let {
                    it.copy(
                        organizationData = it.organizationData.copy(
                            organizationName = existentName,
                            registrationNumber = existentRegistration,
                        ),
                        userData = it.userData.copy(
                            mainEmail = existentEmail,
                        )
                    )
                }
            )
        }

        assertEquals(errorMessage(), exception.message)
        val details = (exception.detail as? Map<String, List<String>>)!!
        assertEquals(3, details.size)
        mapOf(
            OrganizationEntity::organizationName to managementOrganizationValidationOrganizationDataDuplicatedName(),
            OrganizationEntity::registrationNumber to managementOrganizationValidationOrganizationDataDuplicatedRegistration(),
            UserEntity::mainEmail to managementUserValidationUserDataDuplicatedEmail(),
        ).forEach {
            val field = it.key.name.toSnakeCase()
            assertTrue(details.containsKey(field))
            assertEquals(1, details[field]!!.size)
            assertEquals(it.value, details[field]!!.first())
        }

    }

}
