package com.thomas.authentication.jwt.auth0.i18n

import com.thomas.core.i18n.BundleResolver

object AuthenticationJWTAuth0MessageI18N : BundleResolver("strings/authentication-jwt-auth0") {

    fun authenticationTokenRetrieveClaimMissingId() = getFormattedMessage("authentication.token.retrieve-claim.missing-id")

    fun authenticationTokenRetrieveClaimInvalidId() = getFormattedMessage("authentication.token.retrieve-claim.invalid-id")

    fun authenticationTokenRetrieveUserNotFound() = getFormattedMessage("authentication.token.retrieve-user.not-found")

    fun authenticationTokenRetrieveUserInactiveUser() = getFormattedMessage("authentication.token.retrieve-user.inactive-user")

    fun authenticationTokenValidateTokenExpiredToken() = getFormattedMessage("authentication.token.validate-token.expired-token")

    fun authenticationTokenValidateTokenDecodeError() = getFormattedMessage("authentication.token.validate-token.decode-error")

    fun authenticationTokenValidateTokenInvalidSignature() = getFormattedMessage("authentication.token.validate-token.invalid-signature")

    fun authenticationTokenValidateTokenInvalidIssuer() = getFormattedMessage("authentication.token.validate-token.invalid-issuer")

}
