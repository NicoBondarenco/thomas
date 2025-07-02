package com.thomas.locality.port.address.model.response

import com.thomas.core.model.general.AddressState

data class AddressSearchResponse(
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
)
