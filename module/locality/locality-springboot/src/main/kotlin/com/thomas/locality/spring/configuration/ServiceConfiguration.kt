package com.thomas.locality.spring.configuration

import com.thomas.cache.handler.CacheHandler
import com.thomas.locality.data.repository.AddressRepository
import com.thomas.locality.domain.AddressService
import com.thomas.locality.domain.adapter.AddressServiceAdapter
import com.thomas.locality.port.address.AddressSearchPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServiceConfiguration {

    @Bean
    fun addressService(
        addressPort: AddressSearchPort,
        cacheHandler: CacheHandler,
        addressRepository: AddressRepository,
    ): AddressService = AddressServiceAdapter(
        addressPort = addressPort,
        cacheHandler = cacheHandler,
        addressRepository = addressRepository,
    )

}