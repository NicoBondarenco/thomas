package com.thomas.core.extension

import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.entity.EntityValidation
import com.thomas.core.model.entity.EntityValidationException

fun <T : BaseEntity<T>> List<EntityValidation<T>>.validate(
    entity: T,
    errorMessage: String
) = this.filter {
    !it.validate(entity)
}.takeIf {
    it.isNotEmpty()
}?.groupBy(
    { it.field },
    { it.message(entity) }
)?.throws {
    EntityValidationException(errorMessage, it)
}
