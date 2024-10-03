package com.thomas.authentication.jwt.auth0.util

import com.auth0.jwt.JWT
import com.thomas.authentication.jwt.auth0.extension.algorithm
import com.thomas.core.model.security.SecurityUser
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit.MINUTES

private val formatter = DateTimeFormatter.ISO_DATE

private val algorithm = defaultConfiguration.algorithm()

@Suppress("LongParameterList")
internal fun generateToken(
    user: SecurityUser,
    validityMinutes: Long = 5,
    idProperty: String = SecurityUser::userId.name,
    idValue: String = user.userId.toString(),
    issuerName: String = defaultConfiguration.issuerName,
    audienceName: String = defaultConfiguration.jwtAudience
): String = JWT.create()
    .withHeader(
        mutableMapOf<String, Any>(
            "alg" to "RSA"
        )
    )
    .withIssuer(issuerName)
    .withSubject(user.userId.toString())
    .withExpiresAt(now(UTC).plusNanos(MINUTES.toNanos(validityMinutes)).toInstant())
    .withIssuedAt(now(UTC).toInstant())
    .withAudience(audienceName)
    .withClaim(idProperty, idValue)
    .withClaim(SecurityUser::firstName.name, user.firstName)
    .withClaim(SecurityUser::lastName.name, user.lastName)
    .withClaim(SecurityUser::mainEmail.name, user.mainEmail)
    .withClaim(SecurityUser::phoneNumber.name, user.phoneNumber)
    .withClaim(SecurityUser::profilePhoto.name, user.profilePhoto)
    .withClaim(SecurityUser::birthDate.name, user.birthDate?.let { formatter.format(it) })
    .withClaim(SecurityUser::userGender.name, user.userGender?.name)
    .sign(algorithm)
