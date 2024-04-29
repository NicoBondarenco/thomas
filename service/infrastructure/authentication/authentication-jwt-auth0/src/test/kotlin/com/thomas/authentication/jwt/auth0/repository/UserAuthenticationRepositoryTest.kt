package com.thomas.authentication.jwt.auth0.repository

import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.thomas.authentication.jwt.auth0.util.activeGroupAuthenticationOne
import com.thomas.authentication.jwt.auth0.util.activeGroupAuthenticationTwo
import com.thomas.authentication.jwt.auth0.util.activeUserAuthentication
import com.thomas.authentication.jwt.auth0.util.activeUserAuthenticationWithoutGroups
import com.thomas.authentication.jwt.auth0.util.defaultConfiguration
import com.thomas.authentication.jwt.auth0.util.groupDocuments
import com.thomas.authentication.jwt.auth0.util.inactiveGroupAuthenticationThree
import com.thomas.authentication.jwt.auth0.util.userDocuments
import com.thomas.mongo.configuration.MongoDatabaseFactory
import com.thomas.mongo.properties.MongoDatabaseProperties
import java.time.Duration
import java.util.UUID.randomUUID
import kotlinx.coroutines.runBlocking
import org.awaitility.kotlin.atMost
import org.awaitility.kotlin.await
import org.awaitility.kotlin.until
import org.bson.Document
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.testcontainers.containers.GenericContainer

@TestInstance(PER_CLASS)
class UserAuthenticationRepositoryTest {

    private val database: String = "mongo_database"
    private val username: String = "mongo_username"
    private val password: String = "mongo_password"
    private val port: Int = 27017

    private val container = GenericContainer("mongo:6.0.1")
        .withEnv("MONGO_INITDB_DATABASE", database)
        .withEnv("MONGO_INITDB_ROOT_USERNAME", username)
        .withEnv("MONGO_INITDB_ROOT_PASSWORD", password)
        .withExposedPorts(port)

    private lateinit var properties: MongoDatabaseProperties
    private lateinit var mongoDatabase: MongoDatabase
    private lateinit var repository: UserAuthenticationRepository
    private lateinit var userCollection: MongoCollection<Document>
    private lateinit var groupCollection: MongoCollection<Document>

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
        repository = UserAuthenticationRepository(mongoDatabase, defaultConfiguration)
        userCollection = mongoDatabase.getCollection(defaultConfiguration.userCollection)
        groupCollection = mongoDatabase.getCollection(defaultConfiguration.groupCollection)

        insertData()
    }

    private fun insertData() = runBlocking {
        groupCollection.insertMany(groupDocuments)
        userCollection.insertMany(userDocuments)
    }

    @AfterAll
    fun afterAll() {
        container.stop()
        await atMost Duration.ofSeconds(30) until { !container.isRunning }
    }

    @Test
    fun `Retrieve existent user`() {
        val user = repository.findUserAuthentication(activeUserAuthentication.id)!!
        assertEquals(activeUserAuthentication.firstName, user.firstName)
        assertEquals(activeUserAuthentication.lastName, user.lastName)
        assertEquals(activeUserAuthentication.mainEmail, user.mainEmail)
        assertEquals(activeUserAuthentication.phoneNumber, user.phoneNumber)
        assertEquals(activeUserAuthentication.profilePhoto, user.profilePhoto)
        assertEquals(activeUserAuthentication.birthDate, user.birthDate)
        assertEquals(activeUserAuthentication.userGender, user.userGender)
        assertEquals(activeUserAuthentication.userProfile, user.userProfile)
        assertEquals(activeUserAuthentication.isActive, user.isActive)
        assertEquals(activeUserAuthentication.userRoles, user.userRoles)
        assertTrue(user.userGroups.contains(activeGroupAuthenticationOne))
        assertTrue(user.userGroups.contains(activeGroupAuthenticationTwo))
        assertTrue(user.userGroups.contains(inactiveGroupAuthenticationThree))
    }

    @Test
    fun `Retrieve existent user without groups`() {
        val user = repository.findUserAuthentication(activeUserAuthenticationWithoutGroups.id)
        assertEquals(activeUserAuthenticationWithoutGroups, user)
    }

    @Test
    fun `Retrieve inexistent user`() {
        val user = repository.findUserAuthentication(randomUUID())
        assertNull(user)
    }

}
