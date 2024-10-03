package com.thomas.mongodb.configuration

import com.mongodb.ConnectionString
import com.mongodb.DBObjectCodecProvider
import com.mongodb.DBRefCodecProvider
import com.mongodb.DocumentToDBRefTransformer
import com.mongodb.Jep395RecordCodecProvider
import com.mongodb.KotlinCodecProvider
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerApi
import com.mongodb.client.gridfs.codecs.GridFSFileCodecProvider
import com.mongodb.client.model.geojson.codecs.GeoJsonCodecProvider
import com.mongodb.client.model.mql.ExpressionCodecProvider
import com.thomas.mongodb.configuration.codec.BigIntegerCodec
import com.thomas.mongodb.configuration.codec.OffsetDateTimeCodec
import com.thomas.mongodb.configuration.codec.UUIDCodec
import com.thomas.mongodb.properties.MongoDatabaseProperties
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
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.jsr310.Jsr310CodecProvider
import org.bson.codecs.pojo.PojoCodecProvider

abstract class MongoDatabaseFactory<T>{

    companion object {
        protected const val ADMIN_DATABASE = "admin"
    }


    protected val codecProviders: CodecRegistry = fromProviders(
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

    protected val customCodecs: CodecRegistry = fromCodecs(
        BigIntegerCodec(),
        UUIDCodec(),
        OffsetDateTimeCodec(),
    )

    protected val codecRegistries: CodecRegistry = fromRegistries(
        customCodecs,
        codecProviders,
    )

    protected fun MongoDatabaseProperties.mongoClientSettings(): MongoClientSettings =
        MongoClientSettings.builder()
            .applyConnectionString(connectionString())
            .credential(mongoCredential())
            .serverApi(serverApi())
            .codecRegistry(codecRegistries)
            .build()

    protected fun MongoDatabaseProperties.connectionString(): ConnectionString =
        ConnectionString(this.connectionUrl)

    protected fun MongoDatabaseProperties.mongoCredential(): MongoCredential =
        MongoCredential.createCredential(
            this.connectionUsername,
            ADMIN_DATABASE,
            this.connectionPassword.toCharArray()
        )

    protected fun MongoDatabaseProperties.serverApi(): ServerApi =
        ServerApi.builder()
            .version(this.apiVersion)
            .build()

    abstract fun create(properties: MongoDatabaseProperties): T

}
