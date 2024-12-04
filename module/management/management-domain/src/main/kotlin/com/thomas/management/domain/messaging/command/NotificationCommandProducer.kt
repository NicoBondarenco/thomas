package com.thomas.management.domain.messaging.command

import com.thomas.contract.messaging.notification.email.SendEmailCommand

interface NotificationCommandProducer {

    suspend fun sendEmail(command: SendEmailCommand)

}