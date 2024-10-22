package com.thomas.management.domain

import com.thomas.management.domain.model.request.SignupRequest

interface SignupService {

    fun signup(request: SignupRequest)

}