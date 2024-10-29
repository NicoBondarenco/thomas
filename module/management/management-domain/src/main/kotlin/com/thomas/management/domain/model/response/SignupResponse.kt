package com.thomas.management.domain.model.response

data class SignupResponse(
    val organizationData: OrganizationResponse,
    val userData: SignupUserResponse,
)
