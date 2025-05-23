package com.thomas.spring.base.exception

import com.thomas.core.exception.ApplicationException
import com.thomas.core.exception.ErrorType.UNAUTHORIZED_ACTION

class AuthorityException : ApplicationException {

    constructor(message: String) : super(message, UNAUTHORIZED_ACTION)

}