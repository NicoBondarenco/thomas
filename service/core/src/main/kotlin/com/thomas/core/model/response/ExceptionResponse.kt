package com.thomas.core.model.response

import java.time.OffsetDateTime

data class ExceptionResponse(
    val timestamp: OffsetDateTime,
    val status: String,
    val code: Int,
    val path: String,
    val message: String,
    val detail: Any?
)
