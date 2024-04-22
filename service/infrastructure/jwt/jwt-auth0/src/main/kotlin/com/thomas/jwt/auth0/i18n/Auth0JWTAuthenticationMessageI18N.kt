package com.thomas.jwt.auth0.i18n

import com.thomas.core.i18n.BundleResolver

object Auth0JWTAuthenticationMessageI18N : BundleResolver("strings/jwt-auth0") {

    fun authenticationJWTAuth0TokenExpiredToken() = getFormattedMessage("authentication.jwt.auth0.token.expired-token")

    fun authenticationJWTAuth0TokenNotFound() = getFormattedMessage("authentication.jwt.auth0.token.not-found")

}
