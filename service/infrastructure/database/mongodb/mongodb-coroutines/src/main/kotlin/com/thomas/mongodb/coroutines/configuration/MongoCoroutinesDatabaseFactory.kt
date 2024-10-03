package com.thomas.mongodb.coroutines.configuration

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.thomas.mongodb.configuration.MongoDatabaseFactory
import com.thomas.mongodb.properties.MongoDatabaseProperties

object MongoCoroutinesDatabaseFactory : MongoDatabaseFactory<MongoDatabase>() {

    override fun create(properties: MongoDatabaseProperties): MongoDatabase = properties.database()

    private fun MongoDatabaseProperties.database() =
        mongoClients().getDatabase(this.databaseName)

    private fun MongoDatabaseProperties.mongoClients() =
        MongoClient.create(mongoClientSettings())

}
