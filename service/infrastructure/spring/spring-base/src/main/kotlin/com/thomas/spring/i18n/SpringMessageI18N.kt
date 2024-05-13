package com.thomas.spring.i18n

import com.thomas.core.i18n.BundleResolver

internal object SpringMessageI18N : BundleResolver("strings/spring-strings") {

    fun exceptionThrowableMessageDefault(): String =
        getFormattedMessage("exception.throwable.message.default")

    fun exceptionInvalidArgumentParameterErrorsMessage(): String =
        getFormattedMessage("exception.invalid-argument.parameter-errors.message")

    fun requestPageRequestParameterValidationInvalidSize(): String =
        getFormattedMessage("request.page-request.parameter-validation.invalid-size")

    fun requestPageRequestParameterValidationInvalidNumber(value: Any): String =
        getFormattedMessage("request.page-request.parameter-validation.invalid-number", value)

    fun requestPageRequestParameterValidationInvalidField(): String =
        getFormattedMessage("request.page-request.parameter-validation.invalid-field")

    fun requestPageRequestParameterValidationInvalidSort(value: Any): String =
        getFormattedMessage("request.page-request.parameter-validation.invalid-sort", value)

    fun requestRequestParameterValidationConvertError(field: Any, value: Any): String =
        getFormattedMessage("request.request.parameter-validation.convert-error", field, value)

    fun requestFilterChainAuthenticationEntrypointAccessDenied(): String =
        getFormattedMessage("request.filter-chain.authentication-entrypoint.access-denied")

}
