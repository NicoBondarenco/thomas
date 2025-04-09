package com.thomas.spring.base.extension

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.thomas.spring.base.properties.JWTProperties
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

private fun JWTProperties.publicKeySpec() = X509EncodedKeySpec(
    Base64.getDecoder().decode(this.publicKey)
)

private fun JWTProperties.privateKeySpec() = PKCS8EncodedKeySpec(
    Base64.getDecoder().decode(this.privateKey)
)

private fun JWTProperties.rsaPublicKey(): RSAPublicKey =
    KeyFactory.getInstance(jwtAlgorithm).generatePublic(publicKeySpec()) as RSAPublicKey

private fun JWTProperties.rsaPrivateKey(): RSAPrivateKey =
    KeyFactory.getInstance(jwtAlgorithm).generatePrivate(privateKeySpec()) as RSAPrivateKey

internal fun JWTProperties.algorithm() = Algorithm.RSA256(rsaPublicKey(), rsaPrivateKey())

internal fun JWTProperties.verifier(): JWTVerifier = JWT.require(algorithm()).withIssuer(issuerName).build()
