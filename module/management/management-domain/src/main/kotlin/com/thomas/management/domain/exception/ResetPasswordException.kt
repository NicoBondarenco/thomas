package com.thomas.management.domain.exception

import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType
import com.thomas.core.exception.ErrorType.INVALID_PARAMETER
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementResetPasswordResetTokenExpiredToken
import com.thomas.management.domain.i18n.ManagementDomainMessageI18N.managementResetPasswordResetTokenInvalidToken

class ResetPasswordException(
    message: String,
    type: ErrorType,
) : DetailedException(
    message = message,
    type = type,
) {

    companion object {

        fun invalidToken() = ResetPasswordException(
            message = managementResetPasswordResetTokenInvalidToken(),
            type = INVALID_PARAMETER
        )

        fun expiredToken() = ResetPasswordException(
            message = managementResetPasswordResetTokenExpiredToken(),
            type = INVALID_PARAMETER
        )

    }

}
