package com.thomas.spring.extension

import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType
import com.thomas.core.exception.ErrorType.APPLICATION_ERROR
import com.thomas.core.exception.ErrorType.INVALID_ENTITY
import com.thomas.core.exception.ErrorType.INVALID_PARAMETER
import com.thomas.core.exception.ErrorType.NOT_FOUND
import com.thomas.core.exception.ErrorType.UNAUTHENTICATED_USER
import com.thomas.core.exception.ErrorType.UNAUTHORIZED_ACTION
import com.thomas.spring.data.response.ExceptionResponse
import com.thomas.spring.i18n.SpringMessageI18N.exceptionThrowableMessageDefault
import java.time.OffsetDateTime.now
import java.time.ZoneOffset.UTC
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.http.HttpStatus.NOT_FOUND as HTTP_NOT_FOUND

internal fun Throwable.toExceptionResponse(
    uri: String
) = this.httpStatus().let {
    ExceptionResponse(
        timestamp = now(UTC),
        status = it,
        code = it.value(),
        path = uri,
        message = this.message ?: exceptionThrowableMessageDefault(),
        detail = this.details()
    )
}

private fun Throwable.httpStatus() = when (this) {
    is DetailedException -> this.type.toHttpStatus()
    else -> INTERNAL_SERVER_ERROR
}

private fun Throwable.details() = when (this) {
    is DetailedException -> this.detail
    else -> null
}

internal fun ErrorType.toHttpStatus() = when (this) {
    UNAUTHENTICATED_USER -> UNAUTHORIZED
    UNAUTHORIZED_ACTION -> UNAUTHORIZED
    INVALID_ENTITY -> UNPROCESSABLE_ENTITY
    INVALID_PARAMETER -> BAD_REQUEST
    NOT_FOUND -> HTTP_NOT_FOUND
    APPLICATION_ERROR -> INTERNAL_SERVER_ERROR
}
