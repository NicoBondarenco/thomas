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
            Arguments.of("3i3V\"6k"),
            Arguments.of("y62^I%2"),
            Arguments.of("NyhO##P"),
            Arguments.of(",.CbRXB"),
            Arguments.of("m*]<aW$"),
            Arguments.of("|@1ZAnz"),
            Arguments.of("o]T:37f"),
            Arguments.of("F4127.4["),
            Arguments.of(";{W1):[D"),
            Arguments.of("}!8;HD+&"),
            Arguments.of("b>,n+*fo"),
            Arguments.of("{:l|:p@&"),
            Arguments.of("|3]&q[4f"),
            Arguments.of("&U{azx:z"),
            Arguments.of("r\$tpBmBG"),
            Arguments.of("^p*DEfUT"),
            Arguments.of("6xd6DJYB"),
            Arguments.of("8bfIlzoS"),
            Arguments.of("ZkTseyho"),
        )

        @JvmStatic
        fun validPasswords() = listOf(
            Arguments.of("y|em[isWbtA{oK9"),
            Arguments.of("S<2lPYnnn]*al:R"),
            Arguments.of("In}&0aH?"),
            Arguments.of("*2eJ3D+[Af"),
            Arguments.of("L6y7YZ0w."),
            Arguments.of(",n64UMf[-"),
            Arguments.of("oQO|NCRpMpe0lec"),
            Arguments.of("ghc7GN)Z,osmLJR"),
            Arguments.of("{ARR8J%YspuhD"),
            Arguments.of("+LDXuG&5TyMa]Sh"),
            Arguments.of("92QBk(J5O^dXd@m"),
            Arguments.of("h1YhWCg#6-(.P3f"),
            Arguments.of("CCWm2-8KOM^"),
            Arguments.of("-7FdQSfN}(G5JMp"),
            Arguments.of("HP|rC,Y9+y{ZesT"),
            Arguments.of("gl3}}|2G21("),
            Arguments.of("m*Mh4IxU"),
            Arguments.of("edD4e+8C}uy[e(,"),
            Arguments.of("o!PATZ-3&2"),
            Arguments.of("9UU:c){FiTxb>,H"),
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

    @ParameterizedTest
    @MethodSource("validPasswords")
    fun `Change password`(
        password: String
    ) = runTest(StandardTestDispatcher()) {
        currentUser = securityUser
        assertDoesNotThrow {
            passwordService.changePassword(changePasswordRequest.copy(newPassword = password))
        }
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
