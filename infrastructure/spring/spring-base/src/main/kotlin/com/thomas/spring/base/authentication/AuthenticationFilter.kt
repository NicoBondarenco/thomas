package com.thomas.spring.base.authentication

import com.thomas.spring.base.extension.handleAuthentication
import com.thomas.spring.base.extension.handleLocale
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter

class AuthenticationFilter(
    private val authenticator: Authenticator
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        request.handleLocale()
        request.handleAuthentication(authenticator)
        chain.doFilter(request, response)
    }


}
