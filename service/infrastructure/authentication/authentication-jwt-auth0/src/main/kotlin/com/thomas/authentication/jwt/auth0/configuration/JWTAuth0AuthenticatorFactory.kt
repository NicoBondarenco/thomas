package com.thomas.authentication.jwt.auth0.configuration

import com.mongodb.client.MongoDatabase
import com.thomas.authentication.jwt.auth0.JWTAuth0Authenticator
import com.thomas.authentication.jwt.auth0.properties.JWTAuth0Properties
import com.thomas.authentication.jwt.auth0.repository.UserAuthenticationRepository

object JWTAuth0AuthenticatorFactory {

    fun create(
        database: MongoDatabase,
        properties: JWTAuth0Properties,
    ) = JWTAuth0Authenticator(
        properties,
        createRepository(database, properties),
    )

    private fun createRepository(
        database: MongoDatabase,
        properties: JWTAuth0Properties,
    ) = UserAuthenticationRepository(database, properties)

}
