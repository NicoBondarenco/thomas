package com.thomas.jwt.auth0.util

import com.thomas.jwt.configuration.JWTAlgorithmType.RSA256
import com.thomas.jwt.configuration.JWTConfiguration
import com.thomas.jwt.configuration.JWTEncryptionType.RSA


internal const val USER_COLLECTION_NAME = "user_collection"
internal const val GROUP_COLLECTION_NAME = "group_collection"

internal val defaultConfiguration = JWTConfiguration(
    encryptionType = RSA,
    algorithmType = RSA256,
    privateKey = "privateKey",
    publicKey = "publicKey",
    issuerName = "test-authentication",
    jwtAudience = "test-service",
    jwtRealm = "test-service",
    customProperties = mapOf(
        "securityUserCollection" to USER_COLLECTION_NAME,
        "securityGroupCollection" to GROUP_COLLECTION_NAME,
    ),
)

internal val noCollectionConfiguration = defaultConfiguration.copy(
    customProperties = mapOf()
)

internal val authenticatorConfiguration = defaultConfiguration.copy(
    privateKey = JWTConfiguration::class.readResourceText("jwt-keys/test-private.key"),
    publicKey = JWTConfiguration::class.readResourceText("jwt-keys/test-public.key"),
)
