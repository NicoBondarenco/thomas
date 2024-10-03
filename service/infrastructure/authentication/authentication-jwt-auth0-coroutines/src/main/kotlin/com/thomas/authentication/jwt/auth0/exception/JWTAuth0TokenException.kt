package com.thomas.authentication.jwt.auth0.exception

import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.UNAUTHORIZED_ACTION

class JWTAuth0TokenException : DetailedException {

    constructor(message: String) : super(message, UNAUTHORIZED_ACTION)

    constructor(message: String, cause: Throwable) : super(message, UNAUTHORIZED_ACTION, null, cause)

}
