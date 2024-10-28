package com.thomas.management.data.repository

import java.util.UUID

interface OrganizationRepository {

    suspend fun hasAnotherWithName(id: UUID, organizationName: String): Boolean

    suspend fun hasAnotherWithRegistration(id: UUID, registrationName: String): Boolean

}