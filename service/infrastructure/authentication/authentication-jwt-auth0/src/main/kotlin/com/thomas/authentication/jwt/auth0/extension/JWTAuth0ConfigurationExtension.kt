package com.thomas.authentication.jwt.auth0.extension

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.thomas.authentication.jwt.auth0.configuration.JWTAuth0Configuration
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

private fun JWTAuth0Configuration.publicKeySpec() = X509EncodedKeySpec(
    Base64.getDecoder().decode(this.publicKey)
)

private fun JWTAuth0Configuration.privateKeySpec() = PKCS8EncodedKeySpec(
    Base64.getDecoder().decode(this.privateKey)
)

private fun JWTAuth0Configuration.rsaPublicKey(): RSAPublicKey =
    KeyFactory.getInstance(jwtAlgorithm).generatePublic(publicKeySpec()) as RSAPublicKey

private fun JWTAuth0Configuration.rsaPrivateKey(): RSAPrivateKey =
    KeyFactory.getInstance(jwtAlgorithm).generatePrivate(privateKeySpec()) as RSAPrivateKey

internal fun JWTAuth0Configuration.algorithm() = Algorithm.RSA256(rsaPublicKey(), rsaPrivateKey())

internal fun JWTAuth0Configuration.verifier(): JWTVerifier = JWT.require(algorithm()).withIssuer(issuerName).build()
