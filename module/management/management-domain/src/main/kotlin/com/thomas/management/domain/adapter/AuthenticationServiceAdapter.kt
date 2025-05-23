package com.thomas.management.domain.adapter

import com.thomas.core.aspect.AspectClass
import com.thomas.core.model.security.SecurityOrganizationRole.MASTER_ROLE
import com.thomas.core.model.security.SecurityOrganizationRole.ORGANIZATION_ALL
import com.thomas.core.model.security.SecurityUnitRole.UNIT_ALL
import com.thomas.management.data.entity.GroupCompleteEntity
import com.thomas.management.data.entity.UnitRoleEntity
import com.thomas.management.data.entity.UserCompleteEntity
import com.thomas.management.data.repository.UnitRepository
import com.thomas.management.data.repository.UserRepository
import com.thomas.management.domain.AuthenticationService
import com.thomas.management.domain.crypt.Hasher
import com.thomas.management.domain.crypt.Tokenizer
import com.thomas.management.domain.exception.InactiveOrganizationException
import com.thomas.management.domain.exception.InactiveUserException
import com.thomas.management.domain.exception.InvalidCredentialException
import com.thomas.management.domain.exception.InvalidRefreshTokenException
import com.thomas.management.domain.model.data.RefreshTokenData
import com.thomas.management.domain.model.mapper.toSecurityUser
import com.thomas.management.domain.model.request.LoginRequest
import com.thomas.management.domain.model.request.RefreshTokenRequest
import com.thomas.management.domain.model.response.AccessTokenResponse
import kotlinx.coroutines.coroutineScope

@AspectClass
class AuthenticationServiceAdapter(
    private val hasher: Hasher,
    private val tokenizer: Tokenizer,
    private val userRepository: UserRepository,
    private val unitRepository: UnitRepository,
) : AuthenticationService {

    override suspend fun login(
        request: LoginRequest,
    ): AccessTokenResponse = userRepository.findByUsername(request.username)
        ?.isValidUser(request.password)
        ?.loadActiveUnits()
        ?.toAccessTokenResponse()
        ?: throw InvalidCredentialException()

    override suspend fun refresh(
        request: RefreshTokenRequest,
    ): AccessTokenResponse = request.let {
        tokenizer.refreshTokenData(it)
    }.toUserCompleteEntity()
        ?.isValidUser()
        ?.loadActiveUnits()
        ?.toAccessTokenResponse()
        ?: throw InvalidRefreshTokenException()

    private suspend fun RefreshTokenData.toUserCompleteEntity() = userRepository.findByUsername(this.securityUsername)

    private suspend fun UserCompleteEntity.toAccessTokenResponse() = coroutineScope {
        val user = this@toAccessTokenResponse.toSecurityUser()
        AccessTokenResponse(
            accessToken = tokenizer.generateAccessToken(user),
            refreshToken = tokenizer.generateRefreshToken(user),
            accessDuration = tokenizer.accessTokenDuration(),
            refreshDuration = tokenizer.refreshTokenDuration(),
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

    private suspend fun UserCompleteEntity.loadActiveUnits(): UserCompleteEntity = this.takeIf {
        shouldLoadAllUnits()
    }?.let { user ->
        user.copy(
            userUnits = unitRepository.allByOrganization(user.userOrganization.id).filter {
                it.isActive
            }.map {
                UnitRoleEntity(roleUnit = it, roleList = setOf(UNIT_ALL))
            }.toSet()
        )
    } ?: this

    private fun UserCompleteEntity.shouldLoadAllUnits(): Boolean = this.organizationRoles.any {
        it == MASTER_ROLE || it == ORGANIZATION_ALL
    } || this.userGroups.shouldLoadAllUnits()

    private fun Set<GroupCompleteEntity>.shouldLoadAllUnits(): Boolean = this.any {
        it.shouldLoadAllUnits()
    }

    private fun GroupCompleteEntity.shouldLoadAllUnits(): Boolean = this.organizationRoles.any {
        it == MASTER_ROLE || it == ORGANIZATION_ALL
    }

}
