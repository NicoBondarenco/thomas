package com.thomas.spring.filter

import com.thomas.authentication.Authenticator
import com.thomas.core.context.SessionContextHolder.currentLocale
import com.thomas.core.context.SessionContextHolder.currentToken
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.model.security.SecurityUser
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.util.Locale
import org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE
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
        currentLocale = request.requestLocale()
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

    private fun HttpServletRequest.requestLocale() =
        this.getHeader(ACCEPT_LANGUAGE)?.let {
            Locale.forLanguageTag(it)
        } ?: Locale.ROOT

    private fun HttpServletRequest.bearerToken() =
        this.getHeader(AUTHORIZATION)?.takeIf {
            it.trim().isNotEmpty() && it.startsWith("Bearer")
        }?.replace("Bearer", "")?.trim()

}
