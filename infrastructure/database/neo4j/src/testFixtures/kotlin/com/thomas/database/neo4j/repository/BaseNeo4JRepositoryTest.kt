package com.thomas.database.neo4j.repository

import com.thomas.database.neo4j.extension.purge
import com.thomas.database.neo4j.extension.runQueries
import com.thomas.neo4j.StringFunctions
import com.thomas.neo4j.ZonedDateTimeFunctions
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.neo4j.harness.Neo4j
import org.neo4j.harness.Neo4jBuilders
import org.neo4j.ogm.config.Configuration
import org.neo4j.ogm.session.Session
import org.neo4j.ogm.session.SessionFactory

@TestInstance(PER_CLASS)
abstract class BaseNeo4JRepositoryTest<R : Neo4JRepository>(
    protected val nodesPackages: List<String>,
    protected val setupScript: String? = null,
    protected val teardownScript: String? = null,
) {

    protected lateinit var embeddedDatabaseServer: Neo4j

    protected lateinit var sessionFactory: SessionFactory

    protected lateinit var repository: R

    abstract fun createRepository(sessionFactory: SessionFactory): R

    open fun createConfiguration(): Configuration = embeddedDatabaseServer.ogmConfiguration()

    private fun Neo4j.ogmConfiguration() = Configuration.Builder()
        .uri(this.boltURI().toString())
        .database(this.defaultDatabaseService().databaseName())
        .connectionLivenessCheckTimeout(10000)
        .verifyConnection(true)
        .connectionPoolSize(20)
        .useNativeTypes()
        .build()

    @BeforeAll
    open fun beforeAll() {
        embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder()
            .withDisabledServer()
            .withFunction(StringFunctions::class.java)
            .withFunction(ZonedDateTimeFunctions::class.java)
            .build()

        sessionFactory = SessionFactory(createConfiguration(), *nodesPackages.toTypedArray())
        repository = createRepository(sessionFactory)
        setupScript?.apply { runScript(file = this) }
    }

    @AfterAll
    open fun afterAll() {
        teardownScript?.apply { runScript(file = this) }
        embeddedDatabaseServer.close()
    }

    @AfterEach
    open fun afterEach() {
        sessionFactory.purge()
    }

    protected fun runScript(
        file: String,
    ): Session = this::class.java.getResourceAsStream(file).bufferedReader().readLines().filter {
        it.trim().isNotEmpty()
    }.let { lines ->
        sessionFactory.runQueries {
            lines.forEach { line ->
                this.query(line, mapOf<String, Any>())
            }
        }
    }

}
