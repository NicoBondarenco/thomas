package com.thomas.spring.base.authentication

import com.thomas.spring.base.exception.NoAuthenticationException
import com.thomas.spring.base.extension.handleAuthentication
import com.thomas.spring.base.extension.handleLocale
import java.util.function.Supplier
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.core.Authentication
import org.springframework.security.web.access.intercept.RequestAuthorizationContext
import org.springframework.stereotype.Component

@Component
class ApplicationAuthorizationManager(
    private val authenticator: Authenticator
) : AuthorizationManager<RequestAuthorizationContext> {

    @Deprecated("Deprecated in Java")
    override fun check(
        authentication: Supplier<Authentication>,
        requestContext: RequestAuthorizationContext,
    ): AuthorizationDecision = requestContext.request.let { request ->
        request.handleLocale()
        request.handleAuthentication(authenticator).takeIf { it } ?: throw NoAuthenticationException()
        AuthorizationDecision(true)
    }

}
