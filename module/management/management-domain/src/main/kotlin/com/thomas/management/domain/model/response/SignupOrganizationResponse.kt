package com.thomas.management.domain.model.response

import com.thomas.management.data.entity.value.AddressState
import java.time.OffsetDateTime
import java.util.UUID

data class SignupOrganizationResponse(
    val id: UUID,
    val organizationName: String,
    val fantasyName: String?,
    val registrationNumber: String,
    val maximumUsers: Int,
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
