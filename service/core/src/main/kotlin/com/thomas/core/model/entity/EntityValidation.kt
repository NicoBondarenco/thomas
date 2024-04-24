package com.thomas.core.model.entity

data class EntityValidation<T : BaseEntity<T>>(
    val code: String,
    val message: (T) -> String,
    val validate: (T) -> Boolean
)