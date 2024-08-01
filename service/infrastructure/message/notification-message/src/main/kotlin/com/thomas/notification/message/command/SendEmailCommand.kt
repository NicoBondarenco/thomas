package com.thomas.notification.message.command

data class SendEmailCommand(
    val emailRecipient: String,
    val emailSubject: String,
    val emailContent: String,
)
