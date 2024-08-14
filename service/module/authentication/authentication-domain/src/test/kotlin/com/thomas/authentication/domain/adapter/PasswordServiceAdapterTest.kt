package com.thomas.authentication.domain.adapter

import com.thomas.authentication.context.securityUser
import com.thomas.authentication.data.entity.PasswordResetCompleteEntity
import com.thomas.authentication.data.entity.PasswordResetEntity
import com.thomas.authentication.data.entity.UserAuthenticationCompleteEntity
import com.thomas.authentication.data.entity.UserAuthenticationEntity
import com.thomas.authentication.data.repository.ResetPasswordRepository
import com.thomas.authentication.data.repository.UserAuthenticationRepository
import com.thomas.authentication.domain.PasswordService
import com.thomas.authentication.domain.exception.InvalidPasswordException
import com.thomas.authentication.domain.exception.InvalidResetTokenException
import com.thomas.authentication.domain.exception.UserAuthenticationNotFoundException
import com.thomas.authentication.domain.i18n.AuthenticationDomainMessageI18N.authenticationResetPasswordTokenResetInvalidToken
import com.thomas.authentication.domain.i18n.AuthenticationDomainMessageI18N.authenticationUserAuthenticationInvalidPasswordMinimumRequirements
import com.thomas.authentication.domain.i18n.AuthenticationDomainMessageI18N.authenticationUserAuthenticationNotFoundErrorMessage
import com.thomas.authentication.domain.model.request.ChangePasswordRequest
import com.thomas.authentication.domain.model.request.ForgotPasswordRequest
import com.thomas.authentication.domain.model.request.PasswordResetRequest
import com.thomas.authentication.domain.properties.AuthenticationProperties
import com.thomas.authentication.event.resetCompleteEntity
import com.thomas.authentication.event.userAuthentication
import com.thomas.authentication.event.userCompleteAuthentication
import com.thomas.core.context.SessionContextHolder.clearContext
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.hash.Hasher
import io.mockk.every
import io.mockk.mockk
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class PasswordServiceAdapterTest {

    companion object {
        private val CHARS_MAP = mapOf(
            1 to "ABCDEFGHIJKLMNOPQRSTUVWXY".toCharArray().toSet(),
            2 to "abcdefghijklmnopqrstuvwxy".toCharArray().toSet(),
            3 to "0123456789".toCharArray().toSet(),
            0 to "!@#\$%&*()_+-=<>:?,.;".toCharArray().toSet(),
        )

        @JvmStatic
        fun charLists() = CHARS_MAP.values.map {
            Arguments.of(it)
        }
    }

    private val usersComplete: MutableMap<String, UserAuthenticationCompleteEntity> = mutableMapOf()
    private val usersSimple: MutableMap<UUID, UserAuthenticationEntity> = mutableMapOf()

    private val resetTokens: MutableMap<UUID, PasswordResetEntity> = mutableMapOf()
    private val resetsComplete: MutableMap<String, PasswordResetCompleteEntity> = mutableMapOf()

    private val hasher: Hasher = mockk<Hasher>().apply {
        every { this@apply.hash(any(), any()) } answers {
            val args = it.invocation.args
            "${args[0]}-${args[1]}"
        }
    }

    private val userRepository: UserAuthenticationRepository = mockk<UserAuthenticationRepository>().apply {

        every { this@apply.findByUsername(any()) } answers {
            val username = it.invocation.args[0] as String
            usersComplete[username]
        }

        every { this@apply.one(any()) } answers {
            (it.invocation.args[0] as UUID).let { id -> usersSimple[id] }
        }

        every { this@apply.update(any()) } answers {
            (it.invocation.args[0] as UserAuthenticationEntity).apply {
                usersSimple[this.id] = this
            }
        }

    }

    private val resetRepository: ResetPasswordRepository = mockk<ResetPasswordRepository>().apply {

        every { this@apply.upsert(any()) } answers {
            (it.invocation.args[0] as PasswordResetEntity).apply {
                resetTokens[this.id] = this
            }
        }

        every { this@apply.findByToken(any()) } answers {
            (it.invocation.args[0] as String).let { token -> resetsComplete[token] }
        }

    }

    private val properties: AuthenticationProperties = AuthenticationProperties(
        accessDurationSeconds = 3600,
        refreshDurationSeconds = 86400,
        resetDurationSeconds = 900,
        minimumPasswordLength = 8,
    )

    private val adapter: PasswordService = PasswordServiceAdapter(
        hasher = hasher,
        properties = properties,
        resetRepository = resetRepository,
        userRepository = userRepository,
    )

    private fun generatePassword(
        length: Int = 8
    ): String = (1..length).map {
        val index = if (it < 4) {
            it
        } else {
            it % 4
        }
        CHARS_MAP[index]!!.random()
    }.joinToString("")

    @BeforeEach
    fun beforeEach() {
        clearContext()
    }

    @Test
    fun `WHEN changes password with valid password THEN should update the password`() {
        currentUser = securityUser
        userAuthentication.copy(id = currentUser.userId).apply {
            usersSimple[this.id] = this
        }
        val password = generatePassword()

        adapter.changePassword(ChangePasswordRequest(password))

        val user = usersSimple[currentUser.userId]
        assertEquals("${password}-${user!!.passwordSalt}", user.passwordHash)
    }

    @Test
    fun `WHEN changes password with less than 8 chars THEN should throws InvalidPasswordException`() {
        currentUser = securityUser
        userAuthentication.copy(id = currentUser.userId).apply {
            usersSimple[this.id] = this
        }
        val password = generatePassword(7)

        val exception = assertThrows<InvalidPasswordException> {
            adapter.changePassword(ChangePasswordRequest(password))
        }
        assertEquals(authenticationUserAuthenticationInvalidPasswordMinimumRequirements(), exception.message)
    }

    @Test
    fun `WHEN changes password with inexistent user THEN should throws UserAuthenticationNotFoundException`() {
        currentUser = securityUser
        userAuthentication.copy(id = currentUser.userId)
        val password = generatePassword(7)

        val exception = assertThrows<UserAuthenticationNotFoundException> {
            adapter.changePassword(ChangePasswordRequest(password))
        }
        assertEquals(authenticationUserAuthenticationNotFoundErrorMessage(currentUser.userId), exception.message)
    }

    @ParameterizedTest
    @MethodSource("charLists")
    fun `WHEN changes password without chars THEN should throws InvalidPasswordException`(
        chars: Set<Char>
    ) {
        currentUser = securityUser
        userAuthentication.copy(id = currentUser.userId).apply {
            usersSimple[this.id] = this
        }
        val password = generatePassword(15).filter { !chars.contains(it) }

        val exception = assertThrows<InvalidPasswordException> {
            adapter.changePassword(ChangePasswordRequest(password))
        }
        assertEquals(authenticationUserAuthenticationInvalidPasswordMinimumRequirements(), exception.message)
    }

    @Test
    fun `WHEN requested forgot password THEN should save the forgot token`() {
        val entity = userCompleteAuthentication.apply {
            usersComplete[this.mainEmail] = this
        }

        adapter.forgotPassword(ForgotPasswordRequest((entity.mainEmail)))

        val token = resetTokens[entity.id]
        assertNotNull(token)
        assertTrue(token!!.resetToken.endsWith(entity.passwordSalt))
    }

    @Test
    fun `WHEN requested forgot password for non existent user THEN should not save the forgot token`() {
        val entity = userCompleteAuthentication

        adapter.forgotPassword(ForgotPasswordRequest(entity.mainEmail))

        val token = resetTokens[entity.id]
        assertNull(token)
    }

    @Test
    fun `WHEN resetting password with valid token THEN should update password`() {
        val token = resetCompleteEntity.apply {
            resetsComplete[this.resetToken] = this
            usersSimple[this.userAuthentication.id] = this.userAuthentication
        }

        val request = PasswordResetRequest(
            newPassword = generatePassword(),
            resetToken = token.resetToken,
        )

        adapter.resetPassword(request)

        val user = usersSimple[token.id]

        assertEquals("${request.newPassword}-${user!!.passwordSalt}", user.passwordHash)
    }

    @Test
    fun `WHEN resetting password with expired token THEN should throws InvalidResetTokenException`() {
        val token = resetCompleteEntity.copy(
            validUntil = now(UTC).minusSeconds(1)
        ).apply {
            resetsComplete[this.resetToken] = this
            usersSimple[this.userAuthentication.id] = this.userAuthentication
        }

        val request = PasswordResetRequest(
            newPassword = generatePassword(),
            resetToken = token.resetToken,
        )

        val exception = assertThrows<InvalidResetTokenException> { adapter.resetPassword(request) }

        assertEquals(authenticationResetPasswordTokenResetInvalidToken(), exception.message)
    }

    @Test
    fun `WHEN resetting password with inexistent token THEN should throws InvalidResetTokenException`() {
        val token = resetCompleteEntity.apply {
            usersSimple[this.userAuthentication.id] = this.userAuthentication
        }

        val request = PasswordResetRequest(
            newPassword = generatePassword(),
            resetToken = token.resetToken,
        )

        val exception = assertThrows<InvalidResetTokenException> { adapter.resetPassword(request) }

        assertEquals(authenticationResetPasswordTokenResetInvalidToken(), exception.message)
    }

    @Test
    fun `WHEN resetting password with password less than 8 chars THEN should throws InvalidPasswordException`() {
        val token = resetCompleteEntity.apply {
            resetsComplete[this.resetToken] = this
            usersSimple[this.userAuthentication.id] = this.userAuthentication
        }

        val request = PasswordResetRequest(
            newPassword = generatePassword(7),
            resetToken = token.resetToken,
        )

        val exception = assertThrows<InvalidPasswordException> { adapter.resetPassword(request) }

        assertEquals(authenticationUserAuthenticationInvalidPasswordMinimumRequirements(), exception.message)
    }

    @ParameterizedTest
    @MethodSource("charLists")
    fun `WHEN resetting password without chars THEN should throws InvalidPasswordException`(
        chars: Set<Char>
    ) {
        val token = resetCompleteEntity.apply {
            resetsComplete[this.resetToken] = this
            usersSimple[this.userAuthentication.id] = this.userAuthentication
        }

        val request = PasswordResetRequest(
            newPassword = generatePassword(15).filter { !chars.contains(it) },
            resetToken = token.resetToken,
        )

        val exception = assertThrows<InvalidPasswordException> { adapter.resetPassword(request) }

        assertEquals(authenticationUserAuthenticationInvalidPasswordMinimumRequirements(), exception.message)
    }

}
