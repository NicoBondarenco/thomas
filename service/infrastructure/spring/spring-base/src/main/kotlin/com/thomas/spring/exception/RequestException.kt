package com.thomas.spring.exception

import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.INVALID_PARAMETER
import com.thomas.spring.extension.errorDetails
import com.thomas.spring.i18n.SpringMessageI18N.exceptionInvalidArgumentParameterErrorsMessage
import com.thomas.spring.i18n.SpringMessageI18N.requestRequestParameterValidationConvertError
import org.springframework.beans.TypeMismatchException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException

class RequestException : DetailedException {

    constructor(
        message: String,
        detail: Any? = null,
        cause: Throwable? = null
    ) : super(
        message = message,
        type = INVALID_PARAMETER,
        detail = detail,
        cause = cause,
    )

    constructor(
        cause: MethodArgumentNotValidException,
    ) : this(
        message = exceptionInvalidArgumentParameterErrorsMessage(),
        detail = cause.errorDetails(),
        cause = cause,
    )

    constructor(
        cause: TypeMismatchException,
    ) : this(
        message = requestRequestParameterValidationConvertError(cause.propertyName!!, cause.value!!),
        cause = cause,
    )

    constructor(
        cause: HttpMessageNotReadableException,
    ) : this(
        message = exceptionInvalidArgumentParameterErrorsMessage(),
        cause = cause,
    )

}
