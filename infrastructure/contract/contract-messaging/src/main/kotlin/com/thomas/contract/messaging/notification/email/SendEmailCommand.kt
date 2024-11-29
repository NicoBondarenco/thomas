package com.thomas.contract.messaging.notification.email

data class SendEmailCommand(
    val emailRecipients: Set<String>,
    val copyRecipients: Set<String>,
    val blindRecipients: Set<String>,
    val attachmentLocations: Set<String>,
    val emailSubject: String,
    val emailModel: String,
    val emailValues: Map<String, String>,
)
