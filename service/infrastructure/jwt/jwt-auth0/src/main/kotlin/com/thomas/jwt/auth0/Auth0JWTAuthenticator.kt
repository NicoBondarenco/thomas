package com.thomas.jwt.auth0

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.Claim
import com.thomas.core.HttpApplicationException.Companion.unauthorized
import com.thomas.core.model.security.SecurityUser
import com.thomas.jwt.JWTAuthenticator
import com.thomas.jwt.auth0.data.repository.SecurityUserMongoRepository
import com.thomas.jwt.auth0.extension.privateKeySpec
import com.thomas.jwt.auth0.extension.publicKeySpec
import com.thomas.jwt.auth0.i18n.Auth0JWTAuthenticationMessageI18N.authenticationJWTAuth0TokenExpiredToken
import com.thomas.jwt.auth0.i18n.Auth0JWTAuthenticationMessageI18N.authenticationJWTAuth0TokenNotFound
import com.thomas.jwt.configuration.JWTConfiguration
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.UUID
import org.apache.logging.log4j.kotlin.Logging

class Auth0JWTAuthenticator(
    configuration: JWTConfiguration,
    private val repository: SecurityUserMongoRepository,
) : JWTAuthenticator<RSAPrivateKey, RSAPublicKey, PKCS8EncodedKeySpec, X509EncodedKeySpec>(
    configuration
), Logging {

    private val algorithm: Algorithm = Algorithm.RSA256(publicKey, privateKey)
    private val verifier: JWTVerifier = JWT.require(algorithm)
        .withIssuer(configuration.issuerName).build()

    override fun privateKeySpec() = configuration.privateKeySpec()

    override fun publicKeySpec() = configuration.publicKeySpec()

    override fun verifyToken(
        token: String
    ): SecurityUser = try {
        verifier.verify(token).claims.toSecurityUser()
    } catch (e: TokenExpiredException) {
        throw unauthorized(authenticationJWTAuth0TokenExpiredToken(), cause = e)
    }

    private fun Map<String, Claim>.toSecurityUser(): SecurityUser =
        this.stringOrNull(SecurityUser::userId.name)?.let {
            repository.findSecurityUser(UUID.fromString(it))
        } ?: throw unauthorized(authenticationJWTAuth0TokenNotFound())

    private fun Map<String, Claim>.stringOrNull(
        field: String
    ): String? = this[field]?.asString()

}