package com.thomas.management.domain

import com.thomas.management.domain.model.request.SignupRequest
import com.thomas.management.domain.model.response.SignupResponse

interface SignupService {

    suspend fun signup(request: SignupRequest): SignupResponse

}