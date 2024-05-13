package com.thomas.spring.extension

import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException

internal fun MethodArgumentNotValidException.errorDetails() =
    this.bindingResult.allErrors.groupBy({ (it as FieldError).field }, { it.defaultMessage })
