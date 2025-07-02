package com.thomas.locality.data.repository

import com.thomas.locality.data.entity.AddressEntity

interface AddressRepository {

    suspend fun create(entity: AddressEntity): AddressEntity

    suspend fun update(entity: AddressEntity): AddressEntity

    suspend fun findByZipcode(zipcode: String): AddressEntity?

}
