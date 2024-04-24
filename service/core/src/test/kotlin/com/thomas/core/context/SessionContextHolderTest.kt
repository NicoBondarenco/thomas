package com.thomas.core.context

import com.thomas.core.HttpApplicationException
import com.thomas.core.model.general.Gender
import com.thomas.core.model.general.UserProfile
import com.thomas.core.model.security.SecurityUser
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Locale
import java.util.UUID
import java.util.UUID.randomUUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class SessionContextHolderTest {

    @Test
    fun `Given different threads, they should be treated separately`() {
        val threads = 5
        val executors = Executors.newFixedThreadPool(threads)
        val latch = CountDownLatch(threads)

        var locale01 = Locale.FRENCH
        var locale02 = Locale.FRENCH
        var locale03 = Locale.FRENCH
        var locale04 = Locale.FRENCH
        var locale05 = Locale.FRENCH

        val idCurrentUser01 = UUID.randomUUID()
        val idCurrentUser02 = UUID.randomUUID()
        val idCurrentUser03 = UUID.randomUUID()
        val idCurrentUser04 = UUID.randomUUID()
        val idCurrentUser05 = UUID.randomUUID()

        var currentUser01: SecurityUser? = null
        var currentUser02: SecurityUser? = null
        var currentUser03: SecurityUser? = null
        var currentUser04: SecurityUser? = null
        var currentUser05: SecurityUser? = null

        executors.submit {
            SessionContextHolder.context.currentLocale = Locale.CHINA
            SessionContextHolder.context.currentUser = SecurityUser(
                idCurrentUser01,
                "User 01",
                "Last Name 01",
                "user01@test.com",
                "16988776655",
                null,
                LocalDate.now(ZoneOffset.UTC),
                Gender.CIS_MALE,
                UserProfile.COMMON,
                true,
                listOf(),
                listOf()
            )
            Thread.sleep(1500)
            locale01 = SessionContextHolder.context.currentLocale
            currentUser01 = SessionContextHolder.context.currentUser
            latch.countDown()
        }

        executors.submit {
            SessionContextHolder.context.currentLocale = Locale.GERMAN
            SessionContextHolder.context.currentUser = SecurityUser(
                idCurrentUser02,
                "User 02",
                "Last Name 02",
                "user02@test.com",
                "16988776655",
                null,
                LocalDate.now(ZoneOffset.UTC),
                Gender.CIS_MALE,
                UserProfile.COMMON,
                true,
                listOf(),
                listOf()
            )
            Thread.sleep(500)
            locale02 = SessionContextHolder.context.currentLocale
            currentUser02 = SessionContextHolder.context.currentUser
            latch.countDown()
        }

        executors.submit {
            SessionContextHolder.context.currentLocale = Locale.ITALY
            SessionContextHolder.context.currentUser = SecurityUser(
                idCurrentUser03,
                "User 03",
                "Last Name 03",
                "user03@test.com",
                "16988776655",
                null,
                LocalDate.now(ZoneOffset.UTC),
                Gender.CIS_MALE,
                UserProfile.COMMON,
                true,
                listOf(),
                listOf()
            )
            Thread.sleep(1500)
            locale03 = SessionContextHolder.context.currentLocale
            currentUser03 = SessionContextHolder.context.currentUser
            latch.countDown()
        }

        executors.submit {
            SessionContextHolder.context.currentUser = SecurityUser(
                idCurrentUser04,
                "User 04",
                "Last Name 04",
                "user04@test.com",
                "16988776655",
                null,
                LocalDate.now(ZoneOffset.UTC),
                Gender.CIS_MALE,
                UserProfile.COMMON,
                true,
                listOf(),
                listOf()
            )
            Thread.sleep(3500)
            locale04 = SessionContextHolder.context.currentLocale
            currentUser04 = SessionContextHolder.context.currentUser
            latch.countDown()
        }

        executors.submit {
            SessionContextHolder.context.currentLocale = Locale.KOREA
            SessionContextHolder.context.currentUser = SecurityUser(
                idCurrentUser05,
                "User 05",
                "Last Name 05",
                "user05@test.com",
                "16988776655",
                null,
                LocalDate.now(ZoneOffset.UTC),
                Gender.CIS_MALE,
                UserProfile.COMMON,
                true,
                listOf(),
                listOf()
            )
            Thread.sleep(800)
            locale05 = SessionContextHolder.context.currentLocale
            currentUser05 = SessionContextHolder.context.currentUser
            latch.countDown()
        }

        latch.await(10, TimeUnit.SECONDS)

        assertEquals(locale01, Locale.CHINA)
        assertEquals(locale02, Locale.GERMAN)
        assertEquals(locale03, Locale.ITALY)
        assertEquals(locale04, Locale.ROOT)
        assertEquals(locale05, Locale.KOREA)

        assertEquals(idCurrentUser01, currentUser01?.userId)
        assertEquals("User 01", currentUser01?.firstName)
        assertEquals("Last Name 01", currentUser01?.lastName)
        assertEquals("user01@test.com", currentUser01?.mainEmail)

        assertEquals(idCurrentUser02, currentUser02?.userId)
        assertEquals("User 02", currentUser02?.firstName)
        assertEquals("Last Name 02", currentUser02?.lastName)
        assertEquals("user02@test.com", currentUser02?.mainEmail)

        assertEquals(idCurrentUser03, currentUser03?.userId)
        assertEquals("User 03", currentUser03?.firstName)
        assertEquals("Last Name 03", currentUser03?.lastName)
        assertEquals("user03@test.com", currentUser03?.mainEmail)

        assertEquals(idCurrentUser04, currentUser04?.userId)
        assertEquals("User 04", currentUser04?.firstName)
        assertEquals("Last Name 04", currentUser04?.lastName)
        assertEquals("user04@test.com", currentUser04?.mainEmail)

        assertEquals(idCurrentUser05, currentUser05?.userId)
        assertEquals("User 05", currentUser05?.firstName)
        assertEquals("Last Name 05", currentUser05?.lastName)
        assertEquals("user05@test.com", currentUser05?.mainEmail)

    }

    @Test
    fun `When there is no user logged in, should throws exception`() {
        assertThrows<HttpApplicationException> { SessionContextHolder.currentUser }
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