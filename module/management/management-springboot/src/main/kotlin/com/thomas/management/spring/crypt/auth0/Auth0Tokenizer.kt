package com.thomas.management.spring.crypt.auth0

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.IncorrectClaimException
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.security.SecurityGroup
import com.thomas.core.model.security.SecurityOrganization
import com.thomas.core.model.security.SecurityUnit
import com.thomas.core.model.security.SecurityUser
import com.thomas.management.domain.crypt.Tokenizer
import com.thomas.management.domain.model.data.RefreshTokenData
import com.thomas.management.domain.model.request.RefreshTokenRequest
import com.thomas.management.spring.configuration.properties.TokenProperties
import com.thomas.management.spring.i18n.ManagementSpringMessageI18N.refreshTokenValidateTokenDecodeError
import com.thomas.management.spring.i18n.ManagementSpringMessageI18N.refreshTokenValidateTokenExpiredToken
import com.thomas.management.spring.i18n.ManagementSpringMessageI18N.refreshTokenValidateTokenInvalidIssuer
import com.thomas.management.spring.i18n.ManagementSpringMessageI18N.refreshTokenValidateTokenInvalidSignature
import com.thomas.spring.base.exception.JWTTokenException
import com.thomas.spring.base.extension.algorithm
import com.thomas.spring.base.extension.verifier
import com.thomas.spring.base.properties.JWTProperties
import java.nio.charset.StandardCharsets.UTF_8
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter
import java.util.Base64

class Auth0Tokenizer(
    private val jwtProperties: JWTProperties,
    private val tokenProperties: TokenProperties,
    private val objectMapper: ObjectMapper,
) : Tokenizer {

    companion object {
        private val ISO_LOCAL_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE
        private val ISO_OFFSET_DATETIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    }

    private val algorithm: Algorithm = jwtProperties.algorithm()
    private val verifier: JWTVerifier = jwtProperties.verifier()

    override suspend fun generateAccessToken(
        securityUser: SecurityUser,
    ): String = JWT.create()
        .withBaseData(securityUser.userId.toString(), tokenProperties.accessDuration)
        .tokenClaims(securityUser)
        .sign()

    override suspend fun generateRefreshToken(
        securityUser: SecurityUser
    ): String = JWT.create()
        .withBaseData(securityUser.userId.toString(), tokenProperties.refreshDuration)
        .refreshClaims(
            RefreshTokenData(
                securityUsername = securityUser.mainEmail,
                organizationId = securityUser.securityOrganization.organizationId,
                refreshDuration = tokenProperties.refreshDuration,
                validUntil = now(UTC).plusMinutes(tokenProperties.refreshDuration),
            )
        ).sign()

    override suspend fun accessTokenDuration(): Long = tokenProperties.accessDuration

    override suspend fun refreshTokenDuration(): Long = tokenProperties.refreshDuration

    override suspend fun refreshTokenData(
        refreshToken: RefreshTokenRequest
    ): RefreshTokenData = try {
        verifier.verify(refreshToken.refreshToken).payload.let {
            val decoded = Base64.getDecoder().decode(it)
            val content = String(decoded, UTF_8)
            objectMapper.readValue<RefreshTokenData>(content)
        }
    } catch (e: TokenExpiredException) {
        throw JWTTokenException(refreshTokenValidateTokenExpiredToken(), e)
    } catch (e: JWTDecodeException) {
        throw JWTTokenException(refreshTokenValidateTokenDecodeError(), e)
    } catch (e: SignatureVerificationException) {
        throw JWTTokenException(refreshTokenValidateTokenInvalidSignature(), e)
    } catch (e: IncorrectClaimException) {
        throw JWTTokenException(refreshTokenValidateTokenInvalidIssuer(), e)
    }

    private fun JWTCreator.Builder.withBaseData(
        subject: String,
        duration: Long,
    ) = this.withHeader(
        mutableMapOf<String, Any>(
            "alg" to jwtProperties.jwtAlgorithm,
        )
    ).withIssuer(jwtProperties.issuerName)
        .withSubject(subject)
        .withExpiresAt(now(UTC).plusMinutes(duration).toInstant())
        .withIssuedAt(now(UTC).toInstant())
        .withAudience(jwtProperties.jwtAudience)

    private fun JWTCreator.Builder.tokenClaims(
        securityUser: SecurityUser,
    ) = this.withClaim(SecurityUser::userId.name.toSnakeCase(), securityUser.userId.toString())
        .withClaim(SecurityUser::firstName.name.toSnakeCase(), securityUser.firstName)
        .withClaim(SecurityUser::lastName.name.toSnakeCase(), securityUser.lastName)
        .withClaim(SecurityUser::mainEmail.name.toSnakeCase(), securityUser.mainEmail)
        .withClaim(SecurityUser::phoneNumber.name.toSnakeCase(), securityUser.phoneNumber)
        .withClaim(SecurityUser::profilePhoto.name.toSnakeCase(), securityUser.profilePhoto)
        .withClaim(SecurityUser::birthDate.name.toSnakeCase(), securityUser.birthDate?.let { ISO_LOCAL_DATE_FORMATTER.format(it) })
        .withClaim(SecurityUser::userGender.name.toSnakeCase(), securityUser.userGender?.name)
        .withClaim(SecurityUser::userRace.name.toSnakeCase(), securityUser.userRace?.name)
        .withClaim(SecurityUser::userType.name.toSnakeCase(), securityUser.userType.name)
        .withClaim(SecurityUser::isActive.name.toSnakeCase(), securityUser.isActive)
        .withClaim(SecurityUser::securityOrganization.name.toSnakeCase(), securityUser.securityOrganization.toClaim())
        .withClaim(SecurityUser::userGroups.name.toSnakeCase(), securityUser.userGroups.toGroupClaims())
        .withClaim(SecurityUser::securityUnits.name.toSnakeCase(), securityUser.securityUnits.toUnitClaims())

    private fun JWTCreator.Builder.refreshClaims(
        refreshData: RefreshTokenData
    ) = this.withClaim(RefreshTokenData::securityUsername.name.toSnakeCase(), refreshData.securityUsername)
        .withClaim(RefreshTokenData::organizationId.name.toSnakeCase(), refreshData.organizationId.toString())
        .withClaim(RefreshTokenData::refreshDuration.name.toSnakeCase(), refreshData.refreshDuration)
        .withClaim(RefreshTokenData::validUntil.name.toSnakeCase(), ISO_OFFSET_DATETIME_FORMATTER.format(refreshData.validUntil))


    private fun JWTCreator.Builder.sign() = this.sign(algorithm)

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
