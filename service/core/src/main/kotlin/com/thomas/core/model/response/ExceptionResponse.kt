package com.thomas.core.model.response

import com.thomas.core.model.http.HTTPStatus
import java.time.OffsetDateTime

data class ExceptionResponse(
    val timestamp: OffsetDateTime,
    val status: HTTPStatus,
    val code: Int,
    val path: String,
    val message: String,
    val detail: Any?
)
