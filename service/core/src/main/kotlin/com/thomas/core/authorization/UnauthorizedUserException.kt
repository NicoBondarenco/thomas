package com.thomas.core.authorization

import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.UNAUTHORIZED_ACTION

class UnauthorizedUserException(
    message: String
) : DetailedException(
    message = message,
    type = UNAUTHORIZED_ACTION,
)
