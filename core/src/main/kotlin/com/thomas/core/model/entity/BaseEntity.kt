package com.thomas.core.model.entity

import com.thomas.core.extension.throws
import com.thomas.core.i18n.CoreMessageI18N.validationEntityValidationInvalidErrorMessage
import java.util.UUID
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

abstract class BaseEntity<T : BaseEntity<T>> {

    abstract val id: UUID

    @Suppress("UNCHECKED_CAST")
    suspend fun validate(
        businessValidation: List<EntityValidation<T>> = listOf(),
    ) = validations().validate(this as T, errorMessage(), businessValidation)

    open fun errorMessage(): String = validationEntityValidationInvalidErrorMessage()

    open fun validations(): List<EntityValidation<T>> = listOf()

    private suspend fun List<EntityValidation<T>>.validate(
        entity: T,
        errorMessage: String,
        businessValidation: List<EntityValidation<T>>,
    ) = coroutineScope {
        (this@validate + businessValidation).map { validation ->
            async { validation.takeIf { !it.validate(entity) } }
        }.awaitAll().filterNotNull().takeIf {
            it.isNotEmpty()
        }?.groupBy(
            { it.field },
            { it.message(entity) }
        )?.throws {
            EntityValidationException(errorMessage, it)
        }
    }

}
