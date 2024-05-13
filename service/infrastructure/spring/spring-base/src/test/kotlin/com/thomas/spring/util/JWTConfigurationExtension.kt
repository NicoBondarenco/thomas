package com.thomas.spring.util

import com.auth0.jwt.algorithms.Algorithm
import com.thomas.authentication.jwt.auth0.properties.JWTAuth0Properties
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

private fun JWTAuth0Properties.publicKeySpec() = X509EncodedKeySpec(
    Base64.getDecoder().decode(this.publicKey)
)

private fun JWTAuth0Properties.privateKeySpec() = PKCS8EncodedKeySpec(
    Base64.getDecoder().decode(this.privateKey)
)

private fun JWTAuth0Properties.rsaPublicKey(): RSAPublicKey =
    KeyFactory.getInstance(jwtAlgorithm).generatePublic(publicKeySpec()) as RSAPublicKey

private fun JWTAuth0Properties.rsaPrivateKey(): RSAPrivateKey =
    KeyFactory.getInstance(jwtAlgorithm).generatePrivate(privateKeySpec()) as RSAPrivateKey

internal fun JWTAuth0Properties.algorithm() = Algorithm.RSA256(rsaPublicKey(), rsaPrivateKey())
