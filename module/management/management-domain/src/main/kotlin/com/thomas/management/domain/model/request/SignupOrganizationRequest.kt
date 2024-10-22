package com.thomas.management.domain.model.request

import java.time.OffsetDateTime

data class SignupOrganizationRequest(
    val organizationName: String,
    val fantasyName: String?,
    val organizationNumber: String,
    val mainEmail: String,
    val mainPhone: String,
    val addressZipcode: String,
    val addressStreet: String,
    val addressNumber: String,
    val addressComplement: String,
    val addressCity: String,
    val addressState: String,
    val isActive: Boolean,
    val createAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
