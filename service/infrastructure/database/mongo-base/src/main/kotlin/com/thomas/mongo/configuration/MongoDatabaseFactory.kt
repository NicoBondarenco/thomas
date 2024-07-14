package com.thomas.mongo.configuration

import com.mongodb.ConnectionString
import com.mongodb.DBObjectCodecProvider
import com.mongodb.DBRefCodecProvider
import com.mongodb.DocumentToDBRefTransformer
import com.mongodb.Jep395RecordCodecProvider
import com.mongodb.KotlinCodecProvider
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerApi
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import com.mongodb.client.gridfs.codecs.GridFSFileCodecProvider
import com.mongodb.client.model.geojson.codecs.GeoJsonCodecProvider
import com.mongodb.client.model.mql.ExpressionCodecProvider
import com.thomas.mongo.configuration.codec.BigIntegerCodec
import com.thomas.mongo.configuration.codec.OffsetDateTimeCodec
import com.thomas.mongo.configuration.codec.UUIDCodec
import com.thomas.mongo.properties.MongoDatabaseProperties
import org.bson.codecs.BsonCodecProvider
import org.bson.codecs.BsonValueCodecProvider
import org.bson.codecs.CollectionCodecProvider
import org.bson.codecs.DocumentCodecProvider
import org.bson.codecs.EnumCodecProvider
import org.bson.codecs.IterableCodecProvider
import org.bson.codecs.JsonObjectCodecProvider
import org.bson.codecs.MapCodecProvider
import org.bson.codecs.ValueCodecProvider
import org.bson.codecs.configuration.CodecRegistries.fromCodecs
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.jsr310.Jsr310CodecProvider
import org.bson.codecs.pojo.PojoCodecProvider

object MongoDatabaseFactory {

    private const val ADMIN_DATABASE = "admin"

    private val codecProviders = fromProviders(
        ValueCodecProvider(),
        BsonValueCodecProvider(),
        DBRefCodecProvider(),
        DBObjectCodecProvider(),
        DocumentCodecProvider(DocumentToDBRefTransformer()),
        CollectionCodecProvider(DocumentToDBRefTransformer()),
        IterableCodecProvider(DocumentToDBRefTransformer()),
        MapCodecProvider(DocumentToDBRefTransformer()),
        GeoJsonCodecProvider(),
        GridFSFileCodecProvider(),
        Jsr310CodecProvider(),
        JsonObjectCodecProvider(),
        BsonCodecProvider(),
        EnumCodecProvider(),
        ExpressionCodecProvider(),
        Jep395RecordCodecProvider(),
        KotlinCodecProvider(),
        PojoCodecProvider.builder().automatic(true).build(),
    )

    private val customCodecs = fromCodecs(
        BigIntegerCodec(),
        UUIDCodec(),
        OffsetDateTimeCodec(),
    )

    private val codecRegistries = fromRegistries(
        customCodecs,
        codecProviders,
    )

    private fun MongoDatabaseProperties.database() =
        mongoClients().getDatabase(this.databaseName)

    private fun MongoDatabaseProperties.mongoClients() =
        MongoClients.create(mongoClientSettings())

    private fun MongoDatabaseProperties.mongoClientSettings() =
        MongoClientSettings.builder()
            .applyConnectionString(connectionString())
            .credential(mongoCredential())
            .serverApi(serverApi())
            .codecRegistry(codecRegistries)
            .build()

    private fun MongoDatabaseProperties.connectionString() =
        ConnectionString(this.connectionUrl)

    private fun MongoDatabaseProperties.mongoCredential() =
        MongoCredential.createCredential(
            this.connectionUsername,
            ADMIN_DATABASE,
            this.connectionPassword.toCharArray()
        )

    private fun MongoDatabaseProperties.serverApi() =
        ServerApi.builder()
            .version(this.apiVersion)
            .build()

    fun create(properties: MongoDatabaseProperties): MongoDatabase = properties.database()

}
