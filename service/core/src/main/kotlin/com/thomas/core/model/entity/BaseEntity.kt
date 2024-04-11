package com.thomas.core.model.entity

import com.thomas.core.extension.throws
import com.thomas.core.i18n.CoreMessageI18N.coreExceptionEntityValidationValidationError

abstract class BaseEntity<T : BaseEntity<T>> {

    @Suppress("UNCHECKED_CAST")
    fun validate() =
        validations()
            .filter { !it.validate(this as T) }
            .takeIf { it.isNotEmpty() }
            ?.map { EntityValidationErrorDetail(it.code, it.message(this as T)) }
            ?.throws { EntityValidationException(errorMessage(), it) }

    open fun errorMessage(): String = coreExceptionEntityValidationValidationError()

    open fun validations(): List<Validation<T>> = listOf()

    @Suppress("UNCHECKED_CAST")
    open fun update(block: T.() -> Unit): T =
        (this as T).apply(block).apply { this.validate() }

}

data class Validation<T : BaseEntity<T>>(
    val code: String,
    val message: (T) -> String,
    val validate: (T) -> Boolean
)