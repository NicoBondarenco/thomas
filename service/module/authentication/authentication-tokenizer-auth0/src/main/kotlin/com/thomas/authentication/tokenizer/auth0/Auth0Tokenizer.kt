package com.thomas.authentication.tokenizer.auth0

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.thomas.authentication.tokenizer.Tokenizer
import com.thomas.authentication.tokenizer.auth0.extension.algorithm
import com.thomas.authentication.tokenizer.auth0.extension.validityInstant
import com.thomas.authentication.tokenizer.auth0.properties.Auth0TokenizerProperties
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.security.SecurityGroup
import com.thomas.core.model.security.SecurityUser
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter.ISO_DATE

class Auth0Tokenizer(
    private val properties: Auth0TokenizerProperties
) : Tokenizer {

    companion object {

        private val JWT_HEADERS = mapOf<String, Any>(
            "alg" to "RSA256",
            "typ" to "JWT",
        )

        private val FORMATTER = ISO_DATE

    }

    override fun accessToken(
        securityUser: SecurityUser,
        durationSeconds: Long,
    ): String = JWT.create()
        .withExpiresAt(durationSeconds.validityInstant())
        .withProperties(properties)
        .withClaims(securityUser)
        .sign(properties.algorithm())

    private fun JWTCreator.Builder.withProperties(
        properties: Auth0TokenizerProperties
    ) = this.withHeader(JWT_HEADERS)
        .withIssuer(properties.issuerName)
        .withIssuedAt(now(UTC).toInstant())
        .withAudience(properties.jwtAudience)

    private fun JWTCreator.Builder.withClaims(
        user: SecurityUser
    ) = this.withSubject(user.userId.toString())
        .withClaim(SecurityUser::userId.name.toSnakeCase(), user.userId.toString())
        .withClaim(SecurityUser::firstName.name.toSnakeCase(), user.firstName)
        .withClaim(SecurityUser::lastName.name.toSnakeCase(), user.lastName)
        .withClaim(SecurityUser::mainEmail.name.toSnakeCase(), user.mainEmail)
        .withClaim(SecurityUser::phoneNumber.name.toSnakeCase(), user.phoneNumber)
        .withClaim(SecurityUser::profilePhoto.name.toSnakeCase(), user.profilePhoto)
        .withClaim(SecurityUser::birthDate.name.toSnakeCase(), user.birthDate?.let { FORMATTER.format(it) })
        .withClaim(SecurityUser::userGender.name.toSnakeCase(), user.userGender?.name)
        .withClaim(SecurityUser::userRoles.name.toSnakeCase(), user.userRoles.map { it.name })
        .withClaim(SecurityUser::userGroups.name.toSnakeCase(), user.groupsClaim())

    private fun SecurityUser.groupsClaim() = this.userGroups.map {
        mapOf(
            SecurityGroup::groupId.name.toSnakeCase() to it.groupId.toString(),
            SecurityGroup::groupName.name.toSnakeCase() to it.groupName,
            SecurityGroup::groupRoles.name.toSnakeCase() to it.groupRoles.map { role -> role.name }
        )
    }

}
