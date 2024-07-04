package com.thomas.core.model.entity

import com.thomas.core.extension.validate
import com.thomas.core.i18n.CoreMessageI18N.validationEntityValidationInvalidErrorMessage
import java.util.UUID

abstract class BaseEntity<T : BaseEntity<T>> {

    abstract val id: UUID

    @Suppress("UNCHECKED_CAST")
    fun validate() = validations().validate(this as T, errorMessage())

    open fun errorMessage(): String = validationEntityValidationInvalidErrorMessage()

    open fun validations(): List<EntityValidation<T>> = listOf()

}
