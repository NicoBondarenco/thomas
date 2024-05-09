package com.thomas.core.context

import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.UNAUTHENTICATED_USER

class UnauthenticatedUserException(
    message: String
) : DetailedException(
    message = message,
    type = UNAUTHENTICATED_USER,
)