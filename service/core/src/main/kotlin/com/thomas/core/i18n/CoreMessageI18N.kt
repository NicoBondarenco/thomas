package com.thomas.core.i18n

internal object CoreMessageI18N : BundleResolver("strings/core-strings") {

    fun contextCurrentSessionCurrentUserNotLogged(): String = getFormattedMessage("context.current-session.current-user.not-logged")

    fun contextCurrentSessionCurrentUserNotAllowed(): String = getFormattedMessage("context.current-session.current-user.not-allowed")

    fun exceptionDetailedExceptionMessageDefault(): String = getFormattedMessage("exception.detailed-exception.message.default")

    fun validationEntityValidationInvalidErrorMessage(): String = getFormattedMessage("validation.entity-validation.invalid.error-message")

}
