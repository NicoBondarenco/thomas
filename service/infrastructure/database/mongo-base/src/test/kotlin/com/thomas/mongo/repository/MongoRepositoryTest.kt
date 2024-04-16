package com.thomas.mongo.repository

import com.mongodb.ServerApiVersion
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import com.thomas.mongo.configuration.MongoManager
import com.thomas.mongo.configuration.parser.MongoParameterParserException
import com.thomas.mongo.data.TestErrorRepository
import com.thomas.mongo.data.TestMongoEntity
import com.thomas.mongo.data.TestMongoRepository
import com.thomas.mongo.extension.toUpsertDocument
import com.thomas.mongo.properties.MongoDatabaseProperties
import java.time.Duration
import java.time.ZoneOffset.UTC
import java.util.UUID
import org.awaitility.kotlin.atMost
import org.awaitility.kotlin.await
import org.awaitility.kotlin.until
import org.bson.Document
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.assertThrows
import org.testcontainers.containers.GenericContainer

@TestInstance(PER_CLASS)
class MongoRepositoryTest {

    private val database: String = "mongo_database"
    private val username: String = "mongo_username"
    private val password: String = "mongo_password"
    private val port: Int = 27017
    private val testCollectionName: String = "test-collection"
    private val errorCollectionName: String = "error-collection"

    private val container = GenericContainer("mongo:6.0.1")
        .withEnv("MONGO_INITDB_DATABASE", database)
        .withEnv("MONGO_INITDB_ROOT_USERNAME", username)
        .withEnv("MONGO_INITDB_ROOT_PASSWORD", password)
        .withExposedPorts(port)

    private lateinit var properties: MongoDatabaseProperties
    private lateinit var manager: MongoManager
    private lateinit var testMongoRepository: TestMongoRepository
    private lateinit var testErrorRepository: TestErrorRepository
    private lateinit var testCollection: MongoCollection<Document>
    private lateinit var errorCollection: MongoCollection<Document>

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

        manager = MongoManager(properties)

        testMongoRepository = TestMongoRepository(manager.mongoDatabase, testCollectionName)
        testErrorRepository = TestErrorRepository(manager.mongoDatabase, errorCollectionName)

        testCollection = manager.mongoDatabase.getCollection(testCollectionName)
        errorCollection = manager.mongoDatabase.getCollection(errorCollectionName)
    }

    @AfterAll
    fun afterAll() {
        container.stop()
        await atMost Duration.ofSeconds(30) until { !container.isRunning }
    }

    @Test
    fun `Save multilevel entity`() {
        val entity = TestMongoEntity()
        testMongoRepository.save(entity)

        val document = testCollection.find(Filters.eq("_id", entity.id.toString())).firstOrNull()
        assertNotNull(document)

    }

    @Test
    fun `Retrieve multilevel entity`() {
        val entity = TestMongoEntity()
        testCollection.updateOne(
            Filters.eq("_id", entity.id.toString()),
            entity.toUpsertDocument(),
            UpdateOptions().upsert(true)
        )

        val result = testMongoRepository.findById(entity.id)
        assertNotNull(result)
        assertEquals(entity.stringValue, result!!.stringValue)
        assertEquals(entity.booleanValue, result.booleanValue)
        assertEquals(entity.intValue, result.intValue)
        assertEquals(entity.longValue, result.longValue)
        assertEquals(entity.doubleValue, result.doubleValue)
        assertEquals(entity.bigDecimal, result.bigDecimal)
        assertEquals(entity.bigInteger, result.bigInteger)
        assertEquals(entity.dateValue, result.dateValue)
        assertEquals(
            entity.timeValue.withNano(0),
            result.timeValue.withNano(0)
        )
        assertEquals(
            entity.datetimeValue.withNano(0),
            result.datetimeValue.withNano(0)
        )
        assertEquals(
            entity.datetimeOffset.withNano(0).withOffsetSameInstant(UTC),
            result.datetimeOffset.withNano(0).withOffsetSameInstant(UTC)
        )
        assertEquals(entity.enumValue, result.enumValue)
        assertEquals(entity.stringList, result.stringList)
        assertEquals(entity.uuidEmpty, result.uuidEmpty)
        assertEquals(entity.stringEmpty, result.stringEmpty)
        assertEquals(entity.booleanEmpty, result.booleanEmpty)
        assertEquals(entity.intEmpty, result.intEmpty)
        assertEquals(entity.longEmpty, result.longEmpty)
        assertEquals(entity.doubleEmpty, result.doubleEmpty)
        assertEquals(entity.bigdecimalEmpty, result.bigdecimalEmpty)
        assertEquals(entity.bigintegerEmpty, result.bigintegerEmpty)
        assertEquals(entity.dateEmpty, result.dateEmpty)
        assertEquals(entity.timeEmpty, result.timeEmpty)
        assertEquals(entity.datetimeEmpty, result.datetimeEmpty)
        assertEquals(entity.offsetEmpty, result.offsetEmpty)
        assertEquals(entity.enumNull, result.enumNull)
        assertEquals(entity.childValue, result.childValue)
        assertEquals(entity.stringsNull, result.stringsNull)
        assertEquals(entity.childrenNull, result.childrenNull)

        assertEquals(entity.childEntity.childName, result.childEntity.childName)
        assertEquals(entity.childEntity.anotherEntity, result.childEntity.anotherEntity)
        assertEquals(entity.childEntity.anotherEmpty, result.childEntity.anotherEmpty)
        assertEquals(entity.childEntity.anotherList, result.childEntity.anotherList)
        assertEquals(entity.childEntity.listEmpty, result.childEntity.listEmpty)
        assertEquals(entity.childEntity.listNull.filterNotNull(), result.childEntity.listNull.filterNotNull())

        entity.childList.forEach { child ->
            val childResult = result.childList.firstOrNull { it.id == child.id }
            assertNotNull(childResult)
            assertEquals(child.childName, childResult!!.childName)
            assertEquals(child.anotherEntity, childResult.anotherEntity)
            assertEquals(child.anotherEmpty, childResult.anotherEmpty)
            assertEquals(child.anotherList, childResult.anotherList)
            assertEquals(child.listEmpty, childResult.listEmpty)
            assertEquals(child.listNull.filterNotNull(), childResult.listNull.filterNotNull())
        }
    }

    @Test
    fun `Throw error when there is no parameter parser`(){
        val id = UUID.randomUUID()
        errorCollection.updateOne(
            Filters.eq("_id", id),
            Updates.combine(
                Updates.set("id", id),
                Updates.set("stringValue", "Test Name"),
                Updates.set("fileValue", "Test File"),
            ),
            UpdateOptions().upsert(true)
        )
        assertThrows<MongoParameterParserException> { testErrorRepository.findById(id) }
    }

}