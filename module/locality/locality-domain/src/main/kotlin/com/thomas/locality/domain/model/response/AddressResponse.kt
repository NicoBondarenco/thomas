package com.thomas.locality.domain.model.response

import com.thomas.core.model.general.AddressState
import java.time.OffsetDateTime
import java.util.UUID

data class AddressResponse(
    val id: UUID,
    val zipcodeNumber: String,
    val addressStreet: String?,
    val addressComplement: String?,
    val addressUnit: String?,
    val addressNeighborhood: String?,
    val addressCity: String,
    val addressState: AddressState,
    val cityCode: String?,
    val referenceCode: String?,
    val phoneCode: String?,
    val federalCode: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
