package com.thomas.authentication.tokenizer.auth0.extension

import com.auth0.jwt.algorithms.Algorithm
import com.thomas.authentication.tokenizer.auth0.properties.Auth0TokenizerProperties
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.Base64
import java.util.concurrent.TimeUnit.SECONDS

private fun Auth0TokenizerProperties.publicKeySpec() = X509EncodedKeySpec(
    Base64.getDecoder().decode(this.publicKey)
)

private fun Auth0TokenizerProperties.privateKeySpec() = PKCS8EncodedKeySpec(
    Base64.getDecoder().decode(this.privateKey)
)

private fun Auth0TokenizerProperties.rsaPublicKey(): RSAPublicKey =
    KeyFactory.getInstance(jwtAlgorithm).generatePublic(publicKeySpec()) as RSAPublicKey

private fun Auth0TokenizerProperties.rsaPrivateKey(): RSAPrivateKey =
    KeyFactory.getInstance(jwtAlgorithm).generatePrivate(privateKeySpec()) as RSAPrivateKey

internal fun Auth0TokenizerProperties.algorithm() = Algorithm.RSA256(rsaPublicKey(), rsaPrivateKey())

internal fun Long.validityInstant() = now(UTC)
    .plusNanos(SECONDS.toNanos(this)).toInstant()