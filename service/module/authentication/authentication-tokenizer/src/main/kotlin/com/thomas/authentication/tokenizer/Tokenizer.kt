package com.thomas.authentication.tokenizer

import com.thomas.core.model.security.SecurityUser

interface Tokenizer {

    fun accessToken(securityUser: SecurityUser, durationSeconds: Long): String

//    fun refreshToken(securityUser: SecurityUser, durationSeconds: Long): String

}
