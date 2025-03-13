package com.thomas.management.domain.adapter

import com.thomas.core.authorization.authorized
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.extension.validate
import com.thomas.hasher.Hasher
import com.thomas.management.data.entity.PasswordResetEntity
import com.thomas.management.data.entity.UserSimpleEntity
import com.thomas.management.data.repository.PasswordResetRepository
import com.thomas.management.data.repository.UserRepository
import com.thomas.management.domain.PasswordService
import com.thomas.management.domain.exception.ResetPasswordException.Companion.expiredToken
import com.thomas.management.domain.exception.ResetPasswordException.Companion.invalidToken
import com.thomas.management.domain.exception.UserNotFoundException
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUserDataInvalidData
import com.thomas.management.domain.messaging.command.NotificationCommandProducer
import com.thomas.management.domain.model.mapper.toSendEmailCommand
import com.thomas.management.domain.model.mapper.toUserSimpleResponse
import com.thomas.management.domain.model.request.ChangePasswordRequest
import com.thomas.management.domain.model.request.ForgotPasswordRequest
import com.thomas.management.domain.model.request.ResetPasswordRequest
import com.thomas.management.domain.model.response.UserSimpleResponse
import com.thomas.management.domain.properties.PasswordProperties
import com.thomas.management.domain.validation.validPassword
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import java.util.UUID
import kotlinx.coroutines.coroutineScope

class PasswordServiceAdapter(
    private val userRepository: UserRepository,
    private val passwordRepository: PasswordResetRepository,
    private val hasher: Hasher,
    private val passwordProperties: PasswordProperties,
    private val notificationProducer: NotificationCommandProducer,
) : PasswordService {

    companion object {
        private val RESET_TOKEN_CHARS = "ABCDEFGHIJKLMNOPQRSTUWVXYZabcdefghijklmnopqrstuwvxyz0123456789".toCharArray()
        private const val RESET_TOKEN_LENGTH = 30
    }

    override suspend fun changePassword(
        request: ChangePasswordRequest,
    ): UserSimpleResponse = authorized {
        updatePassword(currentUser.userId, request.newPassword).toUserSimpleResponse()
    }

    override suspend fun forgotPassword(request: ForgotPasswordRequest) {
        userRepository.simpleByEmail(request.mainEmail)?.apply {
            val token = hasher.hash(generateResetToken(), this.passwordSalt)
            val expiresOn = now().plusMinutes(passwordProperties.tokenValidityMinutes)
            val entity = passwordRepository.upsertToken(
                PasswordResetEntity(
                    userId = this.id,
                    resetToken = token,
                    expiresOn = expiresOn,
                )
            )
            notificationProducer.sendEmail(entity.toSendEmailCommand(this.mainEmail, passwordProperties))
        }
    }

    override suspend fun resetPassword(request: ResetPasswordRequest) {
        (passwordRepository.findByToken(request.resetToken) ?: throw invalidToken()).takeIf {
            now(UTC).isBefore(it.expiresOn)
        }?.run {
            updatePassword(this.userId, request.newPassword)
        }?: throw expiredToken()
    }

    private suspend fun findSimpleByIdOrThrows(
        id: UUID,
    ): UserSimpleEntity = userRepository.byId(id) ?: throw UserNotFoundException(id)

    private suspend fun generateResetToken(): String = coroutineScope {
        (1..RESET_TOKEN_LENGTH).map { RESET_TOKEN_CHARS.random() }.joinToString(separator = "")
    }

    private suspend fun updatePassword(
        userId: UUID,
        newPassword: String,
    ): UserSimpleEntity = findSimpleByIdOrThrows(userId).let { entity ->
        listOf(
            validPassword(newPassword)
        ).validate(entity, managementUserValidationUserDataInvalidData())

        userRepository.updateSimple(
            entity.copy(
                passwordHash = hasher.hash(newPassword, entity.passwordSalt)
            )
        )
    }

}
