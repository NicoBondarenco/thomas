package com.thomas.core.coroutines.authorization

import com.thomas.core.authorization.UnauthorizedUserException
import com.thomas.core.context.SessionContextHolder.clearContext
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.exception.ErrorType.UNAUTHORIZED_ACTION
import com.thomas.core.generator.SecurityUserGenerator.generate
import com.thomas.core.model.security.SecurityRole.MASTER
import com.thomas.core.random.randomSecurityGroups
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

internal class AuthorizationTest {

    @AfterEach
    internal fun tearDown() {
        clearContext()
    }

    @Test
    fun `When role is required and user does not have it, should throws UnauthorizedUserException`() = runTest(StandardTestDispatcher()) {
        val exception = assertThrows<UnauthorizedUserException> {
            currentUser = generate().copy(
                userRoles = listOf(),
                userGroups = listOf(),
            )
            authorized(roles = arrayOf(MASTER)) {}
        }
        assertEquals(UnauthorizedUserException().message, exception.message)
        assertEquals(UNAUTHORIZED_ACTION, exception.type)
    }

    @Test
    fun `When role is required and user have it, should return the block`() = runTest(StandardTestDispatcher()) {
        assertDoesNotThrow {
            currentUser = generate().copy(
                userRoles = listOf(MASTER),
                userGroups = listOf(),
            )
            assertTrue(authorized(roles = arrayOf(MASTER)) { true })
        }
    }

    @Test
    fun `When role is required and user group have it, should return the block`() = runTest(StandardTestDispatcher()) {
        assertDoesNotThrow {
            currentUser = generate().copy(
                userRoles = listOf(),
                userGroups = listOf(randomSecurityGroups().first().copy(groupRoles = listOf(MASTER))),
            )
            assertTrue(authorized(roles = arrayOf(MASTER)) { true })
        }
    }

    @Test
    fun `When roles are not specified, should return the block`() = runTest(StandardTestDispatcher()) {
        assertDoesNotThrow {
            currentUser = generate().copy(
                userRoles = listOf(),
                userGroups = listOf(),
            )
            assertTrue(authorized { true })
        }
    }

}
