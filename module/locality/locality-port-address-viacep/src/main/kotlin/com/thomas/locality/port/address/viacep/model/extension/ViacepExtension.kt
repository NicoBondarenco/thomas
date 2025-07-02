package com.thomas.locality.port.address.viacep.model.extension

import com.thomas.core.extension.onlyNumbers
import com.thomas.core.model.general.AddressState
import com.thomas.locality.port.address.model.response.AddressSearchResponse
import com.thomas.locality.port.address.viacep.model.response.ViacepResponse

fun ViacepResponse.toAddressSearchResponse() = AddressSearchResponse(
    zipcodeNumber = this.cep!!.onlyNumbers(),
    addressStreet = this.logradouro,
    addressComplement = this.complemento,
    addressUnit = this.unidade,
    addressNeighborhood = this.bairro,
    addressCity = this.localidade!!,
    addressState = AddressState.valueOf(this.uf!!),
    cityCode = this.ibge,
    referenceCode = this.gia,
    phoneCode = this.ddd,
    federalCode = this.siafi,
)