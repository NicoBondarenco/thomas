package com.thomas.authentication.jwt.auth0.properties

data class JWTAuth0Properties(
    val privateKey: String = "",
    val publicKey: String = "",
    val issuerName: String = "",
    val jwtAudience: String = "",
    val jwtRealm: String = "",
    val jwtAlgorithm: String = "",
    val userCollection: String = "",
    val groupCollection: String = "",
)
