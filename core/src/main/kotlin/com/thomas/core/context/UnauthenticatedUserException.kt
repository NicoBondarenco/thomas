package com.thomas.core.context

import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.UNAUTHENTICATED_USER
import com.thomas.core.i18n.CoreMessageI18N.contextCurrentSessionCurrentUserNotLogged

class UnauthenticatedUserException(
    message: String = contextCurrentSessionCurrentUserNotLogged()
) : DetailedException(
    message = message,
    type = UNAUTHENTICATED_USER,
)
