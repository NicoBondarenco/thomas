package com.thomas.core.exception

import com.thomas.core.exception.ErrorType.APPLICATION_ERROR
import com.thomas.core.i18n.CoreMessageI18N.exceptionDetailedExceptionMessageDefault

abstract class DetailedException(
    override val message: String = exceptionDetailedExceptionMessageDefault(),
    val type: ErrorType = APPLICATION_ERROR,
    val detail: Any? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause)
