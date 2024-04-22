package com.thomas.core.authorization

import com.thomas.core.HttpApplicationException
import com.thomas.core.context.SessionContextHolder.clearContext
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.i18n.CoreMessageI18N.coreContextSessionUserNotAllowed
import com.thomas.core.model.general.Gender
import com.thomas.core.model.general.UserProfile
import com.thomas.core.model.http.HTTPStatus.FORBIDDEN
import com.thomas.core.model.security.SecurityRole.MASTER
import com.thomas.core.model.security.SecurityUser
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.UUID.randomUUID
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test

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
    fun `When role is required and user does not have it, should throws exception`() {
        try {
            currentUser = user()
            authorized(roles = arrayOf(MASTER)) {}
            fail("Should have thrown HttpApplicationException")
        } catch (e: HttpApplicationException) {
            assertEquals(FORBIDDEN, e.status)
            assertEquals(coreContextSessionUserNotAllowed(), e.message)
        } catch (e: Exception) {
            fail("Should have thrown HttpApplicationException -> ${e::class.java} - ${e.message}")
        }
    }

    @Test
    fun `When role is required and user have it, should return the block`() {
        try {
            currentUser = user().copy(
                userRoles = listOf(MASTER)
            )
            assertTrue(authorized(roles = arrayOf(MASTER)) { true })
        } catch (e: Exception) {
            fail("Should not have thrown Exception -> ${e::class.java} - ${e.message}")
        }
    }

    @Test
    fun `When roles are not specified, should return the block`() {
        try {
            currentUser = user().copy(
                userRoles = listOf(MASTER)
            )
            assertTrue(authorized { true })
        } catch (e: Exception) {
            fail("Should not have thrown Exception -> ${e::class.java} - ${e.message}")
        }
    }

}