package com.thomas.management.domain.util

import com.thomas.management.domain.model.request.SignupRequest

internal val signupRequest: SignupRequest
    get() = SignupRequest(
        organizationData = signupOrganizationRequest,
        userData = signupUserRequest,
    )