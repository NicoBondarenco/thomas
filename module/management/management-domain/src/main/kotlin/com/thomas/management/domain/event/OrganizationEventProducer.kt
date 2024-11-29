package com.thomas.management.domain.event

import com.thomas.contract.messaging.management.organization.OrganizationCreatedEvent
import com.thomas.contract.messaging.management.organization.OrganizationUpdatedEvent

interface OrganizationEventProducer {

    suspend fun organizationCreated(event: OrganizationCreatedEvent)

    suspend fun organizationUpdated(event: OrganizationUpdatedEvent)

}
