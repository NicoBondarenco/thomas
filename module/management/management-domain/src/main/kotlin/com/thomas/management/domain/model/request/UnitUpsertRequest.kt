package com.thomas.management.domain.model.request

import com.thomas.core.model.general.AddressState
import com.thomas.management.data.entity.value.UnitType

data class UnitUpsertRequest(
    val unitName: String,
    val fantasyName: String?,
    val documentNumber: String,
    val unitType: UnitType,
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
)
