package com.thomas.authentication.domain.adapter

import com.thomas.authentication.data.entity.RefreshTokenCompleteEntity
import com.thomas.authentication.data.entity.RefreshTokenEntity
import com.thomas.authentication.data.entity.UserAuthenticationCompleteEntity
import com.thomas.authentication.data.repository.RefreshTokenRepository
import com.thomas.authentication.data.repository.UserAuthenticationRepository
import com.thomas.authentication.domain.exception.InvalidCredentialsException
import com.thomas.authentication.domain.exception.InvalidRefreshTokenException
import com.thomas.authentication.domain.i18n.AuthenticationDomainMessageI18N.authenticationUserAuthenticationInvalidCredentialsRefreshToken
import com.thomas.authentication.domain.i18n.AuthenticationDomainMessageI18N.authenticationUserAuthenticationInvalidCredentialsUsernamePassword
import com.thomas.authentication.domain.model.request.RefreshTokenRequest
import com.thomas.authentication.domain.properties.AuthenticationProperties
import com.thomas.authentication.event.loginRequest
import com.thomas.authentication.event.refreshTokenCompleteEntity
import com.thomas.authentication.event.userCompleteAuthentication
import com.thomas.authentication.tokenizer.Tokenizer
import com.thomas.core.model.security.SecurityUser
import com.thomas.core.random.randomString
import com.thomas.hash.Hasher
import io.mockk.every
import io.mockk.mockk
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AuthenticationServiceAdapterTest {

    private val users: MutableMap<String, UserAuthenticationCompleteEntity> = mutableMapOf()
    private val refreshTokens: MutableMap<UUID, RefreshTokenEntity> = mutableMapOf()
    private val refreshFulls: MutableMap<String, RefreshTokenCompleteEntity> = mutableMapOf()

    private val hasher: Hasher = mockk<Hasher>().apply {
        every { this@apply.hash(any(), any()) } answers {
            val args = it.invocation.args
            "${args[0]}-${args[1]}"
        }
    }

    private val tokenizer: Tokenizer = mockk<Tokenizer>().apply {
        every { this@apply.accessToken(any(), any()) } answers {
            (it.invocation.args[0] as SecurityUser).userId.toString()
        }
    }

    private val userRepository: UserAuthenticationRepository = mockk<UserAuthenticationRepository>().apply {
        every { this@apply.findByUsername(any()) } answers {
            val username = it.invocation.args[0] as String
            users[username]
        }
    }

    private val refreshRepository: RefreshTokenRepository = mockk<RefreshTokenRepository>().apply {
        every { this@apply.upsert(any()) } answers {
            (it.invocation.args[0] as RefreshTokenEntity).apply {
                refreshTokens[this.id] = this
            }
        }

        every { this@apply.findByToken(any()) } answers {
            (it.invocation.args[0] as String).let { token -> refreshFulls[token] }
        }
    }

    private val properties: AuthenticationProperties = AuthenticationProperties(
        accessDurationSeconds = 3600,
        refreshDurationSeconds = 86400,
        resetDurationSeconds = 900,
        minimumPasswordLength = 8,
    )

    private val adapter = AuthenticationServiceAdapter(
        hasher = hasher,
        tokenizer = tokenizer,
        properties = properties,
        userRepository = userRepository,
        refreshRepository = refreshRepository,
    )

    @Test
    fun `WHEN login with correct credentials THEN should returns the token`() {
        val password = randomString(10)
        val entity = userCompleteAuthentication.let {
            it.copy(
                passwordHash = hasher.hash(password, it.passwordSalt),
                isActive = true,
            ).apply {
                users[this.mainEmail] = this
            }
        }
        val request = loginRequest.copy(username = entity.mainEmail, password = password)

        val token = adapter.login(request)

        assertEquals(entity.id.toString(), token.idToken)
        assertTrue(token.refreshToken.endsWith(entity.passwordSalt))
        assertEquals(properties.accessDurationSeconds, token.durationSeconds)
    }

    @Test
    fun `WHEN login with wrong password THEN should throws InvalidCredentialsException`() {
        val password = randomString(10)
        val entity = userCompleteAuthentication.let {
            it.copy(
                passwordHash = hasher.hash(password, it.passwordSalt),
                isActive = true,
            ).apply {
                users[this.mainEmail] = this
            }
        }
        val request = loginRequest.copy(username = entity.mainEmail)

        val exception = assertThrows<InvalidCredentialsException> { adapter.login(request) }
        assertEquals(authenticationUserAuthenticationInvalidCredentialsUsernamePassword(), exception.message)
    }

    @Test
    fun `WHEN login with inactive user THEN should throws InvalidCredentialsException`() {
        val password = randomString(10)
        val entity = userCompleteAuthentication.let {
            it.copy(
                passwordHash = hasher.hash(password, it.passwordSalt),
                isActive = false,
            ).apply {
                users[this.mainEmail] = this
            }
        }
        val request = loginRequest.copy(username = entity.mainEmail, password = password)

        val exception = assertThrows<InvalidCredentialsException> { adapter.login(request) }
        assertEquals(authenticationUserAuthenticationInvalidCredentialsUsernamePassword(), exception.message)
    }

    @Test
    fun `WHEN login with inexistent user THEN should throws InvalidCredentialsException`() {
        val password = randomString(10)
        val entity = userCompleteAuthentication.let {
            it.copy(
                passwordHash = hasher.hash(password, it.passwordSalt),
                isActive = true,
            )
        }
        val request = loginRequest.copy(username = entity.mainEmail, password = password)

        val exception = assertThrows<InvalidCredentialsException> { adapter.login(request) }
        assertEquals(authenticationUserAuthenticationInvalidCredentialsUsernamePassword(), exception.message)
    }

    @Test
    fun `WHEN refresh token with valid data THEN should return new token`() {
        val refresh = userCompleteAuthentication.copy(
            isActive = true
        ).let {
            refreshTokenCompleteEntity.copy(
                id = it.id,
                userAuthentication = it,
            ).apply {
                refreshFulls[this.refreshToken] = this
            }
        }

        val token = adapter.refresh(RefreshTokenRequest((refresh.refreshToken)))

        assertEquals(refresh.userAuthentication.id.toString(), token.idToken)
        assertEquals(refresh.refreshToken, token.refreshToken)
        assertEquals(properties.accessDurationSeconds, token.durationSeconds)
    }

    @Test
    fun `WHEN refresh token with expired token THEN should throws InvalidRefreshTokenException`() {
        val refresh = userCompleteAuthentication.copy(
            isActive = true
        ).let {
            refreshTokenCompleteEntity.copy(
                id = it.id,
                userAuthentication = it,
                validUntil = now(UTC).minusSeconds(1)
            ).apply {
                refreshFulls[this.refreshToken] = this
            }
        }

        val exception = assertThrows<InvalidRefreshTokenException> {
            adapter.refresh(RefreshTokenRequest((refresh.refreshToken)))
        }

        assertEquals(authenticationUserAuthenticationInvalidCredentialsRefreshToken(), exception.message)
    }

    @Test
    fun `WHEN refresh token with inactive user THEN should throws InvalidRefreshTokenException`() {
        val refresh = userCompleteAuthentication.copy(
            isActive = false
        ).let {
            refreshTokenCompleteEntity.copy(
                id = it.id,
                userAuthentication = it,
            ).apply {
                refreshFulls[this.refreshToken] = this
            }
        }

        val exception = assertThrows<InvalidRefreshTokenException> {
            adapter.refresh(RefreshTokenRequest((refresh.refreshToken)))
        }

        assertEquals(authenticationUserAuthenticationInvalidCredentialsRefreshToken(), exception.message)
    }

}
