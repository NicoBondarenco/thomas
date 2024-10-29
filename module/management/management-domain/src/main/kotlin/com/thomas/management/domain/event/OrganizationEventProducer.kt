package com.thomas.management.domain.event

import com.thomas.management.data.entity.OrganizationEntity

interface OrganizationEventProducer {

    suspend fun organizationCreated(entity: OrganizationEntity)

    suspend fun organizationUpdated(entity: OrganizationEntity)

}