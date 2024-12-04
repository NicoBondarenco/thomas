package com.thomas.management.domain.model.mapper

import com.thomas.contract.messaging.notification.email.SendEmailCommand
import com.thomas.management.data.entity.PasswordResetEntity
import com.thomas.management.domain.properties.PasswordProperties
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.coroutineScope

private const val RESET_TOKEN_MODEL_KEY = "reset_token"
private const val TOKEN_VALIDITY_MODEL_KEY = "token_validity"

suspend fun PasswordResetEntity.toSendEmailCommand(
    userEmail: String,
    passwordProperties: PasswordProperties
) = coroutineScope {
    val formatter = DateTimeFormatter.ofPattern(passwordProperties.tokenValidityPattern)
    SendEmailCommand(
        emailRecipients = setOf(userEmail),
        copyRecipients = setOf(),
        blindRecipients = setOf(),
        attachmentLocations = setOf(),
        emailSubject = passwordProperties.resetEmailSubject,
        emailModel = passwordProperties.resetEmailModel,
        emailValues = mapOf(
            RESET_TOKEN_MODEL_KEY to this@toSendEmailCommand.resetToken,
            TOKEN_VALIDITY_MODEL_KEY to formatter.format(this@toSendEmailCommand.expiresOn),
        ),
    )
}