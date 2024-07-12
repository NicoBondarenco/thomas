package com.thomas.management.data.exposed.repository

import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.model.security.SecurityUser
import com.thomas.exposed.configuration.ExposedDatabaseFactory
import com.thomas.exposed.properties.ExposedAuditoryProperties
import com.thomas.exposed.properties.ExposedDatabaseDialect.POSTGRESQL
import com.thomas.exposed.properties.ExposedDatabaseProperties
import java.io.File
import java.time.Duration
import java.util.UUID.randomUUID
import org.awaitility.kotlin.atMost
import org.awaitility.kotlin.await
import org.awaitility.kotlin.until
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.testcontainers.containers.GenericContainer

@TestInstance(PER_CLASS)
abstract class BaseExposedRepositoryTest {

    private val databasePort = 5432
    private val databaseName = "management-db"
    private val databaseUsername = "management-db-user"
    private val databasePassword = "management-db-pass"

    private lateinit var databaseProperties: ExposedDatabaseProperties
    protected lateinit var database: Database

    private val container = GenericContainer("postgres:14.1")
        .withEnv("POSTGRES_DB", databaseName)
        .withEnv("POSTGRES_USER", databaseUsername)
        .withEnv("POSTGRES_PASSWORD", databasePassword)
        .withExposedPorts(databasePort)

    private fun createDatabaseProperties() = ExposedDatabaseProperties(
        driverClass = "org.postgresql.Driver",
        connectionUrl = "jdbc:postgresql://${container.host}:${container.getMappedPort(databasePort)}/$databaseName",
        databaseUsername = databaseUsername,
        databasePassword = databasePassword,
        defaultSchema = "management",
        explicitDialect = POSTGRESQL,
        databaseAuditory = ExposedAuditoryProperties(
            tablesPackage = "com.thomas.management.data.exposed.mapping",
        )
    )

    private fun initDatabase() {
        val path = BaseExposedRepositoryTest::class.java.getResource("/management/migration")!!.toURI()
        File(path).listFiles()!!.forEach { file ->
            transaction(database) {
                exec(file.readText())
            }
        }
    }

    abstract fun configureBeforeAll()

    @BeforeAll
    fun beforeAll() {
        SecurityUser(
            userId = randomUUID(),
            firstName = "User",
            lastName = "Security",
            mainEmail = "user@example.com",
            isActive = true,
        ).apply { currentUser = this }
        container.start()
        await atMost Duration.ofSeconds(30) until { container.isRunning }
        databaseProperties = createDatabaseProperties()
        database = ExposedDatabaseFactory.create(databaseProperties)
        initDatabase()
        configureBeforeAll()
    }

    @AfterAll
    fun afterAll() {
        container.stop()
        await atMost Duration.ofSeconds(30) until { !container.isRunning }
    }

    @AfterEach
    fun afterEach() {
        val scripts = listOf(
            "DELETE FROM \"management\".\"user_group\";",
            "DELETE FROM \"management\".\"group_role\";",
            "DELETE FROM \"management\".\"user_role\";",
            "DELETE FROM \"management\".\"group\";",
            "DELETE FROM \"management\".\"user\";",
        )
        transaction(database) {
            execInBatch(scripts)
        }
    }

    protected fun insertData(file: String) {
        val script = BaseExposedRepositoryTest::class.java.getResource("/management/test-inserts/$file.sql")!!.readText()
        transaction(database) {
            exec(script)
        }
    }

}
