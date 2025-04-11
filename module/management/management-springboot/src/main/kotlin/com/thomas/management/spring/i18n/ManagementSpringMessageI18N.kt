package com.thomas.management.spring.i18n

import com.thomas.core.i18n.BundleResolver

object ManagementSpringMessageI18N: BundleResolver("strings/management-spring")  {

    fun refreshTokenValidateTokenExpiredToken() = formattedMessage("refresh.token.validate-token.expired-token")

    fun refreshTokenValidateTokenDecodeError() = formattedMessage("refresh.token.validate-token.decode-error")

    fun refreshTokenValidateTokenInvalidSignature() = formattedMessage("refresh.token.validate-token.invalid-signature")

    fun refreshTokenValidateTokenInvalidIssuer() = formattedMessage("refresh.token.validate-token.invalid-issuer")

}
