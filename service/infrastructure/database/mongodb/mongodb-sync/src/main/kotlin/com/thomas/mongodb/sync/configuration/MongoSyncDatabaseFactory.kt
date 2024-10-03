package com.thomas.mongodb.sync.configuration

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import com.thomas.mongodb.configuration.MongoDatabaseFactory
import com.thomas.mongodb.properties.MongoDatabaseProperties

object MongoSyncDatabaseFactory : MongoDatabaseFactory<MongoDatabase>() {

    override fun create(
        properties: MongoDatabaseProperties
    ): MongoDatabase = properties.database()

    private fun MongoDatabaseProperties.database() =
        mongoClients().getDatabase(this.databaseName)

    private fun MongoDatabaseProperties.mongoClients() =
        MongoClients.create(mongoClientSettings())

}
