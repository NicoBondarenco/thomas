package com.thomas.spring.base.exception

import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.UNAUTHORIZED_ACTION

class JWTTokenException : DetailedException {

    constructor(message: String, cause: Throwable) : super(message, UNAUTHORIZED_ACTION, null, cause)

}
