package com.thomas.management.domain.model.response

data class SignupResponse(
    val organizationData: SignupOrganizationResponse,
    val userData: SignupUserResponse,
)
