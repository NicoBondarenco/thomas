package com.thomas.authentication.jwt.auth0.repository

import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.thomas.authentication.jwt.auth0.data.UserAuthentication
import com.thomas.authentication.jwt.auth0.properties.JWTAuth0Properties
import com.thomas.mongodb.repository.MongoRepository
import java.util.UUID

internal class UserAuthenticationRepository(
    database: MongoDatabase,
    configuration: JWTAuth0Properties
) : MongoRepository<UserAuthentication>(
    database,
    configuration.userCollection,
    UserAuthentication::class,
) {
    private val groupCollectionName = configuration.groupCollection

    companion object {
        private const val GROUP_AGGREGATOR_NAME = "userGroups"
        private const val GROUP_PROPERTY_NAME = "groupsIds"
    }

    fun findUserAuthentication(
        id: UUID,
    ): UserAuthentication? = collection.aggregate(
        listOf(
            Aggregates.lookup(
                groupCollectionName,
                GROUP_PROPERTY_NAME,
                OBJECT_ID_PARAMETER_NAME,
                GROUP_AGGREGATOR_NAME
            ),
            Aggregates.match(
                Filters.eq(OBJECT_ID_PARAMETER_NAME, id),
            ),
        )
    ).firstOrNull()

}
