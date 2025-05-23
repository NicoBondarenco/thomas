package com.thomas.spring.base.exception

import com.thomas.core.exception.ApplicationException
import com.thomas.core.exception.ErrorType.UNAUTHORIZED_ACTION

class JWTTokenException : ApplicationException {

    constructor(message: String) : super(message, UNAUTHORIZED_ACTION, null, null)

    constructor(message: String, cause: Throwable) : super(message, UNAUTHORIZED_ACTION, null, cause)

}
