package com.thomas.spring.configuration

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.thomas.authentication.Authenticator
import com.thomas.authentication.jwt.auth0.configuration.JWTAuth0AuthenticatorFactory
import com.thomas.authentication.jwt.auth0.properties.JWTAuth0Properties
import com.thomas.mongo.configuration.MongoDatabaseFactory
import com.thomas.mongo.properties.MongoDatabaseProperties
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthenticationConfiguration {

    @Bean
    @ConfigurationProperties("security.jwt")
    fun jwtConfiguration(): JWTAuth0Properties = JWTAuth0Properties()

    @Bean("authenticatorDatabaseProperties")
    @ConfigurationProperties("security.database")
    fun mongoDatabaseProperties(): MongoDatabaseProperties = MongoDatabaseProperties()

    @Bean("authenticatorDatabase")
    fun authenticatorDatabase(
        @Qualifier("authenticatorDatabaseProperties") mongoDatabaseProperties: MongoDatabaseProperties,
    ): MongoDatabase = MongoDatabaseFactory.create(mongoDatabaseProperties)

    @Bean
    fun authenticator(
        @Qualifier("authenticatorDatabase") authenticatorDatabase: MongoDatabase,
        jwtConfiguration: JWTAuth0Properties
    ): Authenticator = JWTAuth0AuthenticatorFactory.create(
        authenticatorDatabase,
        jwtConfiguration,
    )

}
