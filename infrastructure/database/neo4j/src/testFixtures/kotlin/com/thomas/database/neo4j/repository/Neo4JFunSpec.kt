package com.thomas.database.neo4j.repository

import com.thomas.database.neo4j.extension.purge
import com.thomas.database.neo4j.extension.runQueries
import com.thomas.neo4j.StringFunctions
import com.thomas.neo4j.ZonedDateTimeFunctions
import io.kotest.core.names.TestName
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.scopes.FunSpecContainerScope
import io.kotest.core.spec.style.scopes.addContainer
import org.neo4j.harness.Neo4j
import org.neo4j.harness.Neo4jBuilders
import org.neo4j.ogm.config.Configuration
import org.neo4j.ogm.session.Session
import org.neo4j.ogm.session.SessionFactory

abstract class Neo4JFunSpec<R : Neo4JRepository>(
    protected val nodesPackages: List<String>,
    protected val setupScript: String? = null,
    protected val teardownScript: String? = null,
    body: Neo4JFunSpec<R>.() -> Unit = {}
) : FunSpec(body as FunSpec.() -> Unit) {

    init {
        coroutineTestScope = true
        coroutineTestScope = true
    }

    var embeddedDatabaseServer: Neo4j? = null
    lateinit var sessionFactory: SessionFactory
    lateinit var repository: R

    abstract fun createRepository(sessionFactory: SessionFactory): R

    open fun createConfiguration(): Configuration {
        embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder()
            .withDisabledServer()
            .withFunction(StringFunctions::class.java)
            .withFunction(ZonedDateTimeFunctions::class.java)
            .build()
        return embeddedDatabaseServer!!.ogmConfiguration()
    }

    private fun Neo4j.ogmConfiguration() = Configuration.Builder()
        .uri(this.boltURI().toString())
        .database(this.defaultDatabaseService().databaseName())
        .connectionLivenessCheckTimeout(10000)
        .verifyConnection(true)
        .connectionPoolSize(20)
        .useNativeTypes()
        .build()

    protected fun runScript(
        file: String,
    ): Session = this::class.java.getResourceAsStream(file).bufferedReader().readLines().filter {
        it.trim().isNotEmpty() && !it.startsWith("//")
    }.let { lines ->
        sessionFactory.runQueries {
            lines.forEach { line ->
                this.query(line, mapOf<String, Any>())
            }
        }
    }

    override suspend fun beforeSpec(spec: Spec) {
        sessionFactory = SessionFactory(createConfiguration(), *nodesPackages.toTypedArray())
        repository = createRepository(sessionFactory)
        setupScript?.apply { runScript(file = this) }
    }

    override suspend fun afterSpec(spec: Spec) {
        teardownScript?.apply { runScript(file = this) }
        embeddedDatabaseServer?.close()
    }

    fun context(
        name: String,
        script: String? = null,
        before: () -> Unit = {},
        after: () -> Unit = {},
        test: suspend FunSpecContainerScope.() -> Unit
    ) = addContainer(TestName("context ", name, false), false, null) {
        try {
            script?.apply { runScript(this) }
            before()
            FunSpecContainerScope(this).test()
            after()
        } finally {
            sessionFactory.purge()
        }
    }

}