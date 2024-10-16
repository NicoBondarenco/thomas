package com.thomas.core.authorization

import com.thomas.core.context.SessionContextHolder.clearContext
import com.thomas.core.context.SessionContextHolder.currentUnit
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.exception.ErrorType.UNAUTHORIZED_ACTION
import com.thomas.core.generator.UserGenerator.generateSecurityUserWithRoles
import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.core.model.security.SecurityUnitRole
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class AuthorizationTest {

    @AfterEach
    internal fun tearDown() {
        clearContext()
    }

    @ParameterizedTest
    @EnumSource(SecurityOrganizationRole::class)
    fun `When organization role is required and user does not have it, should throws UnauthorizedUserException`(
        role: SecurityOrganizationRole
    ) = runTest(StandardTestDispatcher()) {
        currentUser = generateSecurityUserWithRoles(
            userUnitRoles = SecurityUnitRole.entries.toSet(),
            groupUnitRoles = SecurityUnitRole.entries.toSet(),
        )
        val exception = assertThrows<UnauthorizedUserException> {
            authorized(roles = arrayOf(role)) {}
        }
        assertEquals(UnauthorizedUserException().message, exception.message)
        assertEquals(UNAUTHORIZED_ACTION, exception.type)
    }

    @ParameterizedTest
    @EnumSource(SecurityUnitRole::class)
    fun `When unit role is required and user does not have it, should throws UnauthorizedUserException`(
        role: SecurityUnitRole
    ) = runTest(StandardTestDispatcher()) {
        currentUser = generateSecurityUserWithRoles(
            userOrganizationRoles = SecurityOrganizationRole.entries.toSet(),
            groupOrganizationRoles = SecurityOrganizationRole.entries.toSet(),
        )
        val exception = assertThrows<UnauthorizedUserException> {
            authorized(roles = arrayOf(role)) {}
        }
        assertEquals(UnauthorizedUserException().message, exception.message)
        assertEquals(UNAUTHORIZED_ACTION, exception.type)
    }

    @ParameterizedTest
    @EnumSource(SecurityOrganizationRole::class)
    fun `When organization role is required and user have it, should return the block`(
        role: SecurityOrganizationRole
    ) = runTest(StandardTestDispatcher()) {
        currentUser = generateSecurityUserWithRoles(
            userOrganizationRoles = setOf(role),
        )
        assertDoesNotThrow {
            assertTrue(authorized(roles = arrayOf(role)) { true })
        }
    }

    @ParameterizedTest
    @EnumSource(SecurityOrganizationRole::class)
    fun `When organization role is required and group have it, should return the block`(
        role: SecurityOrganizationRole
    ) = runTest(StandardTestDispatcher()) {
        currentUser = generateSecurityUserWithRoles(
            groupOrganizationRoles = setOf(role),
        )
        assertDoesNotThrow {
            assertTrue(authorized(roles = arrayOf(role)) { true })
        }
    }

    @ParameterizedTest
    @EnumSource(SecurityUnitRole::class)
    fun `When unit role is required and user have it, should return the block`(
        role: SecurityUnitRole
    ) = runTest(StandardTestDispatcher()) {
        currentUser = generateSecurityUserWithRoles(
            userUnitRoles = setOf(role),
        )
        currentUnit = currentUser.userUnits.random().unitId
        assertDoesNotThrow {
            assertTrue(authorized(roles = arrayOf(role)) { true })
        }
    }

    @ParameterizedTest
    @EnumSource(SecurityUnitRole::class)
    fun `When unit role is required and group have it, should return the block`(
        role: SecurityUnitRole
    ) = runTest(StandardTestDispatcher()) {
        currentUser = generateSecurityUserWithRoles(
            groupUnitRoles = setOf(role),
        )
        currentUnit = currentUser.userGroups.random().groupUnits.random().unitId
        assertDoesNotThrow {
            assertTrue(authorized(roles = arrayOf(role)) { true })
        }
    }

    @Test
    fun `When roles are not specified, should return the block`() = runTest(StandardTestDispatcher()) {
        assertDoesNotThrow {
            currentUser = generateSecurityUserWithRoles()
            assertTrue(authorized { true })
        }
    }

}
