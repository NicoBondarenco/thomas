package com.thomas.management.domain.adapter

import com.thomas.core.authorization.UnauthorizedUserException
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.data.securityUser
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.entity.EntityValidationException
import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.core.model.security.SecurityOrganizationRole.MASTER_ROLE
import com.thomas.core.model.security.SecurityRole
import com.thomas.core.util.BooleanUtils.randomBoolean
import com.thomas.core.util.StringUtils.randomString
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.GroupEntity
import com.thomas.management.data.entity.pageRequestPeriod
import com.thomas.management.domain.GroupService
import com.thomas.management.domain.exception.GroupNotFoundException
import com.thomas.management.domain.groupCreateRoles
import com.thomas.management.domain.groupDeleteRoles
import com.thomas.management.domain.groupReadRoles
import com.thomas.management.domain.groupUpdateRoles
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementGroupSearchNotFoundErrorMessage
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementGroupValidationGroupDataDuplicatedName
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementGroupValidationGroupDataInvalidData
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementGroupValidationUnitDataNotFound
import com.thomas.management.domain.mock.groupNames
import com.thomas.management.domain.mock.groupNotFound
import com.thomas.management.domain.mock.groupProducerMock
import com.thomas.management.domain.mock.groupRepositoryMock
import com.thomas.management.domain.mock.groupUnits
import com.thomas.management.domain.mock.organizationRepositoryMock
import com.thomas.management.domain.mock.unitRepositoryMock
import com.thomas.management.domain.util.groupUpsertRequest
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

class GroupServiceAdapterTest : DomainValidationTest() {

    companion object {

        @JvmStatic
        fun createRoles() = groupCreateRoles.map {
            Arguments.of(it)
        }

        @JvmStatic
        fun updateRoles() = groupUpdateRoles.map {
            Arguments.of(it)
        }

        @JvmStatic
        fun deleteRoles() = groupDeleteRoles.map {
            Arguments.of(it)
        }

        @JvmStatic
        fun readRoles() = groupReadRoles.map {
            Arguments.of(it)
        }

    }

    private val groupService: GroupService = GroupServiceAdapter(
        organizationRepository = organizationRepositoryMock,
        groupRepository = groupRepositoryMock,
        unitRepository = unitRepositoryMock,
        groupProducer = groupProducerMock,
    )

    private val extraValidationsCreate: suspend () -> Unit = {
        coroutineScope {
            coVerify(exactly = 0) { groupProducerMock.groupCreated(any()) }
        }
    }

    private val extraValidationsUpdate: suspend () -> Unit = {
        coroutineScope {
            coVerify(exactly = 0) { groupProducerMock.groupUpdated(any()) }
        }
    }

    override fun errorMessage(): String = managementGroupValidationGroupDataInvalidData()

    override fun executions(): List<InvalidDataInput<*, *>> = mutableListOf<InvalidDataInput<*, *>>().apply {

        groupCreateRoles.forEach { role ->

            randomString().also {
                groupNames.add(it)
                this.add(
                    InvalidDataInput(
                        description = "Group same name create $role",
                        value = it,
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole)
                            groupService.create(
                                groupUpsertRequest.copy(
                                    groupName = it
                                )
                            )
                        },
                        property = GroupEntity::groupName,
                        message = managementGroupValidationGroupDataDuplicatedName(),
                        extraValidations = extraValidationsCreate,
                    )
                )
            }

            randomUUID().also {
                groupUnits.add(it)
                this.add(
                    InvalidDataInput(
                        description = "Group unit not found create $role",
                        value = it.toString(),
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole, it)
                            groupService.create(
                                groupUpsertRequest.copy(
                                    groupUnits = listOf(it, randomUUID(), randomUUID()).associateWith { setOf() }
                                )
                            )
                        },
                        property = GroupCompleteEntity::groupUnits,
                        message = managementGroupValidationUnitDataNotFound(setOf(it)),
                        extraValidations = extraValidationsCreate,
                    )
                )
            }

        }

        groupUpdateRoles.forEach { role ->

            randomString().also {
                groupNames.add(it)
                this.add(
                    InvalidDataInput(
                        description = "Group same name update $role",
                        value = it,
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole)
                            groupService.update(
                                randomUUID(),
                                groupUpsertRequest.copy(
                                    groupName = it
                                )
                            )
                        },
                        property = GroupEntity::groupName,
                        message = managementGroupValidationGroupDataDuplicatedName(),
                        extraValidations = extraValidationsUpdate,
                    )
                )
            }

            randomUUID().also {
                groupUnits.add(it)
                this.add(
                    InvalidDataInput(
                        description = "Group unit not found update $role",
                        value = it.toString(),
                        execution = {
                            userWithOrganizationRole(role as SecurityOrganizationRole, it)
                            groupService.update(
                                randomUUID(),
                                groupUpsertRequest.copy(
                                    groupUnits = listOf(it, randomUUID(), randomUUID()).associateWith { setOf() }
                                )
                            )
                        },
                        property = GroupCompleteEntity::groupUnits,
                        message = managementGroupValidationUnitDataNotFound(setOf(it)),
                        extraValidations = extraValidationsUpdate,
                    )
                )
            }

        }

    }

    @Test
    fun `Group create without role`() = runTest(StandardTestDispatcher()) {
        currentUser = securityUser
        assertThrows<UnauthorizedUserException> {
            groupService.create(groupUpsertRequest)
        }
    }

    @Test
    fun `Group update without role`() = runTest(StandardTestDispatcher()) {
        currentUser = securityUser
        assertThrows<UnauthorizedUserException> {
            groupService.update(randomUUID(), groupUpsertRequest)
        }
    }

    @Test
    fun `Group delete without role`() = runTest(StandardTestDispatcher()) {
        currentUser = securityUser
        assertThrows<UnauthorizedUserException> {
            groupService.delete(randomUUID())
        }
    }

    @Test
    fun `Group one without role`() = runTest(StandardTestDispatcher()) {
        currentUser = securityUser
        assertThrows<UnauthorizedUserException> {
            groupService.one(randomUUID())
        }
    }

    @Test
    fun `Group page without role`() = runTest(StandardTestDispatcher()) {
        currentUser = securityUser
        assertThrows<UnauthorizedUserException> {
            groupService.page(null, null, pageRequestPeriod)
        }
    }

    @ParameterizedTest
    @MethodSource("createRoles")
    fun `Create group success`(
        role: SecurityRole<*, *, *>,
    ) = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(role as SecurityOrganizationRole)
        groupService.create(groupUpsertRequest)
        coVerify(exactly = 1) { groupProducerMock.groupCreated(any()) }
    }

    @ParameterizedTest
    @MethodSource("updateRoles")
    fun `Update group success`(
        role: SecurityRole<*, *, *>,
    ) = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(role as SecurityOrganizationRole)
        groupService.update(randomUUID(), groupUpsertRequest)
        coVerify(exactly = 1) { groupProducerMock.groupUpdated(any()) }
    }

    @ParameterizedTest
    @MethodSource("deleteRoles")
    fun `Delete group success`(
        role: SecurityRole<*, *, *>,
    ) = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(role as SecurityOrganizationRole)
        groupService.delete(randomUUID())
        coVerify(exactly = 1) { groupProducerMock.groupDeleted(any()) }
    }

    @ParameterizedTest
    @MethodSource("readRoles")
    fun `Page with parameters`(
        role: SecurityRole<*, *, *>,
    ) = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(role as SecurityOrganizationRole)
        groupService.page(randomString(), randomBoolean(), pageRequestPeriod)
    }

    @ParameterizedTest
    @MethodSource("readRoles")
    fun `Page without parameters`(
        role: SecurityRole<*, *, *>,
    ) = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(role as SecurityOrganizationRole)
        groupService.page(null, null, pageRequestPeriod)
    }

    @ParameterizedTest
    @MethodSource("readRoles")
    fun `Find one`(
        role: SecurityRole<*, *, *>,
    ) = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(role as SecurityOrganizationRole)
        groupService.one(randomUUID())
    }

    @ParameterizedTest
    @MethodSource("readRoles")
    fun `Find one not found`(
        role: SecurityRole<*, *, *>,
    ) = runTest(StandardTestDispatcher()) {
        userWithOrganizationRole(role as SecurityOrganizationRole)
        randomUUID().apply {
            groupNotFound.add(this)
            val exception = assertThrows<GroupNotFoundException> {
                groupService.one(this)
            }
            assertEquals(managementGroupSearchNotFoundErrorMessage(this), exception.message)
        }
    }

    @Test
    fun `All errors create`() = runTest(StandardTestDispatcher()) {
        val existentName = randomString().apply { groupNames.add(this) }
        val groupUnit = randomUUID().apply { groupUnits.add(this) }

        userWithOrganizationRole(MASTER_ROLE)

        val exception = assertThrows<EntityValidationException> {
            groupService.create(
                groupUpsertRequest.copy(
                    groupName = existentName,
                    groupUnits = mapOf(
                        randomUUID() to setOf(),
                        groupUnit to setOf(),
                    )
                )
            )
        }
        assertEquals(managementGroupValidationGroupDataInvalidData(), exception.message)
        val details = (exception.detail as? Map<String, List<String>>)!!

        assertEquals(2, details.size, details.errorListMessage())
        mapOf(
            GroupEntity::groupName to managementGroupValidationGroupDataDuplicatedName(),
            GroupCompleteEntity::groupUnits to managementGroupValidationUnitDataNotFound(setOf(groupUnit)),
        ).forEach { entry ->
            val field = entry.key.name.toSnakeCase()
            assertTrue(details.containsKey(field))
            assertEquals(1, details[field]!!.size)
            assertEquals(entry.value, details[field]!!.first())
        }
    }

    @Test
    fun `All errors update`() = runTest(StandardTestDispatcher()) {
        val existentName = randomString().apply { groupNames.add(this) }
        val groupUnit = randomUUID().apply { groupUnits.add(this) }

        userWithOrganizationRole(MASTER_ROLE)

        val exception = assertThrows<EntityValidationException> {
            groupService.update(
                randomUUID(),
                groupUpsertRequest.copy(
                    groupName = existentName,
                    groupUnits = mapOf(
                        randomUUID() to setOf(),
                        groupUnit to setOf(),
                    )
                )
            )
        }
        assertEquals(managementGroupValidationGroupDataInvalidData(), exception.message)
        val details = (exception.detail as? Map<String, List<String>>)!!

        assertEquals(2, details.size, details.errorListMessage())
        mapOf(
            GroupEntity::groupName to managementGroupValidationGroupDataDuplicatedName(),
            GroupCompleteEntity::groupUnits to managementGroupValidationUnitDataNotFound(setOf(groupUnit)),
        ).forEach { entry ->
            val field = entry.key.name.toSnakeCase()
            assertTrue(details.containsKey(field))
            assertEquals(1, details[field]!!.size)
            assertEquals(entry.value, details[field]!!.first())
        }
    }

}
