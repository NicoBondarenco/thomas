package com.thomas.spring.resource

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ValidateRequest(
    @get:NotBlank
    @get:Size(min = 10)
    val email: String,
    @get:Max(10)
    val quantity: Int,
)
