package com.thomas.core.extension

import com.thomas.core.HttpApplicationException
import com.thomas.core.i18n.CoreMessageI18N.coreExceptionResponseMessageNoMessage
import com.thomas.core.model.http.HTTPStatus
import com.thomas.core.model.http.HTTPStatus.INTERNAL_SERVER_ERROR
import com.thomas.core.model.response.ExceptionResponse
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC

fun HttpApplicationException.toExceptionResponse(uri: String) =
    this.toExceptionResponse(uri, this.status, this.detail)

fun Throwable.toExceptionResponse(
    uri: String,
    status: HTTPStatus = INTERNAL_SERVER_ERROR,
    details: Any? = null
): ExceptionResponse = ExceptionResponse(
    timestamp = now(UTC),
    status = status,
    code = status.code,
    path = uri,
    message = this.message ?: coreExceptionResponseMessageNoMessage(),
    detail = details
)
