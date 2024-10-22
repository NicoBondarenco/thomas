package com.thomas.management.domain.model.request

data class SignupRequest(
    val organizationData: SignupOrganizationRequest,
    val userData: SignupUserRequest,
)
