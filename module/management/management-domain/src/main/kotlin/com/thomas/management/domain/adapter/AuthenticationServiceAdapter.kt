package com.thomas.management.domain.adapter

import com.thomas.hasher.Hasher
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.data.repository.UserRepository
import com.thomas.management.domain.AuthenticationService
import com.thomas.management.domain.exception.InactiveOrganizationException
import com.thomas.management.domain.exception.InactiveUserException
import com.thomas.management.domain.exception.InvalidCredentialException
import com.thomas.management.domain.exception.InvalidRefreshTokenException
import com.thomas.management.domain.model.data.RefreshTokenData
import com.thomas.management.domain.model.mapper.toSecurityUser
import com.thomas.management.domain.model.request.LoginRequest
import com.thomas.management.domain.model.request.RefreshTokenRequest
import com.thomas.management.domain.model.response.AccessTokenResponse
import com.thomas.management.domain.properties.AuthenticationProperties
import com.thomas.management.domain.token.Tokenizer
import kotlinx.coroutines.coroutineScope

class AuthenticationServiceAdapter(
    private val hasher: Hasher,
    private val tokenizer: Tokenizer,
    private val userRepository: UserRepository,
    private val authenticationProperties: AuthenticationProperties,
) : AuthenticationService {

    override suspend fun login(
        request: LoginRequest,
    ): AccessTokenResponse = userRepository.findByUsername(request.username)
        ?.isValidUser(request.password)
        ?.toAccessTokenResponse()
        ?: throw InvalidCredentialException()

    override suspend fun refresh(
        request: RefreshTokenRequest,
    ): AccessTokenResponse = request.let {
        tokenizer.refreshTokenData(it)
    }.toUserCompleteEntity()
        ?.isValidUser()
        ?.toAccessTokenResponse()
        ?: throw InvalidRefreshTokenException()

    private suspend fun RefreshTokenData.toUserCompleteEntity() = userRepository.findByUsername(this.username)

    private suspend fun UserCompleteEntity.toAccessTokenResponse() = coroutineScope {
        val user = this@toAccessTokenResponse.toSecurityUser()
        AccessTokenResponse(
            idToken = tokenizer.generateAccessToken(user, authenticationProperties.accessDurationSeconds),
            refreshToken = tokenizer.generateRefreshToken(user, authenticationProperties.refreshDurationSeconds),
            durationSeconds = authenticationProperties.refreshDurationSeconds,
        )
    }

    private suspend fun UserCompleteEntity.isValidUser(
        password: String
    ): UserCompleteEntity = this.isValidPassword(password).isValidUser()

    private fun UserCompleteEntity.isValidUser(): UserCompleteEntity =
        this.isUserActive().isOrganizationActive()

    private suspend fun UserCompleteEntity.isValidPassword(
        password: String
    ): UserCompleteEntity = this.apply {
        (this.passwordHash == hasher.hash(password, this.passwordSalt)).takeIf { !it }?.let {
            throw InvalidCredentialException()
        }
    }

    private fun UserCompleteEntity.isUserActive(): UserCompleteEntity = this.apply {
        this.isActive.takeIf { !it }?.let {
            throw InactiveUserException()
        }
    }

    private fun UserCompleteEntity.isOrganizationActive(): UserCompleteEntity = this.apply {
        this.userOrganization.isActive.takeIf { !it }?.let {
            throw InactiveOrganizationException()
        }
    }

}
