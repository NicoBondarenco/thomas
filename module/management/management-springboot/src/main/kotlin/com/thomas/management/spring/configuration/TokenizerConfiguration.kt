package com.thomas.management.spring.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.thomas.management.domain.crypt.Tokenizer
import com.thomas.management.spring.configuration.properties.TokenProperties
import com.thomas.management.spring.crypt.auth0.Auth0Tokenizer
import com.thomas.spring.base.properties.JWTProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(TokenProperties::class)
class TokenizerConfiguration {

    @Bean
    fun tokenizer(
        jwtProperties: JWTProperties,
        tokenProperties: TokenProperties,
        objectMapper: ObjectMapper,
    ): Tokenizer = Auth0Tokenizer(jwtProperties, tokenProperties, objectMapper)

}