package com.thomas.management.domain.adapter

import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.context.UnauthenticatedUserException
import com.thomas.core.extension.toSnakeCase
import com.thomas.core.model.entity.EntityValidationException
import com.thomas.management.data.entity.UserEntity
import com.thomas.management.domain.PasswordService
import com.thomas.management.domain.exception.ResetPasswordException
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementResetPasswordResetTokenExpiredToken
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementResetPasswordResetTokenInvalidToken
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUserDataInvalidData
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUserDataInvalidPassword
import com.thomas.management.domain.mock.expiredTokens
import com.thomas.management.domain.mock.hasherMock
import com.thomas.management.domain.mock.notificationProducerMock
import com.thomas.management.domain.mock.passwordRepositoryMock
import com.thomas.management.domain.mock.passwordTokens
import com.thomas.management.domain.mock.userEmails
import com.thomas.management.domain.mock.userRepositoryMock
import com.thomas.management.domain.properties.PasswordProperties
import com.thomas.management.domain.util.changePasswordRequest
import com.thomas.management.domain.util.forgotPasswordRequest
import com.thomas.management.domain.util.resetPasswordRequest
import com.thomas.management.domain.util.securityUser
import io.mockk.coVerify
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class PasswordServiceAdapterTest : DomainValidationTest() {

    companion object {

        @JvmStatic
        fun invalidPasswords() = listOf(
            Arguments.of(""),
            Arguments.of("        "),
            Arguments.of("Qwerty123"),
            Arguments.of("qwerty@123"),
            Arguments.of("qwerty@ASDF"),
            Arguments.of("98409549"),
            Arguments.of("iuhdfasi"),
            Arguments.of("AIDHJIUQ"),
            Arguments.of("%#$@%!**"),
        )

    }

    private val passwordProperties = PasswordProperties(
        tokenValidityMinutes = 60,
        resetEmailSubject = "Reset Password",
        resetEmailModel = "reset-password",
        tokenValidityPattern = "dd/MM/yyyy HH:mm:ss",
    )

    private val passwordService: PasswordService = PasswordServiceAdapter(
        userRepository = userRepositoryMock,
        passwordRepository = passwordRepositoryMock,
        hasher = hasherMock,
        passwordProperties = passwordProperties,
        notificationProducer = notificationProducerMock,
    )

    @Test
    fun `Change password`() = runTest(StandardTestDispatcher()) {
        currentUser = securityUser
        assertDoesNotThrow { passwordService.changePassword(changePasswordRequest) }
    }

    @Test
    fun `Change password without login`() = runTest(StandardTestDispatcher()) {
        assertThrows<UnauthenticatedUserException> { passwordService.changePassword(changePasswordRequest) }
    }

    @ParameterizedTest
    @MethodSource("invalidPasswords")
    fun `Change password invalid password`(
        password: String
    ) = runTest(StandardTestDispatcher()) {
        currentUser = securityUser
        val exception = assertThrows<EntityValidationException> {
            passwordService.changePassword(
                changePasswordRequest.copy(newPassword = password)
            )
        }
        assertEquals(managementUserValidationUserDataInvalidData(), exception.message)

        val details = (exception.detail as? Map<String, List<String>>)!!
        assertEquals(1, details.size)

        val field = UserEntity::passwordHash.name.toSnakeCase()
        assertTrue(details.containsKey(field))
        assertEquals(1, details[field]!!.size)
        assertEquals(managementUserValidationUserDataInvalidPassword(), details[field]!!.first())
    }

    @Test
    fun `Forgot password`() = runTest(StandardTestDispatcher()) {
        passwordService.forgotPassword(forgotPasswordRequest)
        coVerify(exactly = 1) { notificationProducerMock.sendEmail(any()) }
    }

    @Test
    fun `Forgot password user not found`() = runTest(StandardTestDispatcher()) {
        forgotPasswordRequest.apply {
            userEmails.add(this.mainEmail)
            passwordService.forgotPassword(this)
            coVerify(exactly = 0) { notificationProducerMock.sendEmail(any()) }
        }
    }

    @Test
    fun `Reset password`() = runTest(StandardTestDispatcher()) {
        assertDoesNotThrow { passwordService.resetPassword(resetPasswordRequest) }
    }

    @Test
    fun `Reset password not found`() = runTest(StandardTestDispatcher()) {
        resetPasswordRequest.apply {
            passwordTokens.add(this.resetToken)
            val exception = assertThrows<ResetPasswordException> {
                passwordService.resetPassword(this)
            }
            assertEquals(managementResetPasswordResetTokenInvalidToken(), exception.message)
        }
    }

    @Test
    fun `Reset password expired`() = runTest(StandardTestDispatcher()) {
        resetPasswordRequest.apply {
            expiredTokens.add(this.resetToken)
            val exception = assertThrows<ResetPasswordException> {
                passwordService.resetPassword(this)
            }
            assertEquals(managementResetPasswordResetTokenExpiredToken(), exception.message)
        }
    }

}
