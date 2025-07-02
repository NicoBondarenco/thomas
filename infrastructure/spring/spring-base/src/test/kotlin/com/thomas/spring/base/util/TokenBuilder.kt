package com.thomas.spring.base.util

import com.auth0.jwt.JWT
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.security.SecurityGroup
import com.thomas.core.model.security.SecurityOrganization
import com.thomas.core.model.security.SecurityUnit
import com.thomas.core.model.security.SecurityUser
import com.thomas.spring.base.extension.algorithm
import com.thomas.spring.base.properties.JWTProperties
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit.MINUTES
import org.springframework.stereotype.Component

@Component
class TokenBuilder(
    private val jwtProperties: JWTProperties
) {

    private val formatter = DateTimeFormatter.ISO_DATE
    private val algorithm = jwtProperties.algorithm()

    @Suppress("LongParameterList")
    internal fun generateToken(
        user: SecurityUser,
        validityMinutes: Long = 5,
        idProperty: String = SecurityUser::userId.name,
        idValue: String = user.userId.toString(),
        issuerName: String = jwtProperties.issuerName,
        audienceName: String = jwtProperties.jwtAudience
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
        .withClaim(idProperty.toSnakeCase(), idValue)
        .withClaim(SecurityUser::firstName.name.toSnakeCase(), user.firstName)
        .withClaim(SecurityUser::lastName.name.toSnakeCase(), user.lastName)
        .withClaim(SecurityUser::mainEmail.name.toSnakeCase(), user.mainEmail)
        .withClaim(SecurityUser::phoneNumber.name.toSnakeCase(), user.phoneNumber)
        .withClaim(SecurityUser::profilePhoto.name.toSnakeCase(), user.profilePhoto)
        .withClaim(SecurityUser::birthDate.name.toSnakeCase(), user.birthDate?.let { formatter.format(it) })
        .withClaim(SecurityUser::userGender.name.toSnakeCase(), user.userGender?.name)
        .withClaim(SecurityUser::userRace.name.toSnakeCase(), user.userRace?.name)
        .withClaim(SecurityUser::userType.name.toSnakeCase(), user.userType.name)
        .withClaim(SecurityUser::isActive.name.toSnakeCase(), user.isActive)
        .withClaim(SecurityUser::securityOrganization.name.toSnakeCase(), user.securityOrganization.toClaim())
        .withClaim(SecurityUser::userGroups.name.toSnakeCase(), user.userGroups.toGroupClaims())
        .withClaim(SecurityUser::securityUnits.name.toSnakeCase(), user.securityUnits.toUnitClaims())
        .sign(algorithm)

    private fun SecurityOrganization.toClaim() = mapOf(
        SecurityOrganization::organizationId.name.toSnakeCase() to this.organizationId.toString(),
        SecurityOrganization::organizationName.name.toSnakeCase() to this.organizationName,
        SecurityOrganization::organizationRoles.name.toSnakeCase() to this.organizationRoles.map { it.name },
    )

    private fun SecurityUnit.toClaim() = mapOf(
        SecurityUnit::unitId.name.toSnakeCase() to this.unitId.toString(),
        SecurityUnit::unitName.name.toSnakeCase() to this.unitName,
        SecurityUnit::unitRoles.name.toSnakeCase() to this.unitRoles.map { it.name },
    )

    private fun Set<SecurityUnit>.toUnitClaims() = this.map { it.toClaim() }

    private fun SecurityGroup.toClaim() = mapOf(
        SecurityGroup::groupId.name.toSnakeCase() to this.groupId.toString(),
        SecurityGroup::groupName.name.toSnakeCase() to this.groupName,
        SecurityGroup::securityOrganization.name.toSnakeCase() to this.securityOrganization.toClaim(),
        SecurityGroup::securityUnits.name.toSnakeCase() to this.securityUnits.toUnitClaims(),
    )

    private fun Set<SecurityGroup>.toGroupClaims() = this.map { it.toClaim() }

}
