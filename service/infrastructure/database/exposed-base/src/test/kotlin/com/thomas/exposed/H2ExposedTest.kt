package com.thomas.exposed

import com.thomas.exposed.configuration.ExposedDatabaseFactory
import com.thomas.exposed.metadata.TableMetadata
import com.thomas.exposed.properties.ExposedDatabaseDialect
import com.thomas.exposed.properties.ExposedDatabaseDialect.H2
import org.h2.jdbcx.JdbcDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

class H2ExposedTest : ExposedTest() {

    override val databaseName: String = "test-database"
    override val driverClass: String = "org.h2.Driver"
    override val databaseUsername: String = "thomas-test"
    override val databasePassword: String = "thomas-test"
    override val defaultSchema: String = "public"
    override val explicitDialect: ExposedDatabaseDialect = H2

    private fun databaseArguments(databaseName: String) = listOf(
        "jdbc:h2:~/h2-databases/$databaseName",
        "DATABASE_TO_LOWER=TRUE",
//        "INIT=RUNSCRIPT FROM 'classpath:h2-scripts/database-creation.sql'",
    ).joinToString(";")

    override fun auditableDatabase(): Database {
        val properties = auditableProperties(databaseArguments("test-database"))
        val datasource = JdbcDataSource().also {
            it.setUrl(properties.connectionUrl)
            it.user = properties.databaseUsername
            it.password = properties.databasePassword
        }

        return ExposedDatabaseFactory.create(datasource, properties).apply {
            createDatabase()
        }
    }

    override fun simpleDatabase(): Database {
        val properties = simpleProperties(databaseArguments("simple-database"))
        val datasource = JdbcDataSource().also {
            it.setUrl(properties.connectionUrl)
            it.user = properties.databaseUsername
            it.password = properties.databasePassword
        }

        return ExposedDatabaseFactory.create(datasource, properties).apply {
            createDatabase()
        }
    }

    override fun findTablesMetadata(): List<TableMetadata> {
        val list = mutableListOf<TableMetadata>()
        TransactionManager.current().exec(
            """
            SELECT table_schema, table_name
            FROM INFORMATION_SCHEMA.TABLES;
            """.trimIndent()
        ) { rs ->
            while (rs.next()) {
                list.add(
                    TableMetadata(
                        rs.getString("table_schema"),
                        rs.getString("table_name"),
                    )
                )
            }
        }
        return list
    }

    override fun runAfterAll() {
        auditableDatabase.clearDatabase()
        simpleDatabase.clearDatabase()
    }

    private fun Database.createDatabase() = transaction(this) {
        val script = H2ExposedTest::class.java
            .getResource("/h2-scripts/database-creation.sql")!!.readText()
        exec(script)
    }

    private fun Database.clearDatabase() = transaction(this) {
        exec("drop all objects delete files")
    }

}
