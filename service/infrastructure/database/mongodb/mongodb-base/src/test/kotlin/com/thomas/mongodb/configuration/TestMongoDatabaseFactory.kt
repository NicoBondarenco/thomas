package com.thomas.mongodb.configuration

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import com.thomas.mongodb.properties.MongoDatabaseProperties

object TestMongoDatabaseFactory: MongoDatabaseFactory<MongoDatabase>() {

    override fun create(
        properties: MongoDatabaseProperties
    ): MongoDatabase = properties.database()

    private fun MongoDatabaseProperties.database() =
        mongoClients().getDatabase(this.databaseName)

    private fun MongoDatabaseProperties.mongoClients() =
        MongoClients.create(mongoClientSettings())

}