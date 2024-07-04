package com.thomas.management.domain.i18n

import com.thomas.core.i18n.BundleResolver
import java.util.UUID

object ManagementDomainMessageI18N : BundleResolver("strings/management-domain") {

    fun managementUserSearchNotFoundErrorMessage(id: UUID) =
        getFormattedMessage("management.user-search.not-found.error-message", id)

    fun managementUserValidationUserDataInvalidData() =
        getFormattedMessage("management.user-validation.user-data.invalid-data")

    fun managementUserValidationMainEmailAlreadyUsed() =
        getFormattedMessage("management.user-validation.main-email.already-used")

    fun managementUserValidationDocumentNumberAlreadyUsed() =
        getFormattedMessage("management.user-validation.document-number.already-used")

    fun managementUserValidationPhoneNumberAlreadyUsed() =
        getFormattedMessage("management.user-validation.phone-number.already-used")

    fun managementUserValidationUserGroupsNotFound(ids: String) =
        getFormattedMessage("management.user-validation.user-groups.not-found", ids)

}
