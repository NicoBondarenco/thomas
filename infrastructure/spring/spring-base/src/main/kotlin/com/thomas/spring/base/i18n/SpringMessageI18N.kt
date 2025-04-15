package com.thomas.spring.base.i18n

import com.thomas.core.i18n.BundleResolver

internal object SpringMessageI18N : BundleResolver("strings/spring-strings") {

    fun exceptionThrowableMessageDefault(): String = formattedMessage("exception.throwable.message.default")

    fun exceptionInvalidArgumentParameterErrorsMessage(): String = formattedMessage("exception.invalid-argument.parameter-errors.message")

    fun requestPageRequestParameterValidationInvalidSize(): String = formattedMessage("request.page-request.parameter-validation.invalid-size")

    fun requestPageRequestParameterValidationInvalidNumber(value: Any): String = formattedMessage("request.page-request.parameter-validation.invalid-number", value)

    fun requestPageRequestParameterValidationInvalidField(): String = formattedMessage("request.page-request.parameter-validation.invalid-field")

    fun requestPageRequestParameterValidationInvalidSort(value: Any): String = formattedMessage("request.page-request.parameter-validation.invalid-sort", value)

    fun requestRequestParameterValidationConvertError(field: Any, value: Any): String = formattedMessage("request.request.parameter-validation.convert-error", field, value)

    fun requestFilterChainAuthenticationEntrypointAccessDenied(): String = formattedMessage("request.filter-chain.authentication-entrypoint.access-denied")

    fun authenticationTokenRetrieveUserInactiveUser() = formattedMessage("authentication.token.retrieve-user.inactive-user")

    fun authenticationTokenValidateTokenExpiredToken() = formattedMessage("authentication.token.validate-token.expired-token")

    fun authenticationTokenValidateTokenDecodeError() = formattedMessage("authentication.token.validate-token.decode-error")

    fun authenticationTokenValidateTokenInvalidSignature() = formattedMessage("authentication.token.validate-token.invalid-signature")

    fun authenticationTokenValidateTokenInvalidIssuer() = formattedMessage("authentication.token.validate-token.invalid-issuer")

    fun authenticationManagerValidateAuthenticationNoAuthentication() = formattedMessage("authentication.manager.validate-authentication.no-authentication")

    fun authorityRoleOrganizationRoleNotAuthorized(role: String) = formattedMessage("authority.role.organization-role.not-authorized", role)

    fun authorityRoleUnitRoleNotAuthorized(role: String) = formattedMessage("authority.role.unit-role.not-authorized", role)

}
