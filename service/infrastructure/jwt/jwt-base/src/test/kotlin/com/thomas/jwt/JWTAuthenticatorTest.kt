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
        privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDRgRxRTLqgkKcAE41+FAO8V3GVL6GuZtDcDcLVzFfGFWWv2eCO80/yXGTyVCUd7GrW" +
                "NPA4NbJPhQYQVhqzKeI76Q2TJWNDxZALIK9mTno4n4/6tIw2ZzdVrISaitIefDpfomtddl2PJJ4d3NgCCPxb00qOZDc/gAy1V1Y41OvmQHFaEG2O/U6v" +
                "HESN2joeKjXklv6bkaNLELuihzowDwlpKNGtuD1XAU213IGGXivexP6Ffy8EqqpfzTklhtJRILWpddlC6eWdaYZSmKnlOAitqaA3Z/XKRFX2w5hnVQ64" +
                "g85eVROlmoeoZX31FhvV/zxsZdeFPnKZca2AQ9swW1W3AgMBAAECggEAIrDiY4P1E0lgnd6e+1WNGM7R/AYbG6niw81zibuAfUhlwiytxn37qwIppQbR" +
                "7paLSlURwf0CPbClRGc9nJU+5PS+TrNOMJayN2V+Jc6w5ldlm/nxjcChpQogGwcJrAqeD9/xL1S+QUmq9P7wtcWPJBVkrxdhHp6xfAWuPVLkjQepxSyk" +
                "Ygpf/10BqlDeILmsCL6Tefhcy4fWeIWTnN3gg5+aX0eQcgLcw/doKL9xGrvYSroeUlD7AD+F/jB1TmHVx070nZ5Ft8XOxtfy5cYyZyVgLhCC9PzNRSgd" +
                "mK8RArBk1So0nlVCCnEGsXBLuu7q6okvIrjK2mAp8xaDdC8VwQKBgQD7991ME00si6exKUHL0c9Rqkcuhj41CJxfwQ8dAivMw9BOc9itwCW0NRXeWSgE" +
                "+Ub3Z+sfDq/cq6hSECyEjc5taM29l1yrV6Oq0UQD+C3woJ09BELryvR0T8VUAJ8ZUsMSffeaK4xYxINWGl3u8gGD3j2dUBujYt2JHV3bAaBtPwKBgQDU" +
                "2007Xncvl05uCsyB4z3/0YKS1JkxNfvoYAFnwsDba0+wo8tfzkOE9cdjqxkLkzADwJOQ2Na6DDegpmvd4PiKp8Z+c7dsJBjty7d/Mo6qj6YFs6W5ACq+" +
                "mS8UuvWEgDwrL14XzQxhqceE7hZRzcEA5T/fVzOprWbXZ+N8xORhiQKBgGgcH1UauI2KWsyp1mToGTiOGgFQOI3jM/GgrKT4y648zmpMFaCdPzb5pEKO" +
                "Jin/8eGyD4VypRmR1+eohSx0B3JPlosa6pBlktu2wdq+BgVh5/ZZN2mEH4ObnlY6N4LYHoAail8qVUAsWnQOSve7CQM6pczFV9YnuI+6EZi3UFkzAoGA" +
                "BpwEqupFAypMmiglr4bR3wavakXFt/x4JQAOKx2mBWowoEiFwSTTfeZv2Y1viqd4XzV0n5PMHxzQAWIJiWs57HxHMpf/QxFf4MppmT6FpZAuVJD+fV8e" +
                "9KonBxrDEuk0dRbUciLA1quB8YO/F60u6As2T4YSqZsjzV6BdtdaTBkCgYB+N+tnO+3c1gZP+kt0TRGfKjSgVcRF7CROey8s3duRjcljkh54b9Hhk/iM" +
                "rCd+VTT51hY9Rfs/xYwDZaQPxHZrVjnOU+BAL3bqg6m6YRhZw8TXUSInvIfCAD+FMQ6TXbWa7ZmS4D5yMlUdzqc+cMUcwNzh7jaCRp0a9JSRb6L0fA==",
        publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0YEcUUy6oJCnABONfhQDvFdxlS+hrmbQ3A3C1cxXxhVlr9ngjvNP8l" +
                "xk8lQlHexq1jTwODWyT4UGEFYasyniO+kNkyVjQ8WQCyCvZk56OJ+P+rSMNmc3VayEmorSHnw6X6JrXXZdjySeHdzYAgj8W9NK" +
                "jmQ3P4AMtVdWONTr5kBxWhBtjv1OrxxEjdo6Hio15Jb+m5GjSxC7ooc6MA8JaSjRrbg9VwFNtdyBhl4r3sT+hX8vBKqqX805JY" +
                "bSUSC1qXXZQunlnWmGUpip5TgIramgN2f1ykRV9sOYZ1UOuIPOXlUTpZqHqGV99RYb1f88bGXXhT5ymXGtgEPbMFtVtwIDAQAB",
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