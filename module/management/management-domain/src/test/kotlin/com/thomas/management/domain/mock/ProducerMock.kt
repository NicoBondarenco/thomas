package com.thomas.management.domain.mock

import com.thomas.management.domain.messaging.command.NotificationCommandProducer
import com.thomas.management.domain.messaging.event.OrganizationEventProducer
import com.thomas.management.domain.messaging.event.UnitEventProducer
import com.thomas.management.domain.messaging.event.UserEventProducer
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.spyk

internal val organizationProducerMock = spyk<OrganizationEventProducer>().apply {
    coEvery { organizationCreated(any()) } returns Unit
    coEvery { organizationUpdated(any()) } returns Unit
}

internal val unitProducerMock = spyk<UnitEventProducer>().apply {
    coEvery { unitCreated(any()) } returns Unit
    coEvery { unitUpdated(any()) } returns Unit
    coEvery { unitDeleted(any()) } returns Unit
}

internal val userProducerMock = spyk<UserEventProducer>().apply {
    coEvery { userCreated(any()) } returns Unit
    coEvery { userUpdated(any()) } returns Unit
}

internal val notificationProducerMock = spyk<NotificationCommandProducer>().apply {
    coEvery { sendEmail(any()) } returns Unit
}

fun clearProducerMocks() = clearMocks(
    organizationProducerMock,
    unitProducerMock,
    userProducerMock,
    notificationProducerMock,
    answers = false,
    recordedCalls = true,
    childMocks = false,
    verificationMarks = true,
    exclusionRules = false,
)
