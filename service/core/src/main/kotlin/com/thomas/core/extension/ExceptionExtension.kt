package com.thomas.core.extension

import com.thomas.core.HttpApplicationException
import com.thomas.core.i18n.CoreMessageI18N.coreExceptionResponseMessageNoMessage
import com.thomas.core.model.response.ExceptionResponse
import com.thomas.core.model.http.HTTPStatus
import com.thomas.core.model.http.HTTPStatus.INTERNAL_SERVER_ERROR
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC

fun Throwable.toExceptionResponse(uri: String, status: HTTPStatus? = null): ExceptionResponse {
    val httpStatus: HTTPStatus
    val statusCode: Int
    val detailsValue: Any?

    if (this is HttpApplicationException) {
        httpStatus = this.status
        statusCode = this.status.code
        detailsValue = this.detail
    } else {
        httpStatus = status ?: INTERNAL_SERVER_ERROR
        statusCode = (status ?: INTERNAL_SERVER_ERROR).code
        detailsValue = null
    }

    return ExceptionResponse(
        timestamp = now(UTC),
        status = httpStatus,
        code = statusCode,
        path = uri,
        message = this.message ?: coreExceptionResponseMessageNoMessage(),
        detail = detailsValue
    )
}