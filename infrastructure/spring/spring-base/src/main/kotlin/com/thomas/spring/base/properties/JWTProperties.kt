package com.thomas.spring.base.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "security.jwt")
data class JWTProperties(
    val privateKey: String = "",
    val publicKey: String = "",
    val issuerName: String = "",
    val jwtAudience: String = "",
    val jwtRealm: String = "",
    val jwtAlgorithm: String = "",
)
