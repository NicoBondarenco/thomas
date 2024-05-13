package com.thomas.spring.resolver

import com.fasterxml.jackson.databind.ObjectMapper
import com.thomas.spring.extension.logByStatus
import com.thomas.spring.extension.toExceptionResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver

@Component
class RestExceptionResolver(
    private val objectMapper: ObjectMapper
) : AbstractHandlerExceptionResolver() {

    override fun doResolveException(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any?,
        ex: Exception
    ): ModelAndView? {
        val body = ex.toExceptionResponse(request.requestURI)
        logger.logByStatus(ex, body.status)
        response.contentType = APPLICATION_JSON_VALUE
        response.status = body.code
        response.writer.write(objectMapper.writeValueAsString(body))
        return ModelAndView()
    }

}
