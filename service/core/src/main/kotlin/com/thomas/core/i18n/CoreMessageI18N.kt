package com.thomas.core.i18n

object CoreMessageI18N : BundleResolver("strings/core-strings") {

    fun contextCurrentSessionCurrentUserNotLogged(): String = getFormattedMessage("context.current-session.current-user.not-logged")

    fun contextCurrentSessionCurrentUserNotAllowed(): String = getFormattedMessage("context.current-session.current-user.not-allowed")

    fun exceptionExceptionResponseErrorMessageDefaultMessage(): String = getFormattedMessage(
        "exception.exception-response.error-message.default-message"
    )

    fun validationEntityValidationInvalidErrorMessage(): String = getFormattedMessage("validation.entity-validation.invalid.error-message")

}
