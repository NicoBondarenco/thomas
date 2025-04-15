package com.thomas.spring.base.authentication

import com.thomas.core.authorization.UnauthorizedUserException
import com.thomas.core.data.securityUserRoles
import com.thomas.spring.base.configuration.JacksonConfiguration
import com.thomas.spring.base.exception.JWTTokenException
import com.thomas.spring.base.i18n.SpringMessageI18N.authenticationTokenRetrieveUserInactiveUser
import com.thomas.spring.base.i18n.SpringMessageI18N.authenticationTokenValidateTokenDecodeError
import com.thomas.spring.base.i18n.SpringMessageI18N.authenticationTokenValidateTokenExpiredToken
import com.thomas.spring.base.i18n.SpringMessageI18N.authenticationTokenValidateTokenInvalidIssuer
import com.thomas.spring.base.i18n.SpringMessageI18N.authenticationTokenValidateTokenInvalidSignature
import com.thomas.spring.base.mock.jwtConfiguration
import com.thomas.spring.base.util.TokenBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class JWTAuth0AuthenticatorTest {

    private val authenticator: Authenticator = JWTAuth0Authenticator(
        jwtConfiguration,
        JacksonConfiguration().objectMapper()
    )

    private val tokenBuilder: TokenBuilder = TokenBuilder(jwtConfiguration)

    @Test
    fun `Authenticate token with inactive user throws UnauthorizedUserException`() {
        val token = tokenBuilder.generateToken(
            user = securityUserRoles.copy(isActive = false),
        )
        val exception = assertThrows<UnauthorizedUserException> { authenticator.authenticate(token) }
        assertEquals(authenticationTokenRetrieveUserInactiveUser(), exception.message)
    }

    @Test
    fun `Authenticate token with active user returns the SecurityUser`() {
        val secUser = securityUserRoles.copy(isActive = true)
        val token = tokenBuilder.generateToken(
            user = secUser,
        )
        val user = authenticator.authenticate(token)
        assertEquals(secUser, user)
    }

    @Test
    fun `Authenticate expired token throws JWTAuth0TokenException`() {
        val token = tokenBuilder.generateToken(
            user = securityUserRoles.copy(isActive = true),
            validityMinutes = 0
        )
        val exception = assertThrows<JWTTokenException> { authenticator.authenticate(token) }
        assertEquals(authenticationTokenValidateTokenExpiredToken(), exception.message)
    }

    @Test
    fun `Authenticate invalid token throws JWTAuth0TokenException`() {
        val token = tokenBuilder.generateToken(
            user = securityUserRoles.copy(isActive = true),
        ).substring(0, 50)

        val exception = assertThrows<JWTTokenException> { authenticator.authenticate(token) }
        assertEquals(authenticationTokenValidateTokenDecodeError(), exception.message)
    }

    @Test
    fun `Authenticate invalid token signature throws JWTAuth0TokenException`() {
        val token = tokenBuilder.generateToken(
            user = securityUserRoles.copy(isActive = true),
        ).let { it.substring(0, it.length - 2) }

        val exception = assertThrows<JWTTokenException> { authenticator.authenticate(token) }
        assertEquals(authenticationTokenValidateTokenInvalidSignature(), exception.message)
    }

    @Test
    fun `Authenticate invalid token issuer throws JWTAuth0TokenException`() {
        val token = tokenBuilder.generateToken(
            user = securityUserRoles.copy(isActive = true),
            issuerName = "qwerty"
        )

        val exception = assertThrows<JWTTokenException> { authenticator.authenticate(token) }
        assertEquals(authenticationTokenValidateTokenInvalidIssuer(), exception.message)
    }

}