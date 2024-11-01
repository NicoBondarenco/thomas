package com.thomas.management.domain.model.response

import com.thomas.management.data.entity.OrganizationEntity
import com.thomas.management.data.entity.value.AddressState
import com.thomas.management.data.entity.value.UnitType
import java.time.OffsetDateTime
import java.util.UUID

data class UnitResponse(
    val id: UUID,
    val unitName: String,
    val fantasyName: String?,
    val documentNumber: String,
    val unitType: UnitType,
    val unitOrganization: OrganizationResponse,
    val mainEmail: String,
    val mainPhone: String,
    val addressZipcode: String,
    val addressStreet: String,
    val addressNumber: String,
    val addressComplement: String?,
    val addressNeighborhood: String,
    val addressCity: String,
    val addressState: AddressState,
    val isActive: Boolean,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
