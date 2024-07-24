package com.thomas.authentication.domain

import com.thomas.authentication.domain.model.request.LoginRequest
import com.thomas.authentication.domain.model.response.AccessTokenResponse

interface AuthenticationService {

    fun login(request: LoginRequest): AccessTokenResponse

}
