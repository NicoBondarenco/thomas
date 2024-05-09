package com.thomas.spring.exception

import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.INVALID_PARAMETER
import org.springframework.web.bind.MethodArgumentNotValidException

class RequestException : DetailedException {

    constructor(
        message: String,
    ) : super(
        message = message,
        type = INVALID_PARAMETER,
    )

    constructor(
        message: String,
        cause: MethodArgumentNotValidException,
    ) : super(
        message = message,
        type = INVALID_PARAMETER,
        detail = cause.bindingResult.allErrors,
        cause = cause,
    )

}
