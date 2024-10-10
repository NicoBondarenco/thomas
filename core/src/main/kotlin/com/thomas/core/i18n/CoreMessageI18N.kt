package com.thomas.core.i18n

internal object CoreMessageI18N : BundleResolver("strings/core-strings") {

    fun contextCurrentSessionCurrentUserNotLogged(): String = formattedMessage("context.current-session.current-user.not-logged")

    fun contextCurrentSessionCurrentOrganizationUnresolvedOrganization(): String = formattedMessage("context.current-session.current-organization.unresolved-organization")

    fun contextCurrentSessionCurrentUserNotAllowed(): String = formattedMessage("context.current-session.current-user.not-allowed")

    fun exceptionDetailedExceptionMessageDefault(): String = formattedMessage("exception.detailed-exception.message.default")

    fun validationEntityValidationInvalidErrorMessage(): String = formattedMessage("validation.entity-validation.invalid.error-message")

}
