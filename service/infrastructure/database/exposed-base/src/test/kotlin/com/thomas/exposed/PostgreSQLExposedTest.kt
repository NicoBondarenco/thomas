package com.thomas.exposed

import com.thomas.exposed.configuration.ExposedDatabaseFactory
import com.thomas.exposed.metadata.PostgreSQLTableInfo
import com.thomas.exposed.metadata.TableInfo
import com.thomas.exposed.metadata.TableMetadata
import com.thomas.exposed.properties.ExposedDatabaseDialect
import com.thomas.exposed.properties.ExposedDatabaseDialect.POSTGRESQL
import java.time.Duration
import org.awaitility.kotlin.atMost
import org.awaitility.kotlin.await
import org.awaitility.kotlin.until
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.testcontainers.containers.GenericContainer

class PostgreSQLExposedTest : ExposedTest() {

    override val databaseName: String = "test-database"
    override val driverClass: String = "org.postgresql.Driver"
    override val databaseUsername: String = "thomas-test"
    override val databasePassword: String = "thomas-test"
    override val defaultSchema: String = "public"
    override val explicitDialect: ExposedDatabaseDialect = POSTGRESQL

    private val databaseTable: TableInfo = PostgreSQLTableInfo
    private val tableSchemasFilter: List<String> = listOf("public", "common", "auditory")

    private val port = 5432
    private val container = GenericContainer("postgres:14.1")
        .withEnv("POSTGRES_DB", databaseName)
        .withEnv("POSTGRES_USER", databaseUsername)
        .withEnv("POSTGRES_PASSWORD", databasePassword)
        .withExposedPorts(port)

    private val scripts = listOf(
        "/postgresql-scripts/database-creation.sql"
    )

    override fun auditableDatabase(): Database {
        container.start()

        await atMost Duration.ofSeconds(30) until { container.isRunning }

        val properties = auditableProperties("jdbc:postgresql://${container.host}:${container.getMappedPort(port)}/$databaseName")

        return ExposedDatabaseFactory.create(properties).apply {
            scripts.forEach { name ->
                transaction(this) {
                    val script = PostgreSQLExposedTest::class.java.getResource(name)!!.readText()
                    exec(script)
                }
            }
        }
    }

    override fun simpleDatabase(): Database {
        container.start()

        await atMost Duration.ofSeconds(30) until { container.isRunning }

        val properties = simpleProperties("jdbc:postgresql://${container.host}:${container.getMappedPort(port)}/$databaseName")

        return ExposedDatabaseFactory.create(properties)
    }

    override fun findTablesMetadata(): List<TableMetadata> = databaseTable.selectAll().where {
        databaseTable.schema inList tableSchemasFilter
    }.map {
        TableMetadata(
            schema = it[databaseTable.schema],
            table = it[databaseTable.table],
        )
    }

}
