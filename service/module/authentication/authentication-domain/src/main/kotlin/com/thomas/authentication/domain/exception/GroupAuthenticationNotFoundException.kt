package com.thomas.authentication.domain.exception

import com.thomas.authentication.domain.i18n.AuthenticationDomainMessageI18N.authenticationGroupAuthenticationNotFoundErrorMessage
import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.NOT_FOUND
import java.util.UUID

class GroupAuthenticationNotFoundException(
    id: UUID
) : DetailedException(
    message = authenticationGroupAuthenticationNotFoundErrorMessage(id),
    type = NOT_FOUND,
)
