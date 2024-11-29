package com.thomas.contract.messaging.management.unit

import com.thomas.contract.messaging.management.AddressStateEvent
import com.thomas.contract.messaging.management.UnitTypeEvent
import java.time.OffsetDateTime
import java.util.UUID

data class UnitUpdatedEvent(
    val id: UUID,
    val unitName: String,
    val fantasyName: String?,
    val documentNumber: String,
    val unitType: UnitTypeEvent,
    val unitOrganization: UUID,
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
