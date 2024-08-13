package com.thomas.authentication.domain.adapter

import com.thomas.authentication.data.entity.UserAuthenticationEntity
import com.thomas.authentication.data.repository.UserAuthenticationRepository
import com.thomas.authentication.domain.UserAuthenticationService
import com.thomas.authentication.domain.exception.UserAuthenticationNotFoundException
import com.thomas.authentication.domain.i18n.AuthenticationDomainMessageI18N.authenticationUserAuthenticationNotFoundErrorMessage
import com.thomas.authentication.event.userAuthentication
import com.thomas.authentication.event.userUpsertedEvent
import com.thomas.core.exception.ErrorType
import com.thomas.core.exception.ErrorType.NOT_FOUND
import com.thomas.core.model.security.SecurityRole
import com.thomas.hash.Hasher
import com.thomas.management.message.event.UserUpsertedEvent
import io.mockk.every
import io.mockk.mockk
import java.util.UUID
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextInt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserAuthenticationServiceAdapterTest {

    private val users: MutableMap<UUID, UserAuthenticationEntity> = mutableMapOf()

    private val repository: UserAuthenticationRepository = mockk<UserAuthenticationRepository>().apply {
        every { this@apply.create(any()) } answers {
            (it.invocation.args[0] as UserAuthenticationEntity).apply {
                users[this.id] = this
            }
        }
        every { this@apply.update(any()) } answers {
            (it.invocation.args[0] as UserAuthenticationEntity).apply {
                users[this.id] = this
            }
        }
        every { this@apply.one(any()) } answers {
            (it.invocation.args[0] as UUID).let { id -> users[id] }
        }
    }

    private val hasher: Hasher = mockk<Hasher>().apply {
        every { this@apply.hash(any(), any()) } answers {
            val args = it.invocation.args
            "${args[0]}-${args[1]}"
        }
    }

    private val service: UserAuthenticationService = UserAuthenticationServiceAdapter(repository, hasher)

    @Test
    fun `WHEN creating user by event THEN should save user with password and salt hashes`() {
        val salt = randomUUID().toString()
        val event = userUpsertedEvent
        every { hasher.generateSalt() } returns salt
        service.create(event)

        val user = users[event.id]

        assertNotNull(user)
        user!!.assertWithEvent(event)
    }

    @Test
    fun `WHEN updating user by event THEN should save user without changing password and salt hashes`() {
        val entity = userAuthentication
        users[entity.id] = entity

        val event = userUpsertedEvent.copy(
            id = entity.id,
            userRoles = SecurityRole.entries.shuffled().subList(0, nextInt(1, 4)).toSet(),
            userGroups = (1..nextInt(1, 5)).map { randomUUID() }.toSet()
        )

        service.update(event)

        val user = users[event.id]

        assertNotNull(user)
        user!!.assertWithEvent(event)
    }

    @Test
    fun `WHEN updating user by event AND user is not found THEN should throws UserAuthenticationNotFoundException`() {
        val event = userUpsertedEvent

        val exception = assertThrows<UserAuthenticationNotFoundException> { service.update(event) }
        assertEquals(authenticationUserAuthenticationNotFoundErrorMessage(event.id), exception.message)
        assertEquals(NOT_FOUND, exception.type)
    }

    private fun UserAuthenticationEntity.assertWithEvent(event: UserUpsertedEvent) {
        assertEquals(event.firstName, this.firstName)
        assertEquals(event.lastName, this.lastName)
        assertEquals(event.mainEmail, this.mainEmail)
        assertEquals(event.documentNumber, this.documentNumber)
        assertEquals(event.phoneNumber, this.phoneNumber)
        assertEquals(event.profilePhoto, this.profilePhoto)
        assertEquals(event.birthDate, this.birthDate)
        assertEquals(event.userGender, this.userGender)
        assertEquals(event.isActive, this.isActive)
        assertEquals(event.createdAt, this.createdAt)
        assertEquals(event.updatedAt, this.updatedAt)
        assertEquals(event.userRoles, this.userRoles)
        assertEquals(event.userGroups, this.groupsIds)
    }

}
