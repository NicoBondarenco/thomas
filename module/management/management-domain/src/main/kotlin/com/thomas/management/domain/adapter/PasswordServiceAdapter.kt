package com.thomas.management.domain.adapter

import com.thomas.core.authorization.authorized
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.extension.validate
import com.thomas.hasher.Hasher
import com.thomas.management.data.entity.PasswordResetEntity
import com.thomas.management.data.repository.PasswordResetRepository
import com.thomas.management.data.repository.UserRepository
import com.thomas.management.domain.PasswordService
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementUserValidationUserDataInvalidData
import com.thomas.management.domain.model.mapper.toUserSimpleResponse
import com.thomas.management.domain.model.request.ChangePasswordRequest
import com.thomas.management.domain.model.request.ForgotPasswordRequest
import com.thomas.management.domain.model.request.ResetPasswordRequest
import com.thomas.management.domain.model.response.UserSimpleResponse
import com.thomas.management.domain.properties.PasswordProperties
import com.thomas.management.domain.validation.validPassword
import java.time.OffsetDateTime

class PasswordServiceAdapter(
    userRepository: UserRepository,
    private val passwordRepository: PasswordResetRepository,
    private val hasher: Hasher,
    private val passwordProperties: PasswordProperties,
) : UserBaseAdapter(userRepository), PasswordService {

    override suspend fun changePassword(
        request: ChangePasswordRequest,
    ): UserSimpleResponse = authorized {
        findSimpleByIdOrThrows(currentUser.userId).let { entity ->
            listOf(
                validPassword(request.newPassword)
            ).validate(entity, managementUserValidationUserDataInvalidData())

            userRepository.updateSimple(
                entity.copy(
                    passwordHash = hasher.hash(request.newPassword, entity.passwordSalt)
                )
            ).toUserSimpleResponse()
        }
    }

    override suspend fun forgotPassword(request: ForgotPasswordRequest) {
        userRepository.simpleByEmail(request.mainEmail)?.apply {
            val token = hasher.hash(hasher.generateSalt(), this.passwordSalt)
            val expiresOn = OffsetDateTime.now().plusMinutes(passwordProperties.tokenTtl)
            val entity = PasswordResetEntity(
                userId = this.id,
                resetToken = token,
                validUntil = expiresOn,
            )
            passwordRepository.upsertToken(entity)
        }
    }

    override suspend fun resetPassword(request: ResetPasswordRequest) {
        TODO("Not yet implemented")
    }

}