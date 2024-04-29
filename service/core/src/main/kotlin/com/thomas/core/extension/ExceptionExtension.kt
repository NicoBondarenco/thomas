package com.thomas.core.extension

import com.thomas.core.i18n.CoreMessageI18N.exceptionExceptionResponseErrorMessageDefaultMessage
import com.thomas.core.model.response.ExceptionResponse
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC

fun Throwable.toExceptionResponse(
    uri: String,
    status: String = "SERVER_ERROR",
    code: Int = -1,
    details: Any? = null
): ExceptionResponse = ExceptionResponse(
    timestamp = now(UTC),
    status = status,
    code = code,
    path = uri,
    message = this.message ?: exceptionExceptionResponseErrorMessageDefaultMessage(),
    detail = details
)
