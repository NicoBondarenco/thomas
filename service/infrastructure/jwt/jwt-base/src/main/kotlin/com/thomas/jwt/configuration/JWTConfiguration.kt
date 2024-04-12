package com.thomas.jwt.configuration

data class JWTConfiguration (
    val encryptionType: JWTEncryptionType,
    val algorithmType: JWTAlgorithmType,
    val privateKey: String,
    val publicKey: String,
    val issuerName: String,
    val jwtAudience: String,
    val jwtRealm: String,
    val customProperties: Map<String, Any?> = mapOf(),
)
