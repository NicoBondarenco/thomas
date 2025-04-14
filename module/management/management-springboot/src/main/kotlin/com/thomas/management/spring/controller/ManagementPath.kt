package com.thomas.management.spring.controller

object ManagementPath {

    private const val PRIVATE_API_V1_PREFIX = "/api/v1/management"
    private const val PUBLIC_API_V1_PREFIX = "/public$PRIVATE_API_V1_PREFIX"

    const val PUBLIC_API_V1_SIGNUP = "$PUBLIC_API_V1_PREFIX/signup"

    const val PUBLIC_API_V1_AUTHENTICATION = "$PUBLIC_API_V1_PREFIX/authentication"
    const val PUBLIC_API_V1_AUTHENTICATION_LOGIN = "/login"
    const val PUBLIC_API_V1_AUTHENTICATION_REFRESH = "/refresh"

    const val PRIVATE_API_V1_ORGANIZATION = "$PRIVATE_API_V1_PREFIX/organizations"

}
