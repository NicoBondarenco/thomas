package com.thomas.locality.domain.model.extension

import com.thomas.locality.data.entity.AddressEntity
import com.thomas.locality.domain.model.response.AddressResponse
import com.thomas.locality.port.address.model.response.AddressSearchResponse
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC

fun AddressSearchResponse.toAddressEntity() = AddressEntity(
    zipcodeNumber = this.zipcodeNumber,
    addressStreet = this.addressStreet,
    addressComplement = this.addressComplement,
    addressUnit = this.addressUnit,
    addressNeighborhood = this.addressNeighborhood,
    addressCity = this.addressCity,
    addressState = this.addressState,
    cityCode = this.cityCode,
    referenceCode = this.referenceCode,
    phoneCode = this.phoneCode,
    federalCode = this.federalCode,
)

fun AddressEntity.update(response: AddressSearchResponse) = this.copy(
    zipcodeNumber = response.zipcodeNumber,
    addressStreet = response.addressStreet,
    addressComplement = response.addressComplement,
    addressUnit = response.addressUnit,
    addressNeighborhood = response.addressNeighborhood,
    addressCity = response.addressCity,
    addressState = response.addressState,
    cityCode = response.cityCode,
    referenceCode = response.referenceCode,
    phoneCode = response.phoneCode,
    federalCode = response.federalCode,
    updatedAt = now(UTC)
)

fun AddressEntity.toAddressResponse() = AddressResponse(
    id = this.id,
    zipcodeNumber = this.zipcodeNumber,
    addressStreet = this.addressStreet,
    addressComplement = this.addressComplement,
    addressUnit = this.addressUnit,
    addressNeighborhood = this.addressNeighborhood,
    addressCity = this.addressCity,
    addressState = this.addressState,
    cityCode = this.cityCode,
    referenceCode = this.referenceCode,
    phoneCode = this.phoneCode,
    federalCode = this.federalCode,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)
