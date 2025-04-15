package com.thomas.spring.base.authentication

import com.thomas.core.model.security.SecurityUser

interface Authenticator {

    fun authenticate(token: String): SecurityUser

}
