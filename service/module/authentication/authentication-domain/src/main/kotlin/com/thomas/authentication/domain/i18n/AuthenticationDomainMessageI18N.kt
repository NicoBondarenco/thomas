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


}
