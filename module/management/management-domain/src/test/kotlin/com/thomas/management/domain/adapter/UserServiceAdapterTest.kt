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
import com.thomas.core.util.StringUtils.randomEmail
import com.thomas.core.util.StringUtils.randomString
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.data.entity.UserEntity
import com.thomas.management.domain.UserService
import com.thomas.management.domain.exception.UserNotFoundException
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserSearchNotFoundErrorMessage
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationGroupDataNotFound
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationOrganizationDataMaxUser
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUnitDataNotFound
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUserDataDuplicatedDocument
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUserDataDuplicatedEmail
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUserDataInvalidData
import com.thomas.management.domain.mock.groupRepositoryMock
import com.thomas.management.domain.mock.hasherMock
import com.thomas.management.domain.mock.organizationRepositoryMock
import com.thomas.management.domain.mock.unitRepositoryMock
import com.thomas.management.domain.mock.userDocuments
import com.thomas.management.domain.mock.userEmails
import com.thomas.management.domain.mock.userGroups
import com.thomas.management.domain.mock.userLimit
import com.thomas.management.domain.mock.userNotFound
import com.thomas.management.domain.mock.userProducerMock
import com.thomas.management.domain.mock.userRepositoryMock
import com.thomas.management.domain.mock.userUnits
import com.thomas.management.domain.userCreateRoles
import com.thomas.management.domain.userReadRoles
import com.thomas.management.domain.userUpdateRoles
import com.thomas.management.domain.util.pageRequestPeriod
import com.thomas.management.domain.util.securityUser
import com.thomas.management.domain.util.userCreateRequest
import com.thomas.management.domain.util.userUpdateRequest
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

class UserServiceAdapterTest : DomainValidationTest() {

    companion object {

        @JvmStatic
        fun createRoles() = userCreateRoles.map {
            Arguments.of(it)
        }

        @JvmStatic
        fun updateRoles() = userUpdateRoles.map {
            Arguments.of(it)
        }

        @JvmStatic
        fun readRoles() = userReadRoles.map {
            Arguments.of(it)
        }

    }

    private val userService: UserService = UserServiceAdapter(
        organizationRepository = organizationRepositoryMock,
        groupRepository = groupRepositoryMock,
        unitRepository = unitRepositoryMock,
        userProducer = userProducerMock,
        hasher = hasherMock,
        userRepository = userRepositoryMock,
    )

    private val extraValidationsCreate: suspend () -> Unit = {
        coroutineScope {
            coVerify(exactly = 0) { userProducerMock.userCreated(any()) }
        }
    }

    private val extraValidationsUpdate: suspend () -> Unit = {
        coroutineScope {
            coVerify(exactly = 0) { userProducerMock.userUpdated(any()) }
        }
    }

    override fun errorMessage(): String = managementUserValidationUserDataInvalidData()

    override fun executions(): List<InvalidDataInput<*, *>> = mutableListOf<InvalidDataInput<*, *>>().apply {

        userCreateRoles.forEach { role ->

            randomEmail().also {
                userEmails.add(it)
                this.add(
                    InvalidDataInput(
                        description = "User same email create $role",
                        value = it,
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole)
                            userService.create(
                                userCreateRequest.copy(
                                    mainEmail = it
                                )
                            )
                        },
                        property = UserEntity::mainEmail,
                        message = managementUserValidationUserDataDuplicatedEmail(),
                        extraValidations = extraValidationsCreate,
                    )
                )
            }

            randomDocumentNumber().also {
                userDocuments.add(it)
                this.add(
                    InvalidDataInput(
                        description = "User same document create $role",
                        value = it,
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole)
                            userService.create(
                                userCreateRequest.copy(
                                    documentNumber = it
                                )
                            )
                        },
                        property = UserEntity::documentNumber,
                        message = managementUserValidationUserDataDuplicatedDocument(),
                        extraValidations = extraValidationsCreate,
                    )
                )
            }

            randomUUID().also {
                userLimit.add(it)
                this.add(
                    InvalidDataInput(
                        description = "User limit reached create $role",
                        value = it.toString(),
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole, it)
                            userService.create(userCreateRequest)
                        },
                        property = UserEntity::userOrganization,
                        message = managementUserValidationOrganizationDataMaxUser(),
                        extraValidations = extraValidationsCreate,
                    )
                )
            }

            randomUUID().also {
                userUnits.add(it)
                this.add(
                    InvalidDataInput(
                        description = "User unit not found create $role",
                        value = it.toString(),
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole, it)
                            userService.create(
                                userCreateRequest.copy(
                                    userUnits = listOf(it, randomUUID(), randomUUID()).associateWith { setOf() }
                                )
                            )
                        },
                        property = UserCompleteEntity::userUnits,
                        message = managementUserValidationUnitDataNotFound(setOf(it)),
                        extraValidations = extraValidationsCreate,
                    )
                )
            }

            randomUUID().also {
                userGroups.add(it)
                this.add(
                    InvalidDataInput(
                        description = "User group not found create $role",
                        value = it.toString(),
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole, it)
                            userService.create(
                                userCreateRequest.copy(
                                    userGroups = setOf(it, randomUUID(), randomUUID())
                                )
                            )
                        },
                        property = UserCompleteEntity::userGroups,
                        message = managementUserValidationGroupDataNotFound(setOf(it)),
                        extraValidations = extraValidationsCreate,
                    )
                )
            }

        }

        userUpdateRoles.forEach { role ->

            randomDocumentNumber().also {
                userDocuments.add(it)
                this.add(
                    InvalidDataInput(
                        description = "User same document update $role",
                        value = it,
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole)
                            userService.update(
                                randomUUID(),
                                userUpdateRequest.copy(
                                    documentNumber = it
                                )
                            )
                        },
                        property = UserEntity::documentNumber,
                        message = managementUserValidationUserDataDuplicatedDocument(),
                        extraValidations = extraValidationsUpdate,
                    )
                )
            }

            randomUUID().also {
                userUnits.add(it)
                this.add(
                    InvalidDataInput(
                        description = "User unit not found update $role",
                        value = it.toString(),
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole, it)
                            userService.update(
                                randomUUID(),
                                userUpdateRequest.copy(
                                    userUnits = listOf(it, randomUUID(), randomUUID()).associateWith { setOf() }
                                )
                            )
                        },
                        property = UserCompleteEntity::userUnits,
                        message = managementUserValidationUnitDataNotFound(setOf(it)),
                        extraValidations = extraValidationsUpdate,
                    )
                )
            }

            randomUUID().also {
                userGroups.add(it)
                this.add(
                    InvalidDataInput(
                        description = "User group not found update $role",
                        value = it.toString(),
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole, it)
                            userService.update(
                                randomUUID(),
                                userUpdateRequest.copy(
                                    userGroups = setOf(it, randomUUID(), randomUUID())
                                )
                            )
                        },
                        property = UserCompleteEntity::userGroups,
                        message = managementUserValidationGroupDataNotFound(setOf(it)),
                        extraValidations = extraValidationsUpdate,
                    )
                )
            }

        }

    }

    @Test
    fun `User create without role`() = runTest(StandardTestDispatcher()) {
        currentUser = securityUser
        assertThrows<UnauthorizedUserException> {
            userService.create(userCreateRequest)
        }
    }

    @Test
    fun `User update without role`() = runTest(StandardTestDispatcher()) {
        currentUser = securityUser
        assertThrows<UnauthorizedUserException> {
            userService.update(randomUUID(), userUpdateRequest)
        }
    }

    @Test
    fun `User one without role`() = runTest(StandardTestDispatcher()) {
        currentUser = securityUser
        assertThrows<UnauthorizedUserException> {
            userService.one(randomUUID())
        }
    }

    @Test
    fun `User page without role`() = runTest(StandardTestDispatcher()) {
        currentUser = securityUser
        assertThrows<UnauthorizedUserException> {
            userService.page(null, null, pageRequestPeriod)
        }
    }

    @ParameterizedTest
    @MethodSource("createRoles")
    fun `Create user success`(
        role: SecurityRole<*, *, *>,
    ) = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(role as SecurityOrganizationRole)
        userService.create(userCreateRequest)
        coVerify(exactly = 1) { userProducerMock.userCreated(any()) }
    }

    @ParameterizedTest
    @MethodSource("updateRoles")
    fun `Update user success`(
        role: SecurityRole<*, *, *>,
    ) = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(role as SecurityOrganizationRole)
        userService.update(randomUUID(), userUpdateRequest)
        coVerify(exactly = 1) { userProducerMock.userUpdated(any()) }
    }

    @ParameterizedTest
    @MethodSource("readRoles")
    fun `Page with parameters`(
        role: SecurityRole<*, *, *>,
    ) = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(role as SecurityOrganizationRole)
        userService.page(randomString(), randomBoolean(), pageRequestPeriod)
    }

    @ParameterizedTest
    @MethodSource("readRoles")
    fun `Page without parameters`(
        role: SecurityRole<*, *, *>,
    ) = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(role as SecurityOrganizationRole)
        userService.page(null, null, pageRequestPeriod)
    }

    @ParameterizedTest
    @MethodSource("readRoles")
    fun `Find one`(
        role: SecurityRole<*, *, *>,
    ) = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(role as SecurityOrganizationRole)
        userService.one(randomUUID())
    }

    @ParameterizedTest
    @MethodSource("readRoles")
    fun `Find one not found`(
        role: SecurityRole<*, *, *>,
    ) = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(role as SecurityOrganizationRole)
        randomUUID().apply {
            userNotFound.add(this)
            val exception = assertThrows<UserNotFoundException> {
                userService.one(this)
            }
            assertEquals(managementUserSearchNotFoundErrorMessage(this), exception.message)
        }
    }

    @Test
    fun `All errors create`() = runTest(StandardTestDispatcher()) {
        val existentEmail = randomEmail().apply { userEmails.add(this) }
        val existentDocument = randomDocumentNumber().apply { userDocuments.add(this) }
        val userLimit = randomUUID().apply { userLimit.add(this) }
        val userGroup = randomUUID().apply { userGroups.add(this) }
        val userUnit = randomUUID().apply { userUnits.add(this) }

        userWithOrganizationRole(MASTER_ROLE, userLimit)

        val exception = assertThrows<EntityValidationException> {
            userService.create(
                userCreateRequest.copy(
                    mainEmail = existentEmail,
                    documentNumber = existentDocument,
                    userGroups = setOf(randomUUID(), userGroup),
                    userUnits = mapOf(
                        randomUUID() to setOf(),
                        userUnit to setOf(),
                    )
                )
            )
        }
        assertEquals(managementUserValidationUserDataInvalidData(), exception.message)
        val details = (exception.detail as? Map<String, List<String>>)!!

        assertEquals(5, details.size, details.errorListMessage())
        mapOf(
            UserEntity::mainEmail to managementUserValidationUserDataDuplicatedEmail(),
            UserEntity::documentNumber to managementUserValidationUserDataDuplicatedDocument(),
            UserEntity::userOrganization to managementUserValidationOrganizationDataMaxUser(),
            UserCompleteEntity::userGroups to managementUserValidationGroupDataNotFound(setOf(userGroup)),
            UserCompleteEntity::userUnits to managementUserValidationUnitDataNotFound(setOf(userUnit)),
        ).forEach { entry ->
            val field = entry.key.name.toSnakeCase()
            assertTrue(details.containsKey(field))
            assertEquals(1, details[field]!!.size)
            assertEquals(entry.value, details[field]!!.first())
        }
    }

    @Test
    fun `All errors update`() = runTest(StandardTestDispatcher()) {
        val existentDocument = randomDocumentNumber().apply { userDocuments.add(this) }
        val userGroup = randomUUID().apply { userGroups.add(this) }
        val userUnit = randomUUID().apply { userUnits.add(this) }

        userWithOrganizationRole(MASTER_ROLE)

        val exception = assertThrows<EntityValidationException> {
            userService.update(
                randomUUID(),
                userUpdateRequest.copy(
                    documentNumber = existentDocument,
                    userGroups = setOf(randomUUID(), userGroup),
                    userUnits = mapOf(
                        randomUUID() to setOf(),
                        userUnit to setOf(),
                    )
                )
            )
        }
        assertEquals(managementUserValidationUserDataInvalidData(), exception.message)
        val details = (exception.detail as? Map<String, List<String>>)!!

        assertEquals(3, details.size, details.errorListMessage())
        mapOf(
            UserEntity::documentNumber to managementUserValidationUserDataDuplicatedDocument(),
            UserCompleteEntity::userGroups to managementUserValidationGroupDataNotFound(setOf(userGroup)),
            UserCompleteEntity::userUnits to managementUserValidationUnitDataNotFound(setOf(userUnit)),
        ).forEach { entry ->
            val field = entry.key.name.toSnakeCase()
            assertTrue(details.containsKey(field))
            assertEquals(1, details[field]!!.size)
            assertEquals(entry.value, details[field]!!.first())
        }
    }

}
