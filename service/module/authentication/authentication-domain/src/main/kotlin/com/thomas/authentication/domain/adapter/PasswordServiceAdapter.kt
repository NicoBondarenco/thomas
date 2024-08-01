package com.thomas.authentication.domain.adapter

import com.thomas.authentication.data.entity.PasswordResetEntity
import com.thomas.authentication.data.entity.UserAuthenticationBaseEntity
import com.thomas.authentication.data.entity.UserAuthenticationEntity
import com.thomas.authentication.data.repository.ResetPasswordRepository
import com.thomas.authentication.data.repository.UserAuthenticationRepository
import com.thomas.authentication.domain.PasswordService
import com.thomas.authentication.domain.exception.InvalidPasswordException
import com.thomas.authentication.domain.exception.InvalidResetTokenException
import com.thomas.authentication.domain.exception.UserAuthenticationNotFoundException
import com.thomas.authentication.domain.model.extension.changePassword
import com.thomas.authentication.domain.model.request.ChangePasswordRequest
import com.thomas.authentication.domain.model.request.ForgotPasswordRequest
import com.thomas.authentication.domain.model.request.PasswordResetRequest
import com.thomas.authentication.domain.properties.AuthenticationProperties
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.extension.throws
import com.thomas.hash.Hasher
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID
import java.util.UUID.randomUUID

class PasswordServiceAdapter(
    private val hasher: Hasher,
    private val properties: AuthenticationProperties,
    private val resetRepository: ResetPasswordRepository,
    private val userRepository: UserAuthenticationRepository,
) : PasswordService {

    companion object {
        private val PASSWORD_UPPER_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXY".toCharArray().toSet()
        private val PASSWORD_LOWER_CHARACTERS = "abcdefghijklmnopqrstuvwxy".toCharArray().toSet()
        private val PASSWORD_NUMBER_CHARACTERS = "0123456789".toCharArray().toSet()
        private val PASSWORD_SPECIAL_CHARACTERS = "!@#\$%&*()_+-=<>:?,.;".toCharArray().toSet()
    }

    override fun changePassword(
        request: ChangePasswordRequest
    ) = request.newPassword.let { password ->
        findByIdOrThrows(currentUser.userId).updatePassword(password)
    }

    override fun forgotPassword(request: ForgotPasswordRequest) {
        userRepository.findByUsername(request.userEmail)?.apply {
            val resetToken = hasher.hash(randomUUID().toString(), this.passwordSalt)
            resetRepository.upsert(this.toPasswordResetEntity(resetToken))
        }
    }

    override fun resetPassword(request: PasswordResetRequest) {
        resetRepository.findByToken(request.resetToken)?.takeIf {
            it.validUntil.isBefore(now(UTC))
        }?.userAuthentication?.updatePassword(request.newPassword)
            ?: throw InvalidResetTokenException()
    }

    private fun UserAuthenticationEntity.updatePassword(
        password: String
    ) = this.apply {
        password.validatePassword()
        this.changePassword(hasher.hash(password, this.passwordSalt))
    }.update()

    private fun String.validatePassword() {
        this.toCharArray().let { array ->
            listOf(
                array.intersect(PASSWORD_UPPER_CHARACTERS).isNotEmpty(),
                array.intersect(PASSWORD_LOWER_CHARACTERS).isNotEmpty(),
                array.intersect(PASSWORD_NUMBER_CHARACTERS).isNotEmpty(),
                array.intersect(PASSWORD_SPECIAL_CHARACTERS).isNotEmpty(),
                array.size >= properties.minimumPasswordLength,
            )
        }.takeIf { it.contains(false) }?.throws { InvalidPasswordException() }
    }

    private fun UserAuthenticationBaseEntity.toPasswordResetEntity(
        resetToken: String,
    ) = PasswordResetEntity(
        id = this.id,
        resetToken = resetToken,
        validUntil = now(UTC).plusSeconds(properties.resetDurationSeconds),
    )

    private fun findByIdOrThrows(
        id: UUID,
    ): UserAuthenticationEntity = userRepository.one(id)
        ?: throw UserAuthenticationNotFoundException(id)

    private fun UserAuthenticationEntity.update() {
        userRepository.update(this)
    }

}
