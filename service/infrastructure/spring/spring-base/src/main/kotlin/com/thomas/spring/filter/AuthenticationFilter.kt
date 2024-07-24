package com.thomas.spring.filter

import com.thomas.authentication.Authenticator
import com.thomas.core.context.SessionContextHolder.currentLocale
import com.thomas.core.context.SessionContextHolder.currentToken
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.model.security.SecurityUser
import com.thomas.spring.extension.requestLocale
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class AuthenticationFilter(
    private val authenticator: Authenticator
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        request.requestLocale().apply {
            currentLocale = this
            LocaleContextHolder.setLocale(this)
        }
        request.bearerToken()?.apply {
            val user = authenticator.authenticate(this)
            user.applySpringAuthentication(request)
            currentUser = user
            currentToken = this
        }
        chain.doFilter(request, response)
    }

    private fun SecurityUser.applySpringAuthentication(request: HttpServletRequest) {
        val authentication = UsernamePasswordAuthenticationToken(this, this, this.grantedAuthorities())
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun SecurityUser.grantedAuthorities() =
        this.currentRoles().map { SimpleGrantedAuthority(it.name) }

    private fun HttpServletRequest.bearerToken() =
        this.getHeader(AUTHORIZATION)?.replaceFirst("Bearer", "")?.trim()

}
