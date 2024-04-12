package com.thomas.jwt

import com.thomas.core.HttpApplicationException.Companion.unauthorized
import com.thomas.core.security.SecurityUser
import com.thomas.jwt.configuration.JWTConfiguration
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

class TestAuthenticator(
    configuration: JWTConfiguration
) : JWTAuthenticator<RSAPrivateKey, RSAPublicKey, PKCS8EncodedKeySpec, X509EncodedKeySpec>(configuration) {

    private val tokens = mutableMapOf<String, SecurityUser>()
    private val invalidTokens: List<String> = configuration.customProperties["invalidTokens"]!! as List<String>

    fun addToken(pair: Pair<String, SecurityUser>) {
        tokens[pair.first] = pair.second
    }

    val privateKeyFormat = privateKey.format
    val publicKeyFormat = publicKey.format

    override fun verifyToken(
        token: String
    ): SecurityUser = if (invalidTokens.contains(token)) {
        throw Exception("Token in invalid list")
    } else {
        tokens[token] ?: throw unauthorized("User unauthorized with token $token")
    }

    override fun privateKeySpec(): PKCS8EncodedKeySpec {
        var content = this.javaClass.classLoader.getResource(configuration.privateKey).readText()
        content = content.extractKey()
        return PKCS8EncodedKeySpec(Base64.getDecoder().decode(content))
    }

    override fun publicKeySpec(): X509EncodedKeySpec {
        var content = this.javaClass.classLoader.getResource(configuration.publicKey).readText()
        content = content.extractKey()
        return X509EncodedKeySpec(Base64.getDecoder().decode(content))
    }

}