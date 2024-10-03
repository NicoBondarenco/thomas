package com.thomas.management.domain.adapter

import com.thomas.core.authorization.UnauthorizedUserException
import com.thomas.core.context.SessionContextHolder.clearContext
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.entity.EntityValidationException
import com.thomas.core.model.pagination.PageRequestData
import com.thomas.management.context.userWithUserCreateRole
import com.thomas.management.context.userWithUserReadRole
import com.thomas.management.context.userWithUserUpdateRole
import com.thomas.management.context.userWithoutRole
import com.thomas.management.data.entity.UserBaseEntity
import com.thomas.management.data.repository.GroupRepositoryMock
import com.thomas.management.data.repository.UserRepositoryMock
import com.thomas.management.domain.UserService
import com.thomas.management.domain.exception.GroupListNotFoundException
import com.thomas.management.domain.exception.UserNotFoundException
import com.thomas.management.domain.exception.UserSignupDisabledException
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationDocumentNumberAlreadyUsed
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationMainEmailAlreadyUsed
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationPhoneNumberAlreadyUsed
import com.thomas.management.domain.properties.UserServiceProperties
import com.thomas.management.requests.activeUserRequest
import com.thomas.management.requests.createUserRequest
import com.thomas.management.requests.signupUserRequest
import com.thomas.management.requests.toUserUpdateRequest
import com.thomas.management.requests.updateUserRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.anyOrNull

class UserServiceAdapterTest {

    private val userRepository = spyk(UserRepositoryMock())
    private val groupRepository = spyk(GroupRepositoryMock())
    private val userProducer = spyk(UserEventProducerMock())
    private val serviceProperties = mockk<UserServiceProperties>()
    private val service: UserService = UserServiceAdapter(
        userRepository = userRepository,
        groupRepository = groupRepository,
        eventProducer = userProducer,
        serviceProperties = serviceProperties,
    )

    @BeforeEach
    fun setup() {
        clearContext()
        userRepository.clear()
        groupRepository.clear()
    }

    @Test
    fun `WHEN search users page without permission SHOULD throws UnauthorizedUserException`() {
        currentUser = userWithoutRole
        assertThrows<UnauthorizedUserException> {
            service.page(pageable = PageRequestData())
        }
        verify(exactly = 0) {
            userRepository.page(anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), any())
        }
    }

    @Test
    fun `WHEN search users page with ROLE_USER_READ permission SHOULD returns the result page`() {
        currentUser = userWithUserReadRole
        userRepository.generateUsers(10)
        service.page(pageable = PageRequestData())
        verify(exactly = 1) {
            userRepository.page(null, null, null, null, null, null, any())
        }
    }

    @Test
    fun `WHEN search one user without permission SHOULD throws UnauthorizedUserException`() {
        currentUser = userWithoutRole
        assertThrows<UnauthorizedUserException> {
            service.one(UUID.randomUUID())
        }
        verify(exactly = 0) { userRepository.one(anyOrNull()) }
    }

    @Test
    fun `WHEN search one user with ROLE_USER_READ permission AND user is not found SHOULD throws UserNotFoundException`() {
        userRepository.generateUsers(10)
        currentUser = userWithUserReadRole
        assertThrows<UserNotFoundException> {
            service.one(UUID.randomUUID())
        }
        verify(exactly = 1) { userRepository.one(any()) }
    }

    @Test
    fun `WHEN search one user with ROLE_USER_READ permission AND user exists SHOULD returns the user data`() {
        val users = userRepository.generateUsers(10)
        currentUser = userWithUserReadRole
        service.one(users.random().id)
        verify(exactly = 1) { userRepository.one(any()) }
    }

    @Test
    fun `WHEN create user without permission SHOULD throws UnauthorizedUserException`() {
        currentUser = userWithoutRole
        assertThrows<UnauthorizedUserException> {
            service.create(createUserRequest)
        }
        verify(exactly = 0) { groupRepository.allByIds(any()) }
        verify(exactly = 0) { userRepository.create(any()) }
        verify(exactly = 0) { userProducer.sendCreatedEvent(any()) }
    }

    @Test
    fun `WHEN signup user without current user SHOULD create the user without roles and groups`() {
        every { serviceProperties.signupEnabled }.returns(true)

        val response = service.signup(signupUserRequest)
        verify(exactly = 0) { groupRepository.allByIds(any()) }
        verify(exactly = 1) { userRepository.signup(any()) }
        verify(exactly = 1) { userProducer.sendSignupEvent(any()) }

        val entity = userRepository.one(response.id)!!
        assertTrue(entity.userRoles.isEmpty())
        assertTrue(entity.userGroups.isEmpty())
    }

    @Test
    fun `WHEN signup user with signup disabled SHOULD throws UserSignupDisabledException`() {
        every { serviceProperties.signupEnabled }.returns(false)

        assertThrows<UserSignupDisabledException> {
            service.signup(signupUserRequest)
        }

    }

    @Test
    fun `WHEN create user with ROLE_USER_CREATE permission SHOULD save and return the user`() {
        currentUser = userWithUserCreateRole
        service.create(createUserRequest)
        verify(exactly = 0) { groupRepository.allByIds(any()) }
        verify(exactly = 1) { userRepository.create(any()) }
        verify(exactly = 1) { userProducer.sendCreatedEvent(any()) }
    }

    @Test
    fun `WHEN create user without optional data SHOULD save and return the user`() {
        currentUser = userWithUserCreateRole
        service.create(
            createUserRequest.copy(
                phoneNumber = null,
                birthDate = null,
                userGender = null,
            )
        )
        verify(exactly = 0) { groupRepository.allByIds(any()) }
        verify(exactly = 1) { userRepository.create(any()) }
        verify(exactly = 1) { userProducer.sendCreatedEvent(any()) }
    }

    @Test
    fun `WHEN create user AND all groups are found SHOULD save and return the user`() {
        currentUser = userWithUserCreateRole
        val groups = groupRepository.generateGroups(5)
        service.create(createUserRequest.copy(userGroups = groups.map { it.id }.toSet()))
        verify(exactly = 1) { groupRepository.allByIds(any()) }
        verify(exactly = 1) { userRepository.create(any()) }
        verify(exactly = 1) { userProducer.sendCreatedEvent(any()) }
    }

    @Test
    fun `WHEN create user with already used email SHOULD throws EntityValidationException`() {
        val errorField = UserBaseEntity::mainEmail.name.toSnakeCase()
        val users = userRepository.generateUsers(10)
        currentUser = userWithUserCreateRole
        val exception = assertThrows<EntityValidationException> {
            service.create(createUserRequest.copy(mainEmail = users.random().mainEmail))
        }
        assertEquals(1, exception.errors.size)
        assertTrue(exception.errors.containsKey(errorField))
        assertEquals(managementUserValidationMainEmailAlreadyUsed(), exception.errors[errorField]!!.first())
        verify(exactly = 0) { groupRepository.allByIds(any()) }
        verify(exactly = 0) { userRepository.create(any()) }
        verify(exactly = 0) { userProducer.sendCreatedEvent(any()) }
    }

    @Test
    fun `WHEN create user with already used document SHOULD throws EntityValidationException`() {
        val errorField = UserBaseEntity::documentNumber.name.toSnakeCase()
        val users = userRepository.generateUsers(10)
        currentUser = userWithUserCreateRole
        val exception = assertThrows<EntityValidationException> {
            service.create(createUserRequest.copy(documentNumber = users.random().documentNumber))
        }
        assertEquals(1, exception.errors.size)
        assertTrue(exception.errors.containsKey(errorField))
        assertEquals(managementUserValidationDocumentNumberAlreadyUsed(), exception.errors[errorField]!!.first())
        verify(exactly = 0) { groupRepository.allByIds(any()) }
        verify(exactly = 0) { userRepository.create(any()) }
        verify(exactly = 0) { userProducer.sendCreatedEvent(any()) }
    }

    @Test
    fun `WHEN create user with already used phone SHOULD throws EntityValidationException`() {
        val errorField = UserBaseEntity::phoneNumber.name.toSnakeCase()
        val users = userRepository.generateUsers(10)
        currentUser = userWithUserCreateRole
        val exception = assertThrows<EntityValidationException> {
            service.create(createUserRequest.copy(phoneNumber = users.random().phoneNumber))
        }
        assertEquals(1, exception.errors.size)
        assertTrue(exception.errors.containsKey(errorField))
        assertEquals(managementUserValidationPhoneNumberAlreadyUsed(), exception.errors[errorField]!!.first())
        verify(exactly = 0) { groupRepository.allByIds(any()) }
        verify(exactly = 0) { userRepository.create(any()) }
        verify(exactly = 0) { userProducer.sendCreatedEvent(any()) }
    }

    @Test
    fun `WHEN create user AND group is not found SHOULD throws GroupListNotFoundException`() {
        currentUser = userWithUserCreateRole
        val groups = groupRepository.generateGroups(5)
        assertThrows<GroupListNotFoundException> {
            service.create(
                createUserRequest.copy(
                    userGroups = (groups.map { it.id } + UUID.randomUUID()).toSet()
                )
            )
        }
        verify(exactly = 1) { groupRepository.allByIds(any()) }
        verify(exactly = 0) { userRepository.create(any()) }
        verify(exactly = 0) { userProducer.sendCreatedEvent(any()) }
    }

    @Test
    fun `WHEN update user without permission SHOULD throws UnauthorizedUserException`() {
        currentUser = userWithoutRole
        assertThrows<UnauthorizedUserException> {
            service.update(UUID.randomUUID(), updateUserRequest)
        }
        verify(exactly = 0) { userRepository.update(any()) }
        verify(exactly = 0) { userProducer.sendUpdatedEvent(any()) }
    }

    @Test
    fun `WHEN update user with ROLE_USER_UPDATE permission SHOULD update and return the user`() {
        currentUser = userWithUserUpdateRole
        val users = userRepository.generateUsers(10)
        service.update(users.random().id, updateUserRequest)
        verify(exactly = 1) { userRepository.update(any()) }
        verify(exactly = 1) { userProducer.sendUpdatedEvent(any()) }
    }

    @Test
    fun `WHEN update user without optional data SHOULD update and return the user`() {
        currentUser = userWithUserUpdateRole
        val users = userRepository.generateUsers(10)
        service.update(
            users.random().id,
            updateUserRequest.copy(
                phoneNumber = null,
                birthDate = null,
                userGender = null,
            )
        )
        verify(exactly = 1) { userRepository.update(any()) }
        verify(exactly = 1) { userProducer.sendUpdatedEvent(any()) }
    }

    @Test
    fun `WHEN update user without updating data SHOULD update and return the user`() {
        currentUser = userWithUserUpdateRole
        val users = userRepository.generateUsers(10)
        users.random().apply {
            service.update(
                this.id,
                this.toUserUpdateRequest()
            )
        }
        verify(exactly = 1) { userRepository.update(any()) }
        verify(exactly = 1) { userProducer.sendUpdatedEvent(any()) }
    }

    @Test
    fun `WHEN update user AND all groups are found SHOULD save and return the user`() {
        currentUser = userWithUserUpdateRole
        val users = userRepository.generateUsers(10)
        val groups = groupRepository.generateGroups(5)
        service.update(users.random().id, updateUserRequest.copy(userGroups = groups.map { it.id }.toSet()))
        verify(exactly = 1) { groupRepository.allByIds(any()) }
        verify(exactly = 1) { userRepository.update(any()) }
        verify(exactly = 1) { userProducer.sendUpdatedEvent(any()) }
    }

    @Test
    fun `WHEN update user AND user is not found SHOULD throws UserNotFoundException`() {
        currentUser = userWithUserUpdateRole
        assertThrows<UserNotFoundException> {
            service.update(
                UUID.randomUUID(),
                updateUserRequest
            )
        }
        verify(exactly = 0) { userRepository.update(any()) }
        verify(exactly = 0) { userProducer.sendUpdatedEvent(any()) }
    }

    @Test
    fun `WHEN update user with already used document SHOULD throws EntityValidationException`() {
        val errorField = UserBaseEntity::documentNumber.name.toSnakeCase()
        val users = userRepository.generateUsers(10)
        currentUser = userWithUserUpdateRole
        val id = users.random().id
        val exception = assertThrows<EntityValidationException> {
            service.update(
                id,
                users.filter {
                    it.id != id
                }.random().let {
                    updateUserRequest.copy(documentNumber = it.documentNumber)
                }
            )
        }
        assertEquals(1, exception.errors.size)
        assertTrue(exception.errors.containsKey(errorField))
        assertEquals(managementUserValidationDocumentNumberAlreadyUsed(), exception.errors[errorField]!!.first())
        verify(exactly = 0) { userRepository.update(any()) }
        verify(exactly = 0) { userProducer.sendUpdatedEvent(any()) }
    }

    @Test
    fun `WHEN update user with already used phone SHOULD throws EntityValidationException`() {
        val errorField = UserBaseEntity::phoneNumber.name.toSnakeCase()
        val users = userRepository.generateUsers(10)
        currentUser = userWithUserUpdateRole
        val id = users.random().id
        val exception = assertThrows<EntityValidationException> {
            service.update(
                id,
                users.filter {
                    it.id != id
                }.random().let {
                    updateUserRequest.copy(phoneNumber = it.phoneNumber)
                }
            )
        }
        assertEquals(1, exception.errors.size)
        assertTrue(exception.errors.containsKey(errorField))
        assertEquals(managementUserValidationPhoneNumberAlreadyUsed(), exception.errors[errorField]!!.first())
        verify(exactly = 0) { userRepository.update(any()) }
        verify(exactly = 0) { userProducer.sendUpdatedEvent(any()) }
    }

    @Test
    fun `WHEN update user AND group is not found SHOULD throws GroupListNotFoundException`() {
        currentUser = userWithUserUpdateRole
        val users = userRepository.generateUsers(10)
        val groups = groupRepository.generateGroups(5)
        assertThrows<GroupListNotFoundException> {
            service.update(
                users.random().id,
                updateUserRequest.copy(
                    userGroups = (groups.map { it.id } + UUID.randomUUID()).toSet()
                )
            )
        }
        verify(exactly = 1) { groupRepository.allByIds(any()) }
        verify(exactly = 0) { userRepository.update(any()) }
        verify(exactly = 0) { userProducer.sendUpdatedEvent(any()) }
    }

    @Test
    fun `WHEN set active user without permission SHOULD throws UnauthorizedUserException`() {
        currentUser = userWithoutRole
        assertThrows<UnauthorizedUserException> {
            service.active(UUID.randomUUID(), activeUserRequest)
        }
        verify(exactly = 0) { userRepository.update(any()) }
        verify(exactly = 0) { userProducer.sendUpdatedEvent(any()) }
    }

    @Test
    fun `WHEN set active user with ROLE_GROUP_UPDATE permission SHOULD update and return the user`() {
        currentUser = userWithUserUpdateRole
        val users = userRepository.generateUsers(10)
        service.active(users.random().id, activeUserRequest)
        verify(exactly = 1) { userRepository.update(any()) }
        verify(exactly = 1) { userProducer.sendUpdatedEvent(any()) }
    }

    @Test
    fun `WHEN set active user AND user is not found SHOULD throws UserNotFoundException`() {
        currentUser = userWithUserUpdateRole
        assertThrows<UserNotFoundException> {
            service.active(UUID.randomUUID(), activeUserRequest)
        }
        verify(exactly = 0) { userRepository.update(any()) }
        verify(exactly = 0) { userProducer.sendUpdatedEvent(any()) }
    }

}
