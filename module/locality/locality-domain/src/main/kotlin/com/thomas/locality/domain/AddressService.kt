package com.thomas.locality.domain

import com.thomas.locality.domain.model.response.AddressResponse

interface AddressService {

    suspend fun addressByZipcode(addressZipcode: String, forceUpdate: Boolean): AddressResponse

}