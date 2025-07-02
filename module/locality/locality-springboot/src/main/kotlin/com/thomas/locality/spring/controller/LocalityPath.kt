package com.thomas.locality.spring.controller

object LocalityPath {

    private const val PRIVATE_API_V1_PREFIX = "/api/v1/locality"

    const val PRIVATE_API_V1_ADDRESS = "$PRIVATE_API_V1_PREFIX/addresses"
    const val PRIVATE_API_V1_ADDRESS_ZIPCODE = "/zipcode/{zipcode}"

}
