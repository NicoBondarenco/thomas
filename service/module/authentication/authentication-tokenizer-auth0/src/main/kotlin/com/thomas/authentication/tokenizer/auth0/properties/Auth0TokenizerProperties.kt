package com.thomas.authentication.tokenizer.auth0.properties

data class Auth0TokenizerProperties(
    val privateKey: String = "",
    val publicKey: String = "",
    val issuerName: String = "",
    val jwtAudience: String = "",
    val jwtRealm: String = "",
    val jwtAlgorithm: String = "",
)
