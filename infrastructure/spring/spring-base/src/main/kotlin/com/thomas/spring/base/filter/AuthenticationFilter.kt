package com.thomas.spring.base.filter

import com.thomas.core.context.SessionContextHolder.currentLocale
import com.thomas.core.context.SessionContextHolder.currentToken
import com.thomas.core.context.SessionContextHolder.currentUnit
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.model.security.SecurityUser
import com.thomas.spring.base.authenticator.Authenticator
import com.thomas.spring.base.authority.OrganizationGrantedAuthority
import com.thomas.spring.base.authority.UnitGrantedAuthority
import com.thomas.spring.base.extension.bearerToken
import com.thomas.spring.base.extension.requestLocale
import com.thomas.spring.base.extension.unitId
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
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
        request.handleLocale()
        request.handleAuthentication()
        chain.doFilter(request, response)
    }

    private fun HttpServletRequest.handleLocale() = this.requestLocale().apply {
        currentLocale = this
        LocaleContextHolder.setLocale(this)
    }

    private fun HttpServletRequest.handleAuthentication() = this.bearerToken()?.apply {
        val user = authenticator.authenticate(this)
        user.applySpringAuthentication(this@handleAuthentication)
        currentUser = user
        currentToken = this
        this@handleAuthentication.handleUnit(user)
    }

    private fun HttpServletRequest.handleUnit(user: SecurityUser) = this.unitId()?.takeIf {
        user.unitsRoles.keys.contains(it)
    }?.apply {
        currentUnit = this
    }

    private fun SecurityUser.applySpringAuthentication(request: HttpServletRequest) {
        val authentication = UsernamePasswordAuthenticationToken(this, this, this.grantedAuthorities())
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun SecurityUser.grantedAuthorities() = mutableListOf<GrantedAuthority>().also { roles ->
        roles.addAll(this.organizationRoles.map { role ->
            OrganizationGrantedAuthority(this.userOrganization.organizationId, role)
        })
        roles.addAll(this.unitsRoles.map { unitRole ->
            unitRole.value.map { UnitGrantedAuthority(unitRole.key, it) }
        }.flatten())
    }

}
