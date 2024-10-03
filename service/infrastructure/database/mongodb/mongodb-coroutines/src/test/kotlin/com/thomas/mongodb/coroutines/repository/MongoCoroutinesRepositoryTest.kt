package com.thomas.mongodb.coroutines.repository

import com.mongodb.ServerApiVersion
import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndReplaceOptions
import com.mongodb.client.model.ReturnDocument.AFTER
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.thomas.core.model.pagination.PageRequestData
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection.ASC
import com.thomas.core.model.pagination.PageSortDirection.DESC
import com.thomas.mongodb.coroutines.configuration.MongoCoroutinesDatabaseFactory
import com.thomas.mongodb.coroutines.data.ChildTestEntity
import com.thomas.mongodb.coroutines.data.ParentTestEntity
import com.thomas.mongodb.coroutines.data.ParentTestRepository
import com.thomas.mongodb.coroutines.data.TestMongoEntity
import com.thomas.mongodb.coroutines.data.TestMongoRepository
import com.thomas.mongodb.coroutines.data.childTestList
import com.thomas.mongodb.coroutines.data.entityListFoundData
import com.thomas.mongodb.coroutines.data.entityListNotFoundData
import com.thomas.mongodb.coroutines.data.foundDateMax
import com.thomas.mongodb.coroutines.data.foundDateMin
import com.thomas.mongodb.coroutines.data.fullTestEntity
import com.thomas.mongodb.coroutines.data.parentTestEntity
import com.thomas.mongodb.properties.MongoDatabaseProperties
import java.time.Duration
import java.time.ZoneOffset.UTC
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
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
class MongoCoroutinesRepositoryTest {

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

        mongoDatabase = MongoCoroutinesDatabaseFactory.create(properties)

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
    fun afterEach() = runTest(StandardTestDispatcher()) {
        testCollection.deleteMany(Filters.empty())
        parentCollection.deleteMany(Filters.empty())
        childCollection.deleteMany(Filters.empty())
    }

    @Test
    fun `Retrieve multilevel entity`() = runTest(StandardTestDispatcher()) {
        val entity = TestMongoEntity()
        testCollection.findOneAndReplace(
            Filters.eq("_id", entity.id),
            entity,
            FindOneAndReplaceOptions().upsert(true).returnDocument(AFTER)
        )!!

        val result = testMongoRepository.one(entity.id)
        assertEquals(entity, result)
    }

    @Test
    fun `Retrieve multilevel entity list without filter`() = runTest(StandardTestDispatcher()) {
        val entities = (1..10).map { TestMongoEntity() }
        testCollection.insertMany(entities)

        val result = testMongoRepository.all().toList()
        assertEquals(10, result.size)
    }

    @Test
    fun `Retrieve multilevel entity list with filter`() = runTest(StandardTestDispatcher()) {
        val found = entityListFoundData
        val entities = found + entityListNotFoundData
        testCollection.insertMany(entities)

        val result = testMongoRepository.all(
            found.map { it.stringValue },
            foundDateMin.atOffset(UTC),
            foundDateMax.atOffset(UTC),
        ).toList()
        assertEquals(found, result)
    }

    @Test
    fun `Retrieve multilevel entity page without filter and sort`() = runTest(StandardTestDispatcher()) {
        val found = entityListFoundData
        val entities = found + entityListNotFoundData
        testCollection.insertMany(entities)

        val result = testMongoRepository.page(
            PageRequestData(2, 7)
        )
        assertEquals(7, result.contentList.toList().size)
        assertEquals(entities.size.toLong(), result.totalItems)
    }

    @Test
    fun `Retrieve multilevel entity page without filter sorted ASC`() = runTest(StandardTestDispatcher()) {
        val found = entityListFoundData
        val entities = found + entityListNotFoundData
        testCollection.insertMany(entities)

        val first = entities.sortedBy { it.datetimeOffset }[10]

        val result = testMongoRepository.page(
            PageRequestData(3, 5, listOf(PageSort("datetimeOffset", ASC))),
        )
        val contentList = result.contentList.toList()
        assertEquals(5, contentList.size)
        assertEquals(entities.size.toLong(), result.totalItems)
        assertEquals(first, contentList.first())
    }

    @Test
    fun `Retrieve multilevel entity page without filter sorted DESC`() = runTest(StandardTestDispatcher()) {
        val entities = entityListFoundData + entityListNotFoundData
        testCollection.insertMany(entities)

        val first = entities.sortedByDescending { it.stringValue }[0]

        val result = testMongoRepository.page(
            PageRequestData(1, 3, listOf(PageSort("stringValue", DESC))),
        )
        val contentList = result.contentList.toList()
        assertEquals(3, contentList.size)
        assertEquals(entities.size.toLong(), result.totalItems)
        assertEquals(first, contentList.first())
    }

    @Test
    fun `Retrieve multilevel entity page with filter and without sort`() = runTest(StandardTestDispatcher()) {
        val found = entityListFoundData
        val entities = found + entityListNotFoundData
        testCollection.insertMany(entities)

        val result = testMongoRepository.page(
            found.map { it.stringValue },
            foundDateMin.atOffset(UTC),
            foundDateMax.atOffset(UTC),
            PageRequestData(4, 6),
        )
        assertEquals(2, result.contentList.toList().size)
        assertEquals(entityListFoundData.size.toLong(), result.totalItems)
    }

    @Test
    fun `Retrieve multilevel entity page with filter sorted ASC`() = runTest(StandardTestDispatcher()) {
        val found = entityListFoundData
        val entities = found + entityListNotFoundData
        testCollection.insertMany(entities)

        val result = testMongoRepository.page(
            found.map { it.stringValue },
            foundDateMin.atOffset(UTC),
            foundDateMax.atOffset(UTC),
            PageRequestData(2, 6, listOf(PageSort("bigInteger", ASC))),
        )
        assertEquals(6, result.contentList.toList().size)
        assertEquals(found.size.toLong(), result.totalItems)
        assertTrue(found.map { it.stringValue }.containsAll(result.map { it.stringValue }.contentList.toList()))
    }

    @Test
    fun `Retrieve multilevel entity page with filter sorted DESC`() = runTest(StandardTestDispatcher()) {
        val found = entityListFoundData
        val entities = found + entityListNotFoundData
        testCollection.insertMany(entities)

        val first = found.sortedBy { it.bigDecimal }[12]

        val result = testMongoRepository.page(
            found.map { it.stringValue },
            foundDateMin.atOffset(UTC),
            foundDateMax.atOffset(UTC),
            PageRequestData(4, 4, listOf(PageSort("bigDecimal", ASC))),
        )
        val contentList = result.contentList.toList()
        assertEquals(4, contentList.size)
        assertEquals(found.size.toLong(), result.totalItems)
        assertEquals(first, contentList.first())
    }

    @Test
    fun `Retrieve multilevel entity page empty`() = runTest(StandardTestDispatcher()) {
        val entities = entityListFoundData + entityListNotFoundData
        testCollection.insertMany(entities)

        val result = testMongoRepository.page(
            entityListFoundData.map { it.stringValue },
            foundDateMin.atOffset(UTC).plusDays(20),
            foundDateMax.atOffset(UTC).plusDays(20),
            PageRequestData(),
        )
        assertTrue(result.contentList.count() == 0)
        assertEquals(0, result.totalItems)
    }

    @Test
    fun `Save multilevel entity`() = runTest(StandardTestDispatcher()) {
        val entity = TestMongoEntity()
        val another = TestMongoEntity()
        testMongoRepository.save(entity.id, entity)
        testMongoRepository.save(another.id, another)

        val result = testCollection.find(Filters.eq("_id", entity.id)).firstOrNull()
        assertEquals(entity, result)
    }

    @Test
    fun `Update multilevel entity`() = runTest(StandardTestDispatcher()) {
        val saved = TestMongoEntity()
        testCollection.insertMany(listOf(saved, TestMongoEntity()))

        val entity = TestMongoEntity(id = saved.id)
        val result = testMongoRepository.save(saved.id, entity)
        assertEquals(entity, result)

        val found = testCollection.find(Filters.eq("_id", entity.id)).firstOrNull()
        assertEquals(entity, found)
    }

    @Test
    fun `Find joined entity`() = runTest(StandardTestDispatcher()) {
        childCollection.insertMany(childTestList)
        parentCollection.insertOne(parentTestEntity)

        val result = parentTestRepository.findFullTestEntity(parentTestEntity.id)
        assertEquals(fullTestEntity, result)
    }

}
