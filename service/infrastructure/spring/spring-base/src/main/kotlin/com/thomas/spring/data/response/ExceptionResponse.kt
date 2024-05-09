package com.thomas.spring.data.response

import java.time.OffsetDateTime
import org.springframework.http.HttpStatus

data class ExceptionResponse(
    val timestamp: OffsetDateTime,
    val status: HttpStatus,
    val code: Int,
    val path: String,
    val message: String,
    val detail: Any?
)