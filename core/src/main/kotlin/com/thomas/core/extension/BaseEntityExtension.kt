package com.thomas.core.extension

import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.entity.DeferredEntityValidation
import com.thomas.core.model.entity.EntityValidation
import com.thomas.core.model.entity.EntityValidationException
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun <T : BaseEntity<T>> List<DeferredEntityValidation<T>>.validate(
    entity: T,
    errorMessage: String,
) = coroutineScope {
    val errors = ConcurrentHashMap<String, MutableList<String>>()

    this@validate.map { validation ->
        validation.context.defer(this) {
            errors.validate(entity, validation.validation)
        }
    }.awaitAll()

    errors.takeIf {
        it.isNotEmpty()
    }?.throws {
        EntityValidationException(errorMessage, it)
    }
}

private suspend fun <T : BaseEntity<T>> ConcurrentHashMap<String, MutableList<String>>.validate(
    entity: T,
    validation: EntityValidation<T>,
) = coroutineScope {
    validation.takeIf {
        !it.validate(entity)
    }?.run {
        this@validate.getOrPut(this.field) { mutableListOf() }.add(this.message(entity))
    }
}