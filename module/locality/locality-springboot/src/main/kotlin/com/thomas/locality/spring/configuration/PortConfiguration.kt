package com.thomas.locality.spring.configuration

import com.thomas.locality.port.address.AddressSearchPort
import com.thomas.locality.port.address.viacep.client.AddressSearchViacepPort
import com.thomas.locality.port.address.viacep.configuration.ViacepProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PortConfiguration {

    @Bean
    fun addressSearchProperties(
        @Value("\${locality.port.search.baseUrl}") baseUrl: String,
        @Value("\${locality.port.search.requestTimeout}") requestTimeout: Long,
        @Value("\${locality.port.search.connectTimeout}") connectTimeout: Long,
    ): ViacepProperties = ViacepProperties(
        baseUrl = baseUrl,
        requestTimeout = requestTimeout,
        connectTimeout = connectTimeout,
    )

    @Bean
    fun addressSearchPort(
        properties: ViacepProperties
    ): AddressSearchPort = AddressSearchViacepPort(properties)

}
