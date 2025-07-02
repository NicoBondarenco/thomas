package com.thomas.management.domain.model.request

import com.thomas.core.model.general.AddressState

data class OrganizationUpsertRequest(
    val organizationName: String,
    val fantasyName: String? = null,
    val registrationNumber: String,
    val maximumUsers: Int,
    val maximumUnits: Int,
    val mainEmail: String,
    val mainPhone: String,
    val addressZipcode: String,
    val addressStreet: String,
    val addressNumber: String,
    val addressComplement: String? = null,
    val addressNeighborhood: String,
    val addressCity: String,
    val addressState: AddressState,
    val isActive: Boolean,
)
