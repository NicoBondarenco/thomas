package com.thomas.authentication.jwt.auth0

import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.thomas.authentication.Authenticator
import com.thomas.authentication.jwt.auth0.configuration.JWTAuth0AuthenticatorFactory
import com.thomas.authentication.jwt.auth0.data.UserAuthentication
import com.thomas.authentication.jwt.auth0.exception.JWTAuth0TokenException
import com.thomas.authentication.jwt.auth0.i18n.AuthenticationJWTAuth0MessageI18N.authenticationTokenRetrieveClaimInvalidId
import com.thomas.authentication.jwt.auth0.i18n.AuthenticationJWTAuth0MessageI18N.authenticationTokenRetrieveClaimMissingId
import com.thomas.authentication.jwt.auth0.i18n.AuthenticationJWTAuth0MessageI18N.authenticationTokenRetrieveUserInactiveUser
import com.thomas.authentication.jwt.auth0.i18n.AuthenticationJWTAuth0MessageI18N.authenticationTokenRetrieveUserNotFound
import com.thomas.authentication.jwt.auth0.i18n.AuthenticationJWTAuth0MessageI18N.authenticationTokenValidateTokenDecodeError
import com.thomas.authentication.jwt.auth0.i18n.AuthenticationJWTAuth0MessageI18N.authenticationTokenValidateTokenExpiredToken
import com.thomas.authentication.jwt.auth0.i18n.AuthenticationJWTAuth0MessageI18N.authenticationTokenValidateTokenInvalidIssuer
import com.thomas.authentication.jwt.auth0.i18n.AuthenticationJWTAuth0MessageI18N.authenticationTokenValidateTokenInvalidSignature
import com.thomas.authentication.jwt.auth0.repository.UserAuthenticationRepository
import com.thomas.authentication.jwt.auth0.util.activeUser
import com.thomas.authentication.jwt.auth0.util.authenticationUsers
import com.thomas.authentication.jwt.auth0.util.defaultConfiguration
import com.thomas.authentication.jwt.auth0.util.generateToken
import com.thomas.authentication.jwt.auth0.util.inactiveUser
import com.thomas.core.authorization.UnauthorizedUserException
import io.mockk.every
import io.mockk.mockk
import java.util.UUID
import java.util.UUID.randomUUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class JWTAuth0AuthenticatorTest {

    private val repository = mockk<UserAuthenticationRepository>().apply {
        every { findUserAuthentication(any(UUID::class)) } answers {
            val userId = it.invocation.args[0] as UUID
            authenticationUsers.firstOrNull { u -> u.id == userId }
        }
    }

    private val authenticator: Authenticator = JWTAuth0Authenticator(
        defaultConfiguration,
        repository
    )

    @Test
    fun `Factory creates a new instance`() {
        val database = mockk<MongoDatabase> {
            every {
                getCollection(defaultConfiguration.userCollection, UserAuthentication::class.java)
            } returns mockk<MongoCollection<UserAuthentication>>()

        }
        assertDoesNotThrow { JWTAuth0AuthenticatorFactory.create(database, defaultConfiguration) }
    }

    @Test
    fun `Decode token without user id claim throws JWTAuth0TokenException`() {
        val token = generateToken(
            user = activeUser,
            idProperty = "qwerty"
        )
        val exception = assertThrows<JWTAuth0TokenException> { authenticator.decode(token) }
        assertEquals(authenticationTokenRetrieveClaimMissingId(), exception.message)
    }

    @Test
    fun `Decode token with invalid user id claim throws JWTAuth0TokenException`() {
        val token = generateToken(
            user = activeUser,
            idValue = "qwerty"
        )
        val exception = assertThrows<JWTAuth0TokenException> { authenticator.decode(token) }
        assertEquals(authenticationTokenRetrieveClaimInvalidId(), exception.message)
    }

    @Test
    fun `Decode token with inexistent user throws UnauthorizedUserException`() {
        val token = generateToken(
            user = activeUser,
            idValue = randomUUID().toString()
        )
        val exception = assertThrows<UnauthorizedUserException> { authenticator.decode(token) }
        assertEquals(authenticationTokenRetrieveUserNotFound(), exception.message)
    }

    @Test
    fun `Decode token with existent user returns the SecurityUser`() {
        val token = generateToken(
            user = activeUser,
        )
        val user = authenticator.decode(token)
        assertEquals(activeUser, user)
    }

    @Test
    fun `Authenticate token with inactive user throws UnauthorizedUserException`() {
        val token = generateToken(
            user = inactiveUser,
        )
        val exception = assertThrows<UnauthorizedUserException> { authenticator.authenticate(token) }
        assertEquals(authenticationTokenRetrieveUserInactiveUser(), exception.message)
    }

    @Test
    fun `Authenticate token with active user returns the SecurityUser`() {
        val token = generateToken(
            user = activeUser,
        )
        val user = authenticator.authenticate(token)
        assertEquals(activeUser, user)
    }

    @Test
    fun `Authenticate expired token throws JWTAuth0TokenException`() {
        val token = generateToken(
            user = activeUser,
            validityMinutes = 0
        )
        val exception = assertThrows<JWTAuth0TokenException> { authenticator.authenticate(token) }
        assertEquals(authenticationTokenValidateTokenExpiredToken(), exception.message)
    }

    @Test
    fun `Authenticate invalid token throws JWTAuth0TokenException`() {
        val token = generateToken(
            user = activeUser,
        ).substring(0, 50)

        val exception = assertThrows<JWTAuth0TokenException> { authenticator.authenticate(token) }
        assertEquals(authenticationTokenValidateTokenDecodeError(), exception.message)
    }

    @Test
    fun `Authenticate invalid token signature throws JWTAuth0TokenException`() {
        val token = generateToken(
            user = activeUser,
        ).let { it.substring(0, it.length - 2) }

        val exception = assertThrows<JWTAuth0TokenException> { authenticator.authenticate(token) }
        assertEquals(authenticationTokenValidateTokenInvalidSignature(), exception.message)
    }

    @Test
    fun `Authenticate invalid token issuer throws JWTAuth0TokenException`() {
        val token = generateToken(
            user = activeUser,
            issuerName = "qwerty"
        )

        val exception = assertThrows<JWTAuth0TokenException> { authenticator.authenticate(token) }
        assertEquals(authenticationTokenValidateTokenInvalidIssuer(), exception.message)
    }

}
