package com.thomas.core.authorization

import com.thomas.core.context.SessionContextHolder.clearContext
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.exception.ErrorType.UNAUTHORIZED_ACTION
import com.thomas.core.i18n.CoreMessageI18N.contextCurrentSessionCurrentUserNotAllowed
import com.thomas.core.model.general.Gender
import com.thomas.core.model.general.UserProfile
import com.thomas.core.model.security.SecurityRole.MASTER
import com.thomas.core.model.security.SecurityUser
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.UUID.randomUUID
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

internal class AuthorizationTest {

    private fun user() = SecurityUser(
        randomUUID(),
        "Security",
        "User",
        "security.user@test.com",
        "16988776655",
        null,
        LocalDate.now(ZoneOffset.UTC),
        Gender.CIS_MALE,
        UserProfile.ADMINISTRATOR,
        true,
        listOf(),
        listOf(),
    )

    @AfterEach
    internal fun tearDown() {
        clearContext()
    }

    @Test
    fun `When role is required and user does not have it, should throws UnauthorizedUserException`() {
        val exception = assertThrows<UnauthorizedUserException> {
            currentUser = user()
            authorized(roles = arrayOf(MASTER)) {}
        }
        assertEquals(contextCurrentSessionCurrentUserNotAllowed(), exception.message)
        assertEquals(UNAUTHORIZED_ACTION, exception.type)
    }

    @Test
    fun `When role is required and user have it, should return the block`() {
        assertDoesNotThrow {
            currentUser = user().copy(
                userRoles = listOf(MASTER)
            )
            assertTrue(authorized(roles = arrayOf(MASTER)) { true })
        }
    }

    @Test
    fun `When roles are not specified, should return the block`() {
        assertDoesNotThrow {
            currentUser = user().copy(
                userRoles = listOf(MASTER)
            )
            assertTrue(authorized { true })
        }
    }

}
