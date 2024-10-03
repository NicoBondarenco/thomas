package com.thomas.spring.properties

import com.thomas.authentication.jwt.auth0.properties.JWTAuth0Properties
import com.thomas.mongodb.properties.MongoDatabaseProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "security")
data class SecurityProperties @ConstructorBinding constructor(
    val jwt: JWTAuth0Properties,
    val database: MongoDatabaseProperties,
)
