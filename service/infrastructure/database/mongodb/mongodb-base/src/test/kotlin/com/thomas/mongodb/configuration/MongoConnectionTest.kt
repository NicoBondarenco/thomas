package com.thomas.mongodb.configuration

import com.mongodb.ServerApiVersion
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndReplaceOptions
import com.mongodb.client.model.ReturnDocument.AFTER
import com.thomas.mongodb.data.ChildTestEntity
import com.thomas.mongodb.data.ParentTestEntity
import com.thomas.mongodb.data.TestMongoEntity
import com.thomas.mongodb.properties.MongoDatabaseProperties
import java.time.Duration
import org.awaitility.kotlin.atMost
import org.awaitility.kotlin.await
import org.awaitility.kotlin.until
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.testcontainers.containers.GenericContainer

@TestInstance(PER_CLASS)
class MongoConnectionTest {

    private val database: String = "mongo_database"
    private val username: String = "mongo_username"
    private val password: String = "mongo_password"
    private val port: Int = 27017
    private val testCollectionName: String = "test-collection"
    private val parentCollectionName: String = "parent-collection"
    private val childCollectionName: String = "child-collection"

    private val container = GenericContainer("mongo:6.0.1")
        .withEnv("MONGO_INITDB_DATABASE", database)
        .withEnv("MONGO_INITDB_ROOT_USERNAME", username)
        .withEnv("MONGO_INITDB_ROOT_PASSWORD", password)
        .withExposedPorts(port)

    private lateinit var properties: MongoDatabaseProperties
    private lateinit var mongoDatabase: MongoDatabase
    private lateinit var testCollection: MongoCollection<TestMongoEntity>
    private lateinit var parentCollection: MongoCollection<ParentTestEntity>
    private lateinit var childCollection: MongoCollection<ChildTestEntity>

    @BeforeAll
    fun beforeAll() {
        container.start()

        await atMost Duration.ofSeconds(30) until { container.isRunning }

        properties = MongoDatabaseProperties(
            connectionUrl = "mongodb://${container.host}:${container.getMappedPort(port)}",
            databaseName = database,
            connectionUsername = username,
            connectionPassword = password,
            apiVersion = ServerApiVersion.V1,
        )

        mongoDatabase = TestMongoDatabaseFactory.create(properties)

        testCollection = mongoDatabase.getCollection(testCollectionName, TestMongoEntity::class.java)
        parentCollection = mongoDatabase.getCollection(parentCollectionName, ParentTestEntity::class.java)
        childCollection = mongoDatabase.getCollection(childCollectionName, ChildTestEntity::class.java)
    }

    @AfterAll
    fun afterAll() {
        container.stop()
        await atMost Duration.ofSeconds(30) until { !container.isRunning }
    }

    @AfterEach
    fun afterEach() {
        testCollection.deleteMany(Filters.empty())
        parentCollection.deleteMany(Filters.empty())
        childCollection.deleteMany(Filters.empty())
    }

    @Test
    fun `Insert and retrieve an entity`() {
        val entity = TestMongoEntity()
        testCollection.findOneAndReplace(
            Filters.eq("_id", entity.id),
            entity,
            FindOneAndReplaceOptions().upsert(true).returnDocument(AFTER)
        )!!

        val result = testCollection.find(Filters.eq("_id", entity.id)).firstOrNull()
        assertEquals(entity, result)
    }

}
