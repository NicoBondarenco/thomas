package com.thomas.spring.configuration

import com.mongodb.client.MongoDatabase
import com.thomas.authentication.Authenticator
import com.thomas.authentication.jwt.auth0.configuration.JWTAuth0AuthenticatorFactory
import com.thomas.mongodb.configuration.MongoDatabaseFactory
import com.thomas.mongodb.sync.configuration.MongoSyncDatabaseFactory
import com.thomas.spring.properties.SecurityProperties
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(SecurityProperties::class)
class AuthenticationConfiguration {

    @Bean("authenticatorDatabase")
    fun authenticatorDatabase(
        securityProperties: SecurityProperties
    ): MongoDatabase = MongoSyncDatabaseFactory.create(securityProperties.database)

    @Bean
    fun authenticator(
        @Qualifier("authenticatorDatabase") authenticatorDatabase: MongoDatabase,
        securityProperties: SecurityProperties
    ): Authenticator = JWTAuth0AuthenticatorFactory.create(
        authenticatorDatabase,
        securityProperties.jwt,
    )

}
