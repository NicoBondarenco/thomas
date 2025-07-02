package com.thomas.locality.spring.controller

import com.thomas.locality.domain.AddressService
import com.thomas.locality.domain.model.response.AddressResponse
import com.thomas.locality.spring.controller.LocalityPath.PRIVATE_API_V1_ADDRESS
import com.thomas.locality.spring.controller.LocalityPath.PRIVATE_API_V1_ADDRESS_ZIPCODE
import java.util.UUID
import org.komapper.core.DryRunDatabaseConfig.id
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PRIVATE_API_V1_ADDRESS)
class AddressController(
    private val addressService: AddressService,
) {

    @GetMapping(PRIVATE_API_V1_ADDRESS_ZIPCODE)
    suspend fun zipcode(
        @PathVariable zipcode: String,
        @RequestParam(name = "u", required = false) forceUpdate: Boolean = false,
    ): ResponseEntity<AddressResponse> = ok(addressService.addressByZipcode(zipcode,  forceUpdate))

}