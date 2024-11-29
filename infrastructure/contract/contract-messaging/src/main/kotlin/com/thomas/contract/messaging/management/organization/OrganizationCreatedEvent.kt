package com.thomas.contract.messaging.management.organization

import com.thomas.contract.messaging.management.AddressStateEvent
import java.time.OffsetDateTime
import java.util.UUID

data class OrganizationCreatedEvent(
    val id: UUID,
    val organizationName: String,
    val fantasyName: String?,
    val registrationNumber: String,
    val maximumUsers: Int,
    val maximumUnits: Int,
    val mainEmail: String,
    val mainPhone: String,
    val addressZipcode: String,
    val addressStreet: String,
    val addressNumber: String,
    val addressComplement: String?,
    val addressNeighborhood: String,
    val addressCity: String,
    val addressState: AddressStateEvent,
    val isActive: Boolean,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
