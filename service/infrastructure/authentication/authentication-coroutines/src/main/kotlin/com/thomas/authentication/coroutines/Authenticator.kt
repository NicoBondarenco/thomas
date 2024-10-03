package com.thomas.authentication.coroutines

import com.thomas.core.model.security.SecurityUser

interface Authenticator {

    suspend fun authenticate(token: String): SecurityUser

    suspend fun decode(token: String): SecurityUser

}
