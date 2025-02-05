package com.thomas.management.domain

import com.thomas.management.domain.model.request.LoginRequest
import com.thomas.management.domain.model.request.RefreshTokenRequest
import com.thomas.management.domain.model.response.AccessTokenResponse

interface AuthenticationService {

    suspend fun login(request: LoginRequest): AccessTokenResponse

    suspend fun refresh(request: RefreshTokenRequest): AccessTokenResponse

}