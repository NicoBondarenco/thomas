package com.thomas.spring.base.extension

import com.thomas.core.context.SessionContextHolder.currentLocale
import com.thomas.core.context.SessionContextHolder.currentToken
import com.thomas.core.context.SessionContextHolder.currentUnit
import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.extension.toUUIDOrNull
import com.thomas.core.model.security.SecurityUser
import com.thomas.spring.base.authentication.Authenticator
import com.thomas.spring.base.authority.OrganizationGrantedAuthority
import com.thomas.spring.base.authority.UnitGrantedAuthority
import com.thomas.spring.base.exception.JWTTokenException
import com.thomas.spring.base.i18n.SpringMessageI18N.authenticationTokenValidateTokenInvalidToken
import jakarta.servlet.http.HttpServletRequest
import java.util.Locale
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource

const val UNIT_HEADER = "Unit-ID"
const val BEARER_PREFIX = "Bearer"

internal fun HttpServletRequest.handleLocale() = this.requestLocale().apply {
    currentLocale = this
    LocaleContextHolder.setLocale(this)
}

internal fun HttpServletRequest.handleAuthentication(
    authenticator: Authenticator
): Boolean = this.bearerToken()?.let {
    val user = authenticator.authenticate(it)
    user.applySpringAuthentication(this@handleAuthentication)
    currentUser = user
    currentToken = it
    this@handleAuthentication.handleUnit(user)
    true
} ?: false

internal fun HttpServletRequest.handleUnit(user: SecurityUser) = this.unitId()?.takeIf {
    user.unitsRoles.keys.contains(it)
}?.apply {
    currentUnit = this
}

internal fun SecurityUser.applySpringAuthentication(request: HttpServletRequest) {
    val authentication = UsernamePasswordAuthenticationToken(this, this, this.grantedAuthorities())
    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
    SecurityContextHolder.getContext().authentication = authentication
    SecurityContextHolder.setDeferredContext {
        SecurityContextImpl(authentication)
    }
}

internal fun SecurityUser.grantedAuthorities() = mutableListOf<GrantedAuthority>().also { roles ->
    roles.addAll(this.organizationRoles.map { role ->
        OrganizationGrantedAuthority(this.securityOrganization.organizationId, role)
    })
    roles.addAll(this.unitsRoles.map { unitRole ->
        unitRole.value.map { UnitGrantedAuthority(unitRole.key, it) }
    }.flatten())
}

internal fun HttpServletRequest.requestLocale() =
    this.getHeader(ACCEPT_LANGUAGE)?.let { tag ->
        Locale.forLanguageTag(tag).takeIf {
            it.isO3Country.isNotEmpty()
        }
    } ?: Locale.ROOT

internal fun HttpServletRequest.bearerToken() =
    this.getHeader(AUTHORIZATION)?.let {
        if (!it.startsWith(BEARER_PREFIX)) {
            throw JWTTokenException(authenticationTokenValidateTokenInvalidToken())
        }
        it.replaceFirst(BEARER_PREFIX, "").trim()
    }

internal fun HttpServletRequest.unitId() =
    this.getHeader(UNIT_HEADER)?.toUUIDOrNull()
