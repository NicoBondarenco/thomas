package com.thomas.authentication.domain.adapter

import com.thomas.authentication.data.entity.RefreshTokenCompleteEntity
import com.thomas.authentication.data.entity.RefreshTokenEntity
import com.thomas.authentication.data.entity.UserAuthenticationBaseEntity
import com.thomas.authentication.data.entity.UserAuthenticationCompleteEntity
import com.thomas.authentication.data.repository.RefreshTokenRepository
import com.thomas.authentication.data.repository.UserAuthenticationRepository
import com.thomas.authentication.domain.AuthenticationService
import com.thomas.authentication.domain.exception.InvalidCredentialsException
import com.thomas.authentication.domain.model.extension.toSecurityUser
import com.thomas.authentication.domain.model.request.LoginRequest
import com.thomas.authentication.domain.model.request.RefreshTokenRequest
import com.thomas.authentication.domain.model.response.AccessTokenResponse
import com.thomas.authentication.domain.properties.AuthenticationProperties
import com.thomas.authentication.tokenizer.Tokenizer
import com.thomas.hash.Hasher
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID.randomUUID

class AuthenticationServiceAdapter(
    private val hasher: Hasher,
    private val tokenizer: Tokenizer,
    private val properties: AuthenticationProperties,
    private val userRepository: UserAuthenticationRepository,
    private val refreshRepository: RefreshTokenRepository,
) : AuthenticationService {

    override fun login(
        request: LoginRequest,
    ): AccessTokenResponse = userRepository.findByUsername(request.username)?.takeIf { user ->
        user.validPassword(request.password) && user.isActive
    }?.toAccessTokenResponse() ?: throw InvalidCredentialsException()

    override fun refresh(
        request: RefreshTokenRequest
    ): AccessTokenResponse = refreshRepository.findByToken(request.refreshToken)?.takeIf {
        it.validUntil.isBefore(now(UTC)) && it.userAuthentication.isActive
    }?.toAccessTokenResponse() ?: throw InvalidCredentialsException()

    private fun UserAuthenticationBaseEntity.validPassword(
        password: String
    ): Boolean = this.passwordHash == hasher.hash(password, this.passwordSalt)

    private fun RefreshTokenCompleteEntity.toAccessTokenResponse() = this.userAuthentication.toAccessTokenResponse(
        refreshToken = this.refreshToken,
    )

    private fun UserAuthenticationCompleteEntity.toAccessTokenResponse(
        refreshToken: String? = null
    ) = AccessTokenResponse(
        idToken = tokenizer.accessToken(this.toSecurityUser(), properties.accessDurationSeconds),
        refreshToken = refreshToken ?: this.generateRefreshToken(),
        durationSeconds = properties.accessDurationSeconds,
    )

    private fun UserAuthenticationBaseEntity.generateRefreshToken(): String = refreshRepository.upsert(
        RefreshTokenEntity(
            id = this.id,
            refreshToken = hasher.hash(randomUUID().toString(), this.passwordSalt),
            validUntil = now(UTC).plusSeconds(properties.refreshDurationSeconds),
        )
    ).refreshToken

}
