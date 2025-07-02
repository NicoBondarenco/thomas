package com.thomas.locality.domain.adapter

import com.thomas.cache.handler.CacheHandler
import com.thomas.core.authorization.authorized
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.locality.data.entity.AddressEntity
import com.thomas.locality.data.repository.AddressRepository
import com.thomas.locality.domain.AddressService
import com.thomas.locality.domain.model.extension.toAddressEntity
import com.thomas.locality.domain.model.extension.toAddressResponse
import com.thomas.locality.domain.model.extension.update
import com.thomas.locality.domain.model.response.AddressResponse
import com.thomas.locality.port.address.AddressNotFoundException
import com.thomas.locality.port.address.AddressSearchPort

class AddressServiceAdapter(
    private val addressPort: AddressSearchPort,
    private val cacheHandler: CacheHandler,
    private val addressRepository: AddressRepository,
) : AddressService {

    override suspend fun addressByZipcode(
        addressZipcode: String,
        forceUpdate: Boolean,
    ): AddressResponse = authorized {
        forceUpdate.takeIf { it && currentUser.isMaster }?.let {
            addressBySearch(addressZipcode, true)
        } ?: addressBySteps(addressZipcode)
    }.apply {
        cacheHandler.set(this.zipcodeNumber, this)
    }.toAddressResponse()

    private suspend fun addressBySearch(
        zipcode: String,
        check: Boolean,
    ): AddressEntity = addressPort.searchByZipcode(zipcode).let { response ->
        check.takeIf { it }?.let {
            addressRepository.findByZipcode(zipcode)?.update(response)?.apply {
                addressRepository.update(this)
            }
        } ?: response.toAddressEntity().apply {
            addressRepository.create(this)
        }
    }

    private suspend fun addressBySteps(
        zipcode: String
    ): AddressEntity = (cacheHandler.get(zipcode) ?: addressRepository.findByZipcode(zipcode))
        ?: addressBySearch(zipcode, false)

}