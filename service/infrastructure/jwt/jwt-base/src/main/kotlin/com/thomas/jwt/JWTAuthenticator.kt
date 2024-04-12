package com.thomas.jwt

import com.thomas.core.HttpApplicationException
import com.thomas.core.HttpApplicationException.Companion.unauthorized
import com.thomas.core.security.SecurityUser
import com.thomas.jwt.configuration.JWTConfiguration
import com.thomas.jwt.i18n.JWTAuthenticationMessageI18N.authenticationJWTBaseTokenInvalidToken
import com.thomas.jwt.i18n.JWTAuthenticationMessageI18N.authenticationJWTBaseUserInactiveUser
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.EncodedKeySpec

abstract class JWTAuthenticator<K : PrivateKey, P : PublicKey, KS : EncodedKeySpec, PS : EncodedKeySpec>(
    protected val configuration: JWTConfiguration
) {

    protected val privateKey: K
    protected val publicKey: P

    init {
        val keyFactory: KeyFactory = configuration.encryptionType.keyFactory()
        privateKey = keyFactory.generatePrivate(this.privateKeySpec()) as K
        publicKey = keyFactory.generatePublic(this.publicKeySpec()) as P
    }

    fun authenticate(
        token: String
    ): SecurityUser = try {
        verifyToken(token).takeIf { it.isActive }
            ?: throw unauthorized(authenticationJWTBaseUserInactiveUser())
    } catch (e: HttpApplicationException) {
        throw e
    } catch (e: Exception) {
        throw unauthorized(authenticationJWTBaseTokenInvalidToken(), cause = e)
    }

    abstract fun verifyToken(token: String): SecurityUser

    protected abstract fun privateKeySpec(): KS

    protected abstract fun publicKeySpec(): PS

    protected fun String.extractKey() = this
        .replace(Char(65533).toString(), "")
        .replace(Char(0).toString(), "")
        .replace("\r\n".toRegex(), "")
        .replace("\n".toRegex(), "")
        .replace("\r".toRegex(), "")
        .replace("-----BEGIN PRIVATE KEY-----", "")
        .replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PRIVATE KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "")

}