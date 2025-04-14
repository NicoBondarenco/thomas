package com.thomas.management.spring.controller

object ManagementPath {

    private const val PRIVATE_API_V1_PREFIX = "/api/v1/management"
    private const val PUBLIC_API_V1_PREFIX = "/public$PRIVATE_API_V1_PREFIX"

    const val PUBLIC_API_V1_SIGNUP = "$PUBLIC_API_V1_PREFIX/signup"



    const val PUBLIC_API_V1_USERS = "$PUBLIC_API_V1_PREFIX/users"
    const val PRIVATE_API_V1_USERS = "$PRIVATE_API_V1_PREFIX/users"

    const val PRIVATE_API_V1_GROUPS = "$PRIVATE_API_V1_PREFIX/groups"

}
