package com.thomas.spring.base.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import com.thomas.core.extension.logger
import com.thomas.spring.base.extension.handleAuthentication
import com.thomas.spring.base.extension.handleLocale
import com.thomas.spring.base.extension.logByStatus
import com.thomas.spring.base.extension.toExceptionResponse
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.filter.OncePerRequestFilter

class AuthenticationFilter(
    private val authenticator: Authenticator,
    private val objectMapper: ObjectMapper,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        try {
            request.handleLocale()
            request.handleAuthentication(authenticator)
            chain.doFilter(request, response)
        } catch (e: Exception) {
            logger().logByStatus(e)
            e.toExceptionResponse(request.requestURI).apply {
                response.contentType = APPLICATION_JSON_VALUE
                response.status = this.status.value()
                response.writer.write(objectMapper.writeValueAsString(this))
            }
        }
    }

}
