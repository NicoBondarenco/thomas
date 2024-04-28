package com.thomas.authentication

import com.thomas.core.model.security.SecurityUser

interface Authenticator {

    fun authenticate(token: String): SecurityUser

    fun decode(token: String): SecurityUser

}
