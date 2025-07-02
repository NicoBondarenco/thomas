package com.thomas.locality.port.address.viacep.client

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.badRequest
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.okJson
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.thomas.core.model.general.AddressState
import com.thomas.core.util.NumberUtils.randomInteger
import com.thomas.core.util.StringUtils.randomString
import com.thomas.locality.port.address.AddressNotFoundException
import com.thomas.locality.port.address.AddressRequestErrorException
import com.thomas.locality.port.address.InvalidZipcodeException
import com.thomas.locality.port.address.model.response.AddressSearchResponse
import com.thomas.locality.port.address.viacep.configuration.ViacepProperties
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


@TestInstance(PER_CLASS)
class AddressSearchViacepPortTest {

    companion object {

        @JvmStatic
        fun validZipcodes() = (1..20).map {
            Arguments.of(randomInteger(10000000, 99999999).toString())
        }.toList()

        @JvmStatic
        fun invalidZipcodes() = (1..5).map {
            Arguments.of(randomInteger(10000, 99999).toString())
        }.toList() + (1..5).map {
            Arguments.of(randomInteger(100000000, 999999999).toString())
        }.toList() + (1..5).map {
            Arguments.of(randomString(length = 8))
        }.toList()

        @JvmStatic
        fun badZipcodes() = (1..20).map {
            Arguments.of(randomInteger(10000000, 99999999).toString())
        }.toList()

        @JvmStatic
        fun errorZipcodes() = (1..20).map {
            Arguments.of(randomInteger(10000000, 99999999).toString())
        }.toList()

    }

    private val wiremock: WireMockServer = WireMockServer(options().port(8189))
    private lateinit var properties: ViacepProperties
    private lateinit var port: AddressSearchViacepPort

    private fun searchResponse(
        zipcodeNumber: String,
    ): AddressSearchResponse = AddressSearchResponse(
        zipcodeNumber = zipcodeNumber,
        addressStreet = listOf(randomString(), null).random(),
        addressComplement = listOf(randomString(), null).random(),
        addressUnit = listOf(randomString(), null).random(),
        addressNeighborhood = listOf(randomString(), null).random(),
        addressCity = randomString(),
        addressState = AddressState.entries.random(),
        cityCode = listOf(randomString(), null).random(),
        referenceCode = listOf(randomString(), null).random(),
        phoneCode = listOf(randomInteger(10, 99).toString(), null).random(),
        federalCode = listOf(randomString(), null).random(),
    )

    private fun validResponse(response: AddressSearchResponse): String = "{" +
            "\"cep\": \"${response.zipcodeNumber.substring(0, 5)}-${response.zipcodeNumber.substring(5)}\"," +
            "\"logradouro\": ${response.addressStreet?.let { "\"$it\"" }}," +
            "\"complemento\": ${response.addressComplement?.let { "\"$it\"" }}," +
            "\"unidade\": ${response.addressUnit?.let { "\"$it\"" }}," +
            "\"bairro\": ${response.addressNeighborhood?.let { "\"$it\"" }}," +
            "\"localidade\": \"${response.addressCity}\"," +
            "\"uf\": \"${response.addressState.name}\"," +
            "\"estado\": \"${response.addressState.label}\"," +
            "\"regiao\": \"${response.addressState.region.label}\"," +
            "\"ibge\": ${response.cityCode?.let { "\"$it\"" }}," +
            "\"gia\": ${response.referenceCode?.let { "\"$it\"" }}," +
            "\"ddd\": ${response.phoneCode?.let { "\"$it\"" }}," +
            "\"siafi\": ${response.federalCode?.let { "\"$it\"" }}" +
            "}"

    private fun invalidResponse(): String = "{\"erro\": \"true\"}"

    @BeforeAll
    fun beforeAll() {
        wiremock.start()
        properties = ViacepProperties(wiremock.baseUrl())
        port = AddressSearchViacepPort(properties)
    }

    @AfterAll
    fun afterAll() {
        wiremock.stop()
    }

    @ParameterizedTest
    @MethodSource("validZipcodes")
    fun `Return a valid address search response when the request is successful`(
        zipcode: String
    ) = runTest(StandardTestDispatcher()) {
        val searchResponse = searchResponse(zipcode)
        val validResponse = validResponse(searchResponse)
        wiremock.stubFor(
            get(urlEqualTo("/${zipcode}/json")).willReturn(okJson(validResponse))
        )
        val result = port.searchByZipcode(zipcode)
        assertEquals(searchResponse, result)
    }

    @ParameterizedTest
    @MethodSource("errorZipcodes")
    fun `Return null address search response when the request is error`(
        zipcode: String
    ) = runTest(StandardTestDispatcher()) {
        wiremock.stubFor(
            get(urlEqualTo("/${zipcode}/json")).willReturn(okJson(invalidResponse()))
        )
        assertThrows<AddressNotFoundException> { port.searchByZipcode(zipcode) }
    }

    @ParameterizedTest
    @MethodSource("invalidZipcodes")
    fun `Throw InvalidZipcodeException when the zipcode is invalid`(
        zipcode: String
    ) = runTest(StandardTestDispatcher()) {
        assertThrows<InvalidZipcodeException> { port.searchByZipcode(zipcode) }
    }

    @ParameterizedTest
    @MethodSource("badZipcodes")
    fun `Throw an exception when the request is not successful`(
        zipcode: String
    ) = runTest(StandardTestDispatcher()) {
        wiremock.stubFor(
            get(urlEqualTo("/${zipcode}/json")).willReturn(badRequest())
        )
        assertThrows<AddressRequestErrorException> { port.searchByZipcode(zipcode) }
    }

}
