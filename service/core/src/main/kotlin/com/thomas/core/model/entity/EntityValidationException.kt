package com.thomas.core.model.entity

data class EntityValidationException(
    override val message: String,
    val errors: List<EntityValidationErrorDetail>
) : RuntimeException(message)
