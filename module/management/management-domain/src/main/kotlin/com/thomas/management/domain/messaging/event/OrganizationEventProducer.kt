package com.thomas.management.domain.messaging.event

import com.thomas.contract.messaging.management.organization.OrganizationManagementEvent

interface OrganizationEventProducer {

    suspend fun organizationCreated(event: OrganizationManagementEvent)

    suspend fun organizationUpdated(event: OrganizationManagementEvent)

}
