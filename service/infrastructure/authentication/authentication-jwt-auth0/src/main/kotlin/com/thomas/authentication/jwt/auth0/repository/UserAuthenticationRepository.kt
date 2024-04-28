package com.thomas.authentication.jwt.auth0.repository

import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.thomas.authentication.jwt.auth0.configuration.JWTAuth0Configuration
import com.thomas.authentication.jwt.auth0.data.UserAuthentication
import com.thomas.mongo.repository.MongoRepository
import java.util.UUID
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

class UserAuthenticationRepository(
    database: MongoDatabase,
    configuration: JWTAuth0Configuration
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

    fun findUserAuthentication(id: UUID): UserAuthentication? = runBlocking {
        collection.aggregate(
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

}
