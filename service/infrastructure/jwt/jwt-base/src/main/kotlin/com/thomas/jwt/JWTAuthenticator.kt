package com.thomas.jwt

import com.thomas.core.HttpApplicationException
import com.thomas.core.model.http.HTTPStatus.UNAUTHORIZED
import com.thomas.core.model.security.SecurityUser
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
            ?: throw HttpApplicationException(UNAUTHORIZED, authenticationJWTBaseUserInactiveUser())
    } catch (e: HttpApplicationException) {
        throw e
    } catch (e: Exception) {
        throw HttpApplicationException(UNAUTHORIZED, authenticationJWTBaseTokenInvalidToken(), cause = e)
    }

    abstract fun verifyToken(token: String): SecurityUser

    protected abstract fun privateKeySpec(): KS

    protected abstract fun publicKeySpec(): PS

}
