package com.thomas.authentication.tokenizer.auth0

import com.fasterxml.jackson.module.kotlin.readValue
import com.thomas.authentication.tokenizer.auth0.data.defaultConfiguration
import com.thomas.authentication.tokenizer.auth0.data.defaultUser
import com.thomas.authentication.tokenizer.auth0.data.mapper
import com.thomas.core.model.security.SecurityUser
import java.util.Base64
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.RepeatedTest

class Auth0TokenizerTest {

    private val tokenizer = Auth0Tokenizer(defaultConfiguration)
    private val duration = 3600L
    private val decoder = Base64.getUrlDecoder()

    @RepeatedTest(100)
    fun `Generate valid token`() {
        val user = defaultUser

        val token = tokenizer.accessToken(user, duration)

        val parts = token.split(".")

        assertEquals("{\"alg\":\"RS256\",\"typ\":\"JWT\"}", String(decoder.decode(parts[0])))

        val body = String(decoder.decode(parts[1]))
        val result = mapper.readValue<SecurityUser>(body).copy(isActive = user.isActive)
        assertEquals(user, result)
    }

}