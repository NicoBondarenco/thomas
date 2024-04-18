package com.thomas.mongo.configuration

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoClientSettings.getDefaultCodecRegistry
import com.mongodb.MongoCredential
import com.mongodb.ServerApi
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import com.thomas.mongo.configuration.codec.BigIntegerCodec
import com.thomas.mongo.configuration.codec.OffsetDatetimeCodec
import com.thomas.mongo.configuration.codec.UUIDCodec
import com.thomas.mongo.properties.MongoDatabaseProperties
import org.bson.codecs.configuration.CodecRegistries.fromCodecs
import org.bson.codecs.configuration.CodecRegistries.fromRegistries

class MongoManager(
    properties: MongoDatabaseProperties
) {

    private val codecRegistry = fromRegistries(
        fromCodecs(
            BigIntegerCodec(),
            UUIDCodec(),
            OffsetDatetimeCodec(),
        ),
        getDefaultCodecRegistry()
    )

    val mongoDatabase: MongoDatabase

    init {
        val connectionString = ConnectionString(properties.connectionUrl)
        val serverApi = ServerApi.builder()
            .version(properties.apiVersion)
            .build()
        val mongoCredential = MongoCredential.createCredential(
            properties.connectionUsername,
            "admin",
            properties.connectionPassword.toCharArray()
        )
        val mongoSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .credential(mongoCredential)
            .serverApi(serverApi)
            .codecRegistry(codecRegistry)
            .build()
        val mongoClient = MongoClients.create(mongoSettings)
        mongoDatabase = mongoClient.getDatabase(properties.databaseName)
    }

}