package com.thomas.locality.port.address.viacep.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thomas.locality.port.address.AddressNotFoundException
import com.thomas.locality.port.address.AddressSearchPort
import com.thomas.locality.port.address.model.response.AddressSearchResponse
import com.thomas.locality.port.address.viacep.configuration.ClientFactory
import com.thomas.locality.port.address.viacep.configuration.ViacepProperties
import com.thomas.locality.port.address.viacep.exception.ViacepEmptyBodyException
import com.thomas.locality.port.address.viacep.exception.ViacepSearchException
import com.thomas.locality.port.address.viacep.model.extension.toAddressSearchResponse
import com.thomas.locality.port.address.viacep.model.response.ViacepResponse
import okhttp3.OkHttpClient
import okhttp3.Request

class AddressSearchViacepPort(
    private val properties: ViacepProperties,
) : AddressSearchPort() {

    private val client: OkHttpClient = ClientFactory.client(properties)
    private val mapper: ObjectMapper = ClientFactory.mapper()

    override suspend fun search(
        zipcode: String
    ): AddressSearchResponse = client.newCall(request(zipcode)).execute().use { response ->
        val success = response.takeIf {
            it.isSuccessful
        } ?: throw ViacepSearchException(zipcode, response.code, response.body?.string())

        val body = success.body?.string()?: throw ViacepEmptyBodyException(zipcode, response.code, response.body?.string())

        mapper.readValue<ViacepResponse>(body).takeIf {
            it.erro != true
        }?.toAddressSearchResponse() ?: throw AddressNotFoundException(zipcode)
    }

    private fun request(
        zipcode: String
    ): Request = Request.Builder().url("${properties.baseUrl}/$zipcode/json").build()

}
