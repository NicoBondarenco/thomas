package com.thomas.spring.configuration

import com.thomas.spring.data.extension.toExceptionResponse
import com.thomas.spring.exception.RequestException
import com.thomas.spring.i18n.SpringMessageI18N.exceptionInvalidArgumentParameterErrorsMessage
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ExceptionHandlerConfiguration : ResponseEntityExceptionHandler() {

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? = handleExceptionInternal(
        RequestException(exceptionInvalidArgumentParameterErrorsMessage(), ex),
        null,
        headers,
        status,
        request
    )

    override fun handleExceptionInternal(
        exception: Exception,
        body: Any?,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? = exception.let {
        it.logByStatus(status)
        super.handleExceptionInternal(
            it,
            body ?: it.toExceptionResponse(request.contextPath),
            headers,
            status,
            request
        )
    }

    private fun Exception.logByStatus(code: HttpStatusCode) {
        if (code.is5xxServerError) {
            logger.error(this.message, this)
        } else {
            logger.debug(this.message, this)
        }
    }

}
