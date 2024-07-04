package com.thomas.mongo.repository

import com.mongodb.ServerApiVersion
import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndReplaceOptions
import com.mongodb.client.model.ReturnDocument.AFTER
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection.ASC
import com.thomas.core.model.pagination.PageSortDirection.DESC
import com.thomas.mongo.configuration.MongoDatabaseFactory
import com.thomas.mongo.data.ChildTestEntity
import com.thomas.mongo.data.ParentTestEntity
import com.thomas.mongo.data.ParentTestRepository
import com.thomas.mongo.data.TestMongoEntity
import com.thomas.mongo.data.TestMongoRepository
import com.thomas.mongo.data.childTestList
import com.thomas.mongo.data.entityListFoundData
import com.thomas.mongo.data.entityListNotFoundData
import com.thomas.mongo.data.foundDateMax
import com.thomas.mongo.data.foundDateMin
import com.thomas.mongo.data.fullTestEntity
import com.thomas.mongo.data.parentTestEntity
import com.thomas.mongo.properties.MongoDatabaseProperties
import java.time.Duration
import java.time.ZoneOffset.UTC
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.awaitility.kotlin.atMost
import org.awaitility.kotlin.await
import org.awaitility.kotlin.until
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.testcontainers.containers.GenericContainer

@TestInstance(PER_CLASS)
class MongoRepositoryTest {

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
    private lateinit var testMongoRepository: TestMongoRepository
    private lateinit var parentTestRepository: ParentTestRepository
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

        mongoDatabase = MongoDatabaseFactory.create(properties)

        testMongoRepository = TestMongoRepository(mongoDatabase, testCollectionName)
        parentTestRepository = ParentTestRepository(mongoDatabase, parentCollectionName)

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
        runBlocking {
            testCollection.deleteMany(Filters.empty())
            parentCollection.deleteMany(Filters.empty())
            childCollection.deleteMany(Filters.empty())
        }
    }

    @Test
    fun `Retrieve multilevel entity`() {
        val entity = TestMongoEntity()
        runBlocking {
            testCollection.findOneAndReplace(
                Filters.eq("_id", entity.id),
                entity,
                FindOneAndReplaceOptions().upsert(true).returnDocument(AFTER)
            )!!
        }

        val result = testMongoRepository.one(entity.id)
        assertEquals(entity, result)
    }

    @Test
    fun `Retrieve multilevel entity list without filter`() {
        val entities = (1..10).map { TestMongoEntity() }
        runBlocking { testCollection.insertMany(entities) }

        val result = testMongoRepository.all()
        assertEquals(10, result.size)
    }

    @Test
    fun `Retrieve multilevel entity list with filter`() {
        val found = entityListFoundData
        val entities = found + entityListNotFoundData
        runBlocking { testCollection.insertMany(entities) }

        val result = testMongoRepository.all(
            found.map { it.stringValue },
            foundDateMin.atOffset(UTC),
            foundDateMax.atOffset(UTC),
        )
        assertEquals(found, result)
    }

    @Test
    fun `Retrieve multilevel entity page without filter and sort`() {
        val found = entityListFoundData
        val entities = found + entityListNotFoundData
        runBlocking { testCollection.insertMany(entities) }

        val result = testMongoRepository.page(
            PageRequest(2, 7)
        )
        assertEquals(7, result.contentList.size)
        assertEquals(entities.size.toLong(), result.totalItems)
    }

    @Test
    fun `Retrieve multilevel entity page without filter sorted ASC`() {
        val found = entityListFoundData
        val entities = found + entityListNotFoundData
        runBlocking { testCollection.insertMany(entities) }

        val first = entities.sortedBy { it.datetimeOffset }[10]

        val result = testMongoRepository.page(
            PageRequest(3, 5, listOf(PageSort("datetimeOffset", ASC))),
        )
        assertEquals(5, result.contentList.size)
        assertEquals(entities.size.toLong(), result.totalItems)
        assertEquals(first, result.contentList.first())
    }

    @Test
    fun `Retrieve multilevel entity page without filter sorted DESC`() {
        val entities = entityListFoundData + entityListNotFoundData
        runBlocking { testCollection.insertMany(entities) }

        val first = entities.sortedByDescending { it.stringValue }[0]

        val result = testMongoRepository.page(
            PageRequest(1, 3, listOf(PageSort("stringValue", DESC))),
        )
        assertEquals(3, result.contentList.size)
        assertEquals(entities.size.toLong(), result.totalItems)
        assertEquals(first, result.contentList.first())
    }

    @Test
    fun `Retrieve multilevel entity page with filter and without sort`() {
        val found = entityListFoundData
        val entities = found + entityListNotFoundData
        runBlocking { testCollection.insertMany(entities) }

        val result = testMongoRepository.page(
            found.map { it.stringValue },
            foundDateMin.atOffset(UTC),
            foundDateMax.atOffset(UTC),
            PageRequest(4, 6),
        )
        assertEquals(2, result.contentList.size)
        assertEquals(entityListFoundData.size.toLong(), result.totalItems)
    }

    @Test
    fun `Retrieve multilevel entity page with filter sorted ASC`() {
        val found = entityListFoundData
        val entities = found + entityListNotFoundData
        runBlocking {
            testCollection.insertMany(entities)
        }

        val first = found.sortedBy { it.bigInteger }[6]

        val result = testMongoRepository.page(
            found.map { it.stringValue },
            foundDateMin.atOffset(UTC),
            foundDateMax.atOffset(UTC),
            PageRequest(2, 6, listOf(PageSort("bigInteger", ASC))),
        )
        assertEquals(6, result.contentList.size)
        assertEquals(found.size.toLong(), result.totalItems)
        assertEquals(first, result.contentList.first())
    }

    @Test
    fun `Retrieve multilevel entity page with filter sorted DESC`() {
        val found = entityListFoundData
        val entities = found + entityListNotFoundData
        runBlocking { testCollection.insertMany(entities) }

        val first = found.sortedBy { it.bigDecimal }[12]

        val result = testMongoRepository.page(
            found.map { it.stringValue },
            foundDateMin.atOffset(UTC),
            foundDateMax.atOffset(UTC),
            PageRequest(4, 4, listOf(PageSort("bigDecimal", ASC))),
        )
        assertEquals(4, result.contentList.size)
        assertEquals(found.size.toLong(), result.totalItems)
        assertEquals(first, result.contentList.first())
    }

    @Test
    fun `Retrieve multilevel entity page empty`() {
        val entities = entityListFoundData + entityListNotFoundData
        runBlocking { testCollection.insertMany(entities) }

        val result = testMongoRepository.page(
            entityListFoundData.map { it.stringValue },
            foundDateMin.atOffset(UTC).plusDays(20),
            foundDateMax.atOffset(UTC).plusDays(20),
            PageRequest(),
        )
        assertTrue(result.contentList.isEmpty())
        assertEquals(0, result.totalItems)
    }

    @Test
    fun `Save multilevel entity`() {
        val entity = TestMongoEntity()
        val another = TestMongoEntity()
        testMongoRepository.save(entity.id, entity)
        testMongoRepository.save(another.id, another)

        val result = runBlocking { testCollection.find(Filters.eq("_id", entity.id)).firstOrNull() }
        assertEquals(entity, result)
    }

    @Test
    fun `Update multilevel entity`() {
        val saved = TestMongoEntity()
        runBlocking { testCollection.insertMany(listOf(saved, TestMongoEntity())) }

        val entity = TestMongoEntity(id = saved.id)
        val result = testMongoRepository.save(saved.id, entity)
        assertEquals(entity, result)

        val found = runBlocking { testCollection.find(Filters.eq("_id", entity.id)).firstOrNull() }
        assertEquals(entity, found)
    }

    @Test
    fun `Find joined entity`() {
        runBlocking {
            childCollection.insertMany(childTestList)
            parentCollection.insertOne(parentTestEntity)
        }

        val result = parentTestRepository.findFullTestEntity(parentTestEntity.id)
        assertEquals(fullTestEntity, result)
    }

}
