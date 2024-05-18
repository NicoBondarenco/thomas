package com.thomas.core.model.entity

import com.thomas.core.extension.throws
import com.thomas.core.i18n.CoreMessageI18N.validationEntityValidationInvalidErrorMessage
import java.util.UUID

abstract class BaseEntity<T : BaseEntity<T>> {

    abstract val id: UUID

    @Suppress("UNCHECKED_CAST")
    fun validate() =
        validations()
            .filter { !it.validate(this as T) }
            .takeIf { it.isNotEmpty() }
            ?.groupBy( { it.field }, { it.message(this as T) })
            ?.throws { EntityValidationException(errorMessage(), it) }

    open fun errorMessage(): String = validationEntityValidationInvalidErrorMessage()

    open fun validations(): List<EntityValidation<T>> = listOf()

}
