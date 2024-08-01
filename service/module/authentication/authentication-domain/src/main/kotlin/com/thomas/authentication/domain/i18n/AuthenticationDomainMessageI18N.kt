package com.thomas.authentication.domain.i18n

import com.thomas.core.i18n.BundleResolver
import java.util.UUID

object AuthenticationDomainMessageI18N : BundleResolver("strings/authentication-domain") {

    fun authenticationUserAuthenticationNotFoundErrorMessage(id: UUID) =
        getFormattedMessage("authentication.user-authentication.not-found.error-message", id)

    fun authenticationGroupAuthenticationNotFoundErrorMessage(id: UUID) =
        getFormattedMessage("authentication.group-authentication.not-found.error-message", id)

    fun authenticationUserAuthenticationInvalidCredentialsUsernamePassword() =
        getFormattedMessage("authentication.user-authentication.invalid-credentials.username-password")

    fun authenticationUserAuthenticationInvalidCredentialsRefreshToken() =
        getFormattedMessage("authentication.user-authentication.invalid-credentials.refresh-token")

    fun authenticationUserAuthenticationInvalidPasswordMinimumRequirements() =
        getFormattedMessage("authentication.user-authentication.invalid-password.minimum-requirements")

    fun authenticationResetPasswordTokenResetInvalidToken() =
        getFormattedMessage("authentication.reset-password.token-reset.invalid-token")

}
