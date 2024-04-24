package com.thomas.core

import com.thomas.core.model.http.HTTPStatus

class HttpApplicationException(
    val status: HTTPStatus,
    message: String,
    val detail: Any? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause)
