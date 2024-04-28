package com.thomas.authentication.jwt.auth0.exception

class JWTAuth0TokenException : RuntimeException {

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)

}
