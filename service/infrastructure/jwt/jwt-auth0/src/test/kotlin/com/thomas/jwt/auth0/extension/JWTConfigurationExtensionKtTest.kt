package com.thomas.jwt.auth0.extension

import com.thomas.jwt.auth0.Auth0JWTConfigurationException
import com.thomas.jwt.auth0.util.GROUP_COLLECTION_NAME
import com.thomas.jwt.auth0.util.USER_COLLECTION_NAME
import com.thomas.jwt.auth0.util.defaultConfiguration
import com.thomas.jwt.auth0.util.noCollectionConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class JWTConfigurationExtensionKtTest {

    @Test
    fun `User Collection Name is Set`() {
        assertEquals(USER_COLLECTION_NAME, defaultConfiguration.securityUserCollectionName())
    }

    @Test
    fun `User Collection Name is NOT Set`() {
        assertThrows<Auth0JWTConfigurationException> { noCollectionConfiguration.securityUserCollectionName() }
    }

    @Test
    fun `Group Collection Name is Set`() {
        assertEquals(GROUP_COLLECTION_NAME, defaultConfiguration.securityGroupCollectionName())
    }

    @Test
    fun `Group Collection Name is NOT Set`() {
        assertThrows<Auth0JWTConfigurationException> { noCollectionConfiguration.securityGroupCollectionName() }
    }

}
