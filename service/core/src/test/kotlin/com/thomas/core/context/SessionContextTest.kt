package com.thomas.core.context

import com.thomas.core.HttpApplicationException
import com.thomas.core.context.SessionContextHolder.clearContext
import com.thomas.core.context.SessionContextHolder.context
import com.thomas.core.context.SessionContextHolder.currentLocale
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.model.general.Gender
import com.thomas.core.model.general.UserProfile
import com.thomas.core.security.SecurityGroup
import com.thomas.core.security.SecurityRole.MASTER
import com.thomas.core.security.SecurityUser
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Locale.ENGLISH
import java.util.Locale.FRENCH
import java.util.Locale.ROOT
import java.util.UUID
import java.util.UUID.randomUUID
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

internal class SessionContextTest {

    private val user = SecurityUser(
        randomUUID(),
        "Security",
        "User",
        "security.user@test.com",
        "16988776655",
        null,
        LocalDate.now(ZoneOffset.UTC),
        Gender.CIS_MALE,
        UserProfile.COMMON,
        true,
        listOf(
            MASTER,
        ),
        listOf(
            SecurityGroup(
                UUID.fromString("7115cb17-cd5d-48d0-b968-95c8cb92e54a"), "Security Group",
                listOf(
                    MASTER
                ),
            ),
        )
    )

    @AfterEach
    internal fun tearDown() {
        clearContext()
    }

    @Test
    fun `Clear session context`() {
        currentUser = user
        currentLocale = FRENCH

        assertEquals(user, context.currentUser)
        assertEquals(FRENCH, context.currentLocale)

        clearContext()

        assertThrows<HttpApplicationException> { currentUser }
        assertEquals(ROOT, currentLocale)
    }

    @Test
    fun `When no locale is informed, ROOT should be default`() {
        assertEquals(ROOT, SessionContext().currentLocale)
    }

    @Test
    fun `When a locale is informed, the informed locale should be set`() {
        val session = SessionContext(mutableMapOf()).apply {
            this.currentLocale = ENGLISH
        }
        assertEquals(ENGLISH, session.currentLocale)
    }

    @Test
    fun `When no user is set, should throws exception`() {
        assertThrows(HttpApplicationException::class.java) {
            SessionContext().currentUser
        }
    }

    @Test
    fun `When user is set, should returns the users`() {
        assertDoesNotThrow {
            SessionContext().apply {
                currentUser = user
            }.currentUser
        }
    }

    @Test
    fun `When a custom property exists, should return the respective value`() {
        val uuid = randomUUID().toString()
        val property = "custom"
        val session = SessionContext(mutableMapOf(property to uuid))
        assertEquals(uuid, session.getProperty(property))
    }

    @Test
    fun `When a custom property does not exists and is set, should return the respective value`() {
        val uuid = randomUUID().toString()
        val property = "custom"
        val session = SessionContext(mutableMapOf())
        session.setProperty(property, uuid)
        assertEquals(uuid, session.getProperty(property))
    }

    @Test
    fun `When a custom property exists and is set, should return the new value`() {
        val uuid = randomUUID().toString()
        val property = "custom"
        val session = SessionContext(mutableMapOf(property to "ad95cb8c-1f60-4b9b-8a8e-5c25dacb7a7b"))
        session.setProperty(property, uuid)
        assertEquals(uuid, session.getProperty(property))
    }

    @Test
    fun `When create a session, current token should be null`(){
        val session = SessionContext(mutableMapOf())
        assertNull(session.currentToken)
    }

    @Test
    fun `When token is set in a session, current token should be returned`(){
        val uuid = randomUUID().toString()
        val session = SessionContext(mutableMapOf()).apply {
            currentToken = uuid
        }
        assertEquals(uuid, session.currentToken)
    }

}