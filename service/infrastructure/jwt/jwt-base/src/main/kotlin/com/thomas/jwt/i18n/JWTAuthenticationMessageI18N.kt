package com.thomas.jwt.i18n

import com.thomas.core.i18n.BundleResolver

object JWTAuthenticationMessageI18N : BundleResolver("strings/jwt-base") {

    fun authenticationJWTBaseTokenInvalidToken() = getFormattedMessage("authentication.jwt.base.token.invalid-token")

    fun authenticationJWTBaseUserInactiveUser() = getFormattedMessage("authentication.jwt.base.user.inactive-user")

}
