package com.thomas.core.model.entity

import com.thomas.core.model.entity.DeferredEntityValidationContext.Companion.EMPTY


data class DeferredEntityValidation<T : BaseEntity<T>>(
    val validation: EntityValidation<T>,
    val context: DeferredEntityValidationContext = EMPTY,
)