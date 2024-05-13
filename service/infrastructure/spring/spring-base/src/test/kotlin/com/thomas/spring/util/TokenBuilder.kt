package com.thomas.spring.util

import com.auth0.jwt.JWT
import com.thomas.core.model.security.SecurityUser
import com.thomas.spring.properties.SecurityProperties
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit.MINUTES
import org.springframework.stereotype.Component


@Component
class TokenBuilder(
    private val securityProperties: SecurityProperties
) {

    private val formatter = DateTimeFormatter.ISO_DATE

    @Suppress("LongParameterList")
    internal fun generateToken(
        user: SecurityUser,
        validityMinutes: Long = 5,
        idProperty: String = SecurityUser::userId.name,
        idValue: String = user.userId.toString(),
        issuerName: String = securityProperties.jwt.issuerName,
        audienceName: String = securityProperties.jwt.jwtAudience
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
        .withClaim(SecurityUser::userProfile.name, user.userProfile.name)
        .sign(securityProperties.jwt.algorithm())
}


