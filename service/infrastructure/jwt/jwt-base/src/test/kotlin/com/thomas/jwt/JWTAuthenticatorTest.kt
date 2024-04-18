package com.thomas.jwt

import com.thomas.core.HttpApplicationException
import com.thomas.jwt.configuration.JWTAlgorithmType.RSA256
import com.thomas.jwt.configuration.JWTConfiguration
import com.thomas.jwt.configuration.JWTEncryptionType.RSA
import com.thomas.jwt.i18n.JWTAuthenticationMessageI18N.authenticationJWTBaseTokenInvalidToken
import com.thomas.jwt.i18n.JWTAuthenticationMessageI18N.authenticationJWTBaseUserInactiveUser
import com.thomas.jwt.util.inactiveToken
import com.thomas.jwt.util.invalidToken
import com.thomas.jwt.util.userToken
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class JWTAuthenticatorTest {

    private val jwtConfiguration = JWTConfiguration(
        encryptionType = RSA,
        algorithmType = RSA256,
        privateKey = "jwt-keys/test-private.key",
        publicKey = "jwt-keys/test-public.key",
        issuerName = "test-authentication",
        jwtAudience = "test-service",
        jwtRealm = "test-service",
        customProperties = mapOf("invalidTokens" to listOf(invalidToken.first)),
    )

    private lateinit var authenticator: TestAuthenticator

    @BeforeEach
    fun setup() {
         authenticator = TestAuthenticator(jwtConfiguration)
    }

    @Test
    fun `Validate token success`() {
        authenticator.addToken(userToken)
        assertDoesNotThrow { authenticator.authenticate(userToken.first) }
    }

    @Test
    fun `Validate user inactive`() {
        authenticator.addToken(inactiveToken)
        val exception = assertThrows<HttpApplicationException> {
            authenticator.authenticate(inactiveToken.first)
        }
        assertEquals(authenticationJWTBaseUserInactiveUser(), exception.message)
    }

    @Test
    fun `Validate invalid token`() {
        authenticator.addToken(invalidToken)
        val exception = assertThrows<HttpApplicationException> {
            authenticator.authenticate(invalidToken.first)
        }
        assertEquals(authenticationJWTBaseTokenInvalidToken(), exception.message)
        assertNotNull(exception.cause)
        assertEquals("Token in invalid list", exception.cause!!.message)
    }

    @Test
    fun `Validate keys format`(){
        assertEquals("PKCS#8", authenticator.privateKeyFormat)
        assertEquals("X.509", authenticator.publicKeyFormat)
    }

}