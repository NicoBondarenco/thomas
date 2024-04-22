package com.thomas.jwt.auth0.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.thomas.core.model.security.SecurityUser
import com.thomas.jwt.auth0.extension.privateKeySpec
import com.thomas.jwt.auth0.extension.publicKeySpec
import com.thomas.jwt.configuration.JWTConfiguration
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit.MINUTES

private val formatter = DateTimeFormatter.ISO_DATE

private fun JWTConfiguration.algorithm() = this.encryptionType.keyFactory().let {
    Algorithm.RSA256(
        it.generatePublic(this.publicKeySpec()) as RSAPublicKey,
        it.generatePrivate(this.privateKeySpec()) as RSAPrivateKey,
    )
}

fun JWTConfiguration.generateToken(
    user: SecurityUser,
    validityMinutes: Long = 5,
    idProperty: String = SecurityUser::userId.name,
): String = JWT.create()
    .withHeader(
        mutableMapOf<String, Any>(
            "alg" to this.algorithmType.name
        )
    )
    .withIssuer(this.issuerName)
    .withSubject(user.userId.toString())
    .withExpiresAt(now(UTC).plusNanos(MINUTES.toNanos(validityMinutes)).toInstant())
    .withIssuedAt(now(UTC).toInstant())
    .withAudience(this.jwtAudience)
    .withClaim(idProperty, user.userId.toString())
    .withClaim(SecurityUser::firstName.name, user.firstName)
    .withClaim(SecurityUser::lastName.name, user.lastName)
    .withClaim(SecurityUser::mainEmail.name, user.mainEmail)
    .withClaim(SecurityUser::phoneNumber.name, user.phoneNumber)
    .withClaim(SecurityUser::profilePhoto.name, user.profilePhoto)
    .withClaim(SecurityUser::birthDate.name, user.birthDate?.let { formatter.format(it) })
    .withClaim(SecurityUser::userGender.name, user.userGender?.name)
    .withClaim(SecurityUser::userProfile.name, user.userProfile.name)
    .sign(this.algorithm())
