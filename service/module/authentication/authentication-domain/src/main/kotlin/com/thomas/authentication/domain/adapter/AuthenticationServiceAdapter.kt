package com.thomas.authentication.domain.adapter

import com.thomas.authentication.domain.AuthenticationService
import com.thomas.authentication.domain.model.request.LoginRequest
import com.thomas.authentication.domain.model.response.AccessTokenResponse

class AuthenticationServiceAdapter : AuthenticationService {

    override fun login(
        request: LoginRequest,
    ): AccessTokenResponse {
        TODO("Not yet implemented")
    }

}
