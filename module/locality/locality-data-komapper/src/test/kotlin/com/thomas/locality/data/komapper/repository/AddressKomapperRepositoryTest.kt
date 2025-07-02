package com.thomas.locality.data.komapper.repository

import com.thomas.core.context.SessionContextHolder.currentUser
import com.thomas.core.data.securityUserMaster
import com.thomas.core.model.general.AddressState
import com.thomas.core.util.NumberUtils.randomInteger
import com.thomas.core.util.StringUtils.randomString
import com.thomas.database.komapper.extension.EmptyR2dbcDataTypeProvider
import com.thomas.locality.data.entity.AddressEntity
import com.thomas.locality.data.komapper.definition.address
import io.kotest.common.runBlocking
import io.r2dbc.pool.ConnectionPool
import io.r2dbc.pool.ConnectionPoolConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import java.io.File
import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneOffset.UTC
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.awaitility.kotlin.atMost
import org.awaitility.kotlin.await
import org.awaitility.kotlin.until
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.assertNull
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.dialect.postgresql.r2dbc.PostgreSqlR2dbcDataTypeProvider
import org.komapper.dialect.postgresql.r2dbc.PostgreSqlR2dbcDialect
import org.komapper.r2dbc.DefaultR2dbcDatabaseConfig
import org.komapper.r2dbc.R2dbcDatabase
import org.testcontainers.containers.GenericContainer

@TestInstance(PER_CLASS)
class AddressKomapperRepositoryTest {

    companion object {
        private const val DATABASE_PORT = 5432
        private const val DATABASE_NAME = "management-db"
        private const val DATABASE_USERNAME = "management-db-user"
        private const val DATABASE_PASSWORD = "management-db-pass"
        private const val DATABASE_TIMEZONE = "UTC"
    }

    private val entity: AddressEntity
        get() = AddressEntity(
            zipcodeNumber = randomInteger(10000000, 99999999).toString(),
            addressStreet = listOf(randomString(), null).random(),
            addressComplement = listOf(randomString(), null).random(),
            addressUnit = listOf(randomString(), null).random(),
            addressNeighborhood = listOf(randomString(), null).random(),
            addressCity = randomString(numbers = false),
            addressState = AddressState.entries.random(),
            cityCode = listOf(randomString(), null).random(),
            referenceCode = listOf(randomString(), null).random(),
            phoneCode = listOf(randomInteger(10, 99).toString(), null).random(),
            federalCode = listOf(randomString(), null).random(),
            createdAt = OffsetDateTime.now(UTC).withNano(0),
            updatedAt = OffsetDateTime.now(UTC).withNano(0),
        )

    private val entities: MutableList<AddressEntity> = mutableListOf()

    private val container = GenericContainer("postgres:14.1")
        .withEnv("POSTGRES_DB", DATABASE_NAME)
        .withEnv("POSTGRES_USER", DATABASE_USERNAME)
        .withEnv("POSTGRES_PASSWORD", DATABASE_PASSWORD)
        .withEnv("TZ", DATABASE_TIMEZONE)
        .withEnv("PGTZ", DATABASE_TIMEZONE)
        .withExposedPorts(DATABASE_PORT)

    private lateinit var connection: ConnectionFactory
    private lateinit var pool: ConnectionPool
    private lateinit var database: R2dbcDatabase
    private lateinit var repository: AddressKomapperRepository

    private fun initDatabase() = runBlocking {
        val path = AddressKomapperRepositoryTest::class.java.getResource("/locality/migration")!!.toURI()
        File(path).listFiles()!!.sortedBy { it.name }.forEach { file ->
            database.runQuery(QueryDsl.executeScript(file.readText()))
        }
    }

    private fun insertData(file: String) = runBlocking {
        val script = AddressKomapperRepositoryTest::class.java.getResource("/locality/test-data/$file.sql")!!.readText()
        database.runQuery(QueryDsl.executeScript(script))
        entities.addAll(database.runQuery(QueryDsl.from(Meta.address)).toList())
    }

    @BeforeAll
    fun beforeAll() {
        container.start()
        await atMost Duration.ofSeconds(30) until { container.isRunning }
        currentUser = securityUserMaster

        connection = PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                .host(container.host)
                .port(container.getMappedPort(DATABASE_PORT))
                .database(DATABASE_NAME)
                .username(DATABASE_USERNAME)
                .password(DATABASE_PASSWORD)
                .schema("address")
                .build()
        )
        pool = ConnectionPool(
            ConnectionPoolConfiguration.builder(connection)
                .initialSize(5)
                .maxSize(20)
                .maxIdleTime(Duration.ofMinutes(30))
                .maxCreateConnectionTime(Duration.ofSeconds(10))
                .maxAcquireTime(Duration.ofSeconds(5))
                .maxLifeTime(Duration.ofHours(1))
                .validationQuery("SELECT 1")
                .build()
        )
        database = R2dbcDatabase(
            DefaultR2dbcDatabaseConfig(
                connectionFactory = pool,
                dialect = PostgreSqlR2dbcDialect(),
                dataTypeProvider = PostgreSqlR2dbcDataTypeProvider(EmptyR2dbcDataTypeProvider)
            )
        )
        repository = AddressKomapperRepository(database)
        initDatabase()
    }

    @AfterAll
    fun afterAll() {
        container.stop()
        await atMost Duration.ofSeconds(30) until { !container.isRunning }
    }

    @AfterEach
    fun afterEach() = runBlocking {
        listOf(
            "DELETE FROM \"locality\".\"address\";",
        ).forEach { database.runQuery(QueryDsl.executeScript(it)) }
        entities.clear()
    }

    @Test
    fun `Create address`() = runTest(StandardTestDispatcher()) {
        repeat(10) {
            val address = entity
            repository.create(address)
            val result = repository.id(address.id)
                ?.let {
                    it.copy(
                        createdAt = it.createdAt.withOffsetSameInstant(UTC),
                        updatedAt = it.updatedAt.withOffsetSameInstant(UTC)
                    )
                }
            assertEquals(address, result)
        }
    }

    @Test
    fun `Update address`() = runTest(StandardTestDispatcher()) {
        insertData("address-data")
        entities.forEach { e ->
            val address = e.let {
                val createdAt = it.createdAt.withNano(0).withOffsetSameInstant(UTC)
                entity.copy(
                    id = it.id,
                    zipcodeNumber = it.zipcodeNumber,
                    createdAt = createdAt,
                    updatedAt = createdAt.plusSeconds(100_000)
                )
            }
            repository.update(address)
            val result = repository.id(address.id)
                ?.let {
                    it.copy(
                        createdAt = it.createdAt.withOffsetSameInstant(UTC),
                        updatedAt = it.updatedAt.withOffsetSameInstant(UTC)
                    )
                }
            assertEquals(address, result)
        }
    }

    @Test
    fun `Find by zipcode`() = runTest(StandardTestDispatcher()) {
        insertData("address-data")
        entities.forEach { address ->
            val result = repository.findByZipcode(address.zipcodeNumber)

            assertNotNull(result)
            assertEquals(address, result)
        }
    }

    @Test
    fun `Find by zipcode not found`() = runTest(StandardTestDispatcher()) {
        insertData("address-data")
        repeat(10) {
            val zipcode = randomInteger(1000000, 9999999).toString().padStart(8, '0')

            val result = repository.findByZipcode(zipcode)

            assertNull(result)
        }
    }

}