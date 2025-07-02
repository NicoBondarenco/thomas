package com.thomas.locality.port.address.viacep.configuration

data class ViacepProperties(
    val baseUrl: String,
    val requestTimeout: Long = 5000,
    val connectTimeout: Long = 5000,
)
