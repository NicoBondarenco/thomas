package com.thomas.core.authorization

import com.thomas.core.context.SessionContextHolder.clearContext
import com.thomas.core.context.SessionContextHolder.currentMember
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.exception.ErrorType.UNAUTHORIZED_ACTION
import com.thomas.core.generator.UserGenerator.generateSecurityUserWithRoles
import com.thomas.core.model.security.SecurityMemberRole
import com.thomas.core.model.security.SecurityOrganizationRole
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
            userMemberRoles = SecurityMemberRole.entries.toSet(),
            groupMemberRoles = SecurityMemberRole.entries.toSet(),
        )
        val exception = assertThrows<UnauthorizedUserException> {
            authorized(roles = arrayOf(role)) {}
        }
        assertEquals(UnauthorizedUserException().message, exception.message)
        assertEquals(UNAUTHORIZED_ACTION, exception.type)
    }

    @ParameterizedTest
    @EnumSource(SecurityMemberRole::class)
    fun `When member role is required and user does not have it, should throws UnauthorizedUserException`(
        role: SecurityMemberRole
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
    @EnumSource(SecurityMemberRole::class)
    fun `When member role is required and user have it, should return the block`(
        role: SecurityMemberRole
    ) = runTest(StandardTestDispatcher()) {
        currentUser = generateSecurityUserWithRoles(
            userMemberRoles = setOf(role),
        )
        currentMember = currentUser.userMembers.random().memberId
        assertDoesNotThrow {
            assertTrue(authorized(roles = arrayOf(role)) { true })
        }
    }

    @ParameterizedTest
    @EnumSource(SecurityMemberRole::class)
    fun `When member role is required and group have it, should return the block`(
        role: SecurityMemberRole
    ) = runTest(StandardTestDispatcher()) {
        currentUser = generateSecurityUserWithRoles(
            groupMemberRoles = setOf(role),
        )
        currentMember = currentUser.userGroups.random().groupMembers.random().memberId
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
