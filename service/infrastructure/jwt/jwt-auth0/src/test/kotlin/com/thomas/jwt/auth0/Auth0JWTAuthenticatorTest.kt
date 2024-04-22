package com.thomas.jwt.auth0

import com.auth0.jwt.exceptions.TokenExpiredException
import com.thomas.core.HttpApplicationException
import com.thomas.jwt.auth0.data.repository.SecurityUserMongoRepository
import com.thomas.jwt.auth0.i18n.Auth0JWTAuthenticationMessageI18N.authenticationJWTAuth0TokenExpiredToken
import com.thomas.jwt.auth0.i18n.Auth0JWTAuthenticationMessageI18N.authenticationJWTAuth0TokenNotFound
import com.thomas.jwt.auth0.util.authenticatorConfiguration
import com.thomas.jwt.auth0.util.generateToken
import com.thomas.jwt.auth0.util.users
import com.thomas.jwt.auth0.util.validUser
import io.mockk.every
import io.mockk.mockk
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.assertThrows

@TestInstance(PER_CLASS)
class Auth0JWTAuthenticatorTest {

    private val repository = mockk<SecurityUserMongoRepository>().apply {
        every { this@apply.findSecurityUser(any()) } answers {
            val userId = it.invocation.args[0] as UUID
            users.firstOrNull { user -> user.userId == userId }
        }
    }

    private val configuration = authenticatorConfiguration

    private val authenticator = Auth0JWTAuthenticator(
        configuration = configuration,
        repository = repository,
    )

    @Test
    fun `Token is valid and user is found in repository`() {
        val user = validUser
        val token = configuration.generateToken(user)

        val verified = authenticator.verifyToken(token)
        assertEquals(user, verified)
    }

    @Test
    fun `Token is expired`() {
        val user = validUser
        val token = configuration.generateToken(user = user, validityMinutes = 0)

        val exception = assertThrows<HttpApplicationException> { authenticator.verifyToken(token) }
        assertEquals(authenticationJWTAuth0TokenExpiredToken(), exception.message)
        assertEquals(TokenExpiredException::class, exception.cause!!::class)
    }

    @Test
    fun `User id is not set`() {
        val user = validUser
        val token = configuration.generateToken(user = user, idProperty = "id")

        val exception = assertThrows<HttpApplicationException> { authenticator.verifyToken(token) }
        assertEquals(authenticationJWTAuth0TokenNotFound(), exception.message)
    }

    @Test
    fun `User is not found`() {
        val user = validUser.copy(userId = UUID.randomUUID())
        val token = configuration.generateToken(user = user)

        val exception = assertThrows<HttpApplicationException> { authenticator.verifyToken(token) }
        assertEquals(authenticationJWTAuth0TokenNotFound(), exception.message)
    }

}