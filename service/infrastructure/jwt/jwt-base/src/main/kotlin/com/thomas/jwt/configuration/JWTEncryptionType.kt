package com.thomas.jwt.configuration

import java.security.KeyFactory

enum class JWTEncryptionType(
    val keyFactory: () -> KeyFactory
) {

    RSA({ KeyFactory.getInstance("RSA") }),

}
