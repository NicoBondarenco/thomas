package com.thomas.management.domain.model.request

import com.thomas.management.data.entity.value.AddressState

data class SignupOrganizationRequest(
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
)
