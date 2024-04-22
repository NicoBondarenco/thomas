package com.thomas.jwt.auth0.extension

import com.thomas.jwt.auth0.Auth0JWTConfigurationException
import com.thomas.jwt.configuration.JWTConfiguration
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

private const val SECURITY_USER_COLLECTION_PROPERTY = "securityUserCollection"
private const val GROUP_USER_COLLECTION_PROPERTY = "securityGroupCollection"

fun JWTConfiguration.securityUserCollectionName() = SECURITY_USER_COLLECTION_PROPERTY.let {
    this.customProperties[it]?.toString()
        ?: throw Auth0JWTConfigurationException("Property $it not set")
}

fun JWTConfiguration.securityGroupCollectionName() = GROUP_USER_COLLECTION_PROPERTY.let {
    this.customProperties[it]?.toString()
        ?: throw Auth0JWTConfigurationException("Property $it not set")
}

fun JWTConfiguration.privateKeySpec() = PKCS8EncodedKeySpec(
    Base64.getDecoder().decode(privateKeyText())
)

fun JWTConfiguration.publicKeySpec() = X509EncodedKeySpec(
    Base64.getDecoder().decode(publicKeyText())
)

private fun JWTConfiguration.privateKeyText() = keyText(this.privateKey)

private fun JWTConfiguration.publicKeyText() = keyText(this.publicKey)

private fun keyText(key: String) = key.extractKey()

private fun String.extractKey() = this
    .replace(Char(65533).toString(), "")
    .replace(Char(0).toString(), "")
    .replace("\r\n".toRegex(), "")
    .replace("\n".toRegex(), "")
    .replace("\r".toRegex(), "")
    .replace("-----BEGIN PRIVATE KEY-----", "")
    .replace("-----BEGIN PUBLIC KEY-----", "")
    .replace("-----END PRIVATE KEY-----", "")
    .replace("-----END PUBLIC KEY-----", "")