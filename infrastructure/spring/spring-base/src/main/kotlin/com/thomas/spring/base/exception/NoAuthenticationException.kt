package com.thomas.spring.base.exception

import com.thomas.spring.base.i18n.SpringMessageI18N.authenticationManagerValidateAuthenticationNoAuthentication
import org.springframework.security.core.AuthenticationException

class NoAuthenticationException: AuthenticationException(
    authenticationManagerValidateAuthenticationNoAuthentication()
)