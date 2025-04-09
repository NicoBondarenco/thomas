package com.thomas.spring.base.exception

import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.UNAUTHORIZED_ACTION

class AuthorityException : DetailedException {

    constructor(message: String) : super(message, UNAUTHORIZED_ACTION)

}