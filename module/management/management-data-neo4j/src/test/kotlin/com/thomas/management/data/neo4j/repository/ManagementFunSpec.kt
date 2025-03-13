package com.thomas.management.data.neo4j.repository

import com.thomas.database.neo4j.repository.Neo4JFunSpec
import com.thomas.database.neo4j.repository.Neo4JRepository
import io.kotest.core.spec.style.scopes.FunSpecContainerScope
import kotlin.reflect.KClass
import org.neo4j.ogm.config.Configuration

abstract class ManagementFunSpec<R : Neo4JRepository>(
    body: ManagementFunSpec<R>.() -> Unit = {}
) : Neo4JFunSpec<R>(
    nodesPackages = listOf("com.thomas.management.data.neo4j.model.node"),
    setupScript = "/scripts/setup.cypher",
    teardownScript = "/scripts/teardown.cypher",
    body = body as Neo4JFunSpec<R>.() -> Unit,
) {

    private lateinit var nodesMap: MutableMap<KClass<*>, (Any) -> Any>
    private val entitiesMap: MutableMap<KClass<*>, Set<Any>> = mutableMapOf()

    fun initNodes(classes: Map<KClass<*>, (Any) -> Any>) {
        nodesMap = mutableMapOf()
        classes.forEach { (klass, mapper) ->
            nodesMap[klass] = mapper
        }
    }

    fun loadNodes() {
        nodesMap.forEach { (klass, mapper) ->
            sessionFactory.openSession().loadAll(klass.java, 5).takeIf {
                it.isNotEmpty()
            }?.map(mapper)?.apply {
                entitiesMap[this.first()::class] = this.toSet()
            }
        }
    }

    fun clearNodes() {
        entitiesMap.clear()
    }

    fun <E : Any> entities(
        type: KClass<E>
    ): Set<E> = entitiesMap[type] as? Set<E> ?: emptySet()

    fun context(
        name: String,
        script: String? = null,
        test: suspend FunSpecContainerScope.() -> Unit
    ) = context(
        name = name,
        script = script,
        before = this::loadNodes,
        after = this::clearNodes,
        test = test,
    )

    override fun createConfiguration(): Configuration = Configuration.Builder()
        .uri("bolt://localhost:7687")
        .credentials("neo4j", "Meruss@453822")
        .database("neo4j")
        .connectionLivenessCheckTimeout(10000)
        .verifyConnection(true)
        .connectionPoolSize(20)
        .useNativeTypes()
        .build()

}