package com.thomas.core.context

import com.thomas.core.model.general.Gender
import com.thomas.core.model.security.SecurityUser
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Locale
import java.util.Locale.CHINA
import java.util.Locale.FRENCH
import java.util.Locale.GERMAN
import java.util.Locale.ITALY
import java.util.Locale.KOREA
import java.util.Locale.ROOT
import java.util.UUID
import java.util.UUID.randomUUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class SessionContextHolderTest {

    private val threads = 5
    private val executors = Executors.newFixedThreadPool(threads)
    private val latch = CountDownLatch(threads)

    private val locales = mutableMapOf<String, Locale>(
        "01" to FRENCH,
        "02" to FRENCH,
        "03" to FRENCH,
        "04" to FRENCH,
        "05" to FRENCH,
    )

    private val users = mutableMapOf<String, SecurityUser?>(
        "01" to null,
        "02" to null,
        "03" to null,
        "04" to null,
        "05" to null,
    )

    private fun ExecutorService.submitTest(
        session: SessionData
    ) = this.submit {
        session.locale?.apply { SessionContextHolder.context.currentLocale = this }
        SessionContextHolder.context.currentUser = SecurityUser(
            session.id,
            "User ${session.number}",
            "Last Name ${session.number}",
            "user${session.number}@test.com",
            "16988776655",
            null,
            LocalDate.now(ZoneOffset.UTC),
            Gender.CIS_MALE,
            true,
            listOf(),
            listOf()
        )
        runBlocking { delay(session.delay) }
        locales[session.number] = SessionContextHolder.context.currentLocale
        users[session.number] = SessionContextHolder.context.currentUser
        latch.countDown()
    }

    private data class SessionData(
        val id: UUID = randomUUID(),
        val locale: Locale?,
        val number: String,
        val delay: Long,
    )

    @Test
    fun `Given different threads, they should be treated separately`() {
        val sessions = listOf(
            SessionData(locale = CHINA, number = "01", delay = 1500),
            SessionData(locale = GERMAN, number = "02", delay = 500),
            SessionData(locale = ITALY, number = "03", delay = 1500),
            SessionData(locale = null, number = "04", delay = 3500),
            SessionData(locale = KOREA, number = "05", delay = 800),
        )

        sessions.forEach { executors.submitTest(it) }

        latch.await(10, TimeUnit.SECONDS)

        sessions.forEach {
            assertEquals(it.locale ?: ROOT, locales[it.number])
            assertNotNull(users[it.number])
            val user = users[it.number]!!

            assertEquals(it.id, user.userId)
            assertEquals("User ${it.number}", user.firstName)
            assertEquals("Last Name ${it.number}", user.lastName)
            assertEquals("user${it.number}@test.com", user.mainEmail)
        }
    }

    @Test
    fun `When there is no user logged in, should throws exception`() {
        assertThrows<UnauthenticatedUserException> { SessionContextHolder.currentUser }
    }

    @Test
    fun `When a custom property does not exists and is set, should return the respective value`() {
        val uuid = randomUUID().toString()
        val property = "custom"
        SessionContextHolder.setSessionProperty(property, uuid)
        assertEquals(uuid, SessionContextHolder.getSessionProperty(property))
    }

    @Test
    fun `When create a session, current token should be null`() {
        assertNull(SessionContextHolder.currentToken)
    }

    @Test
    fun `When token is set in a session, current token should be returned`() {
        val uuid = randomUUID().toString()
        SessionContextHolder.currentToken = uuid
        assertEquals(uuid, SessionContextHolder.currentToken)
    }

}
