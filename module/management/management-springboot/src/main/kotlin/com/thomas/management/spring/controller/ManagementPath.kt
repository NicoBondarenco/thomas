package com.thomas.management.spring.controller

object ManagementPath {

    private const val PRIVATE_API_V1_PREFIX = "/api/v1/management"
    private const val PUBLIC_API_V1_PREFIX = "/public$PRIVATE_API_V1_PREFIX"

    const val PUBLIC_API_V1_SIGNUP = "$PUBLIC_API_V1_PREFIX/signup"

    const val PUBLIC_API_V1_AUTHENTICATION = "$PUBLIC_API_V1_PREFIX/authentication"
    const val PUBLIC_API_V1_AUTHENTICATION_LOGIN = "/login"
    const val PUBLIC_API_V1_AUTHENTICATION_REFRESH = "/refresh"

    const val PUBLIC_API_V1_PASSWORD = "$PUBLIC_API_V1_PREFIX/password"
    const val PUBLIC_API_V1_PASSWORD_FORGOT = "/forgot"
    const val PUBLIC_API_V1_PASSWORD_RESET = "/reset"

    const val PRIVATE_API_V1_ORGANIZATION = "$PRIVATE_API_V1_PREFIX/organizations"

    const val PRIVATE_API_V1_UNIT = "$PRIVATE_API_V1_PREFIX/units"

    const val PRIVATE_API_V1_GROUP = "$PRIVATE_API_V1_PREFIX/groups"

    const val PRIVATE_API_V1_USER = "$PRIVATE_API_V1_PREFIX/users"

    const val PRIVATE_API_V1_USER_PASSWORD = "/change-password"

}
