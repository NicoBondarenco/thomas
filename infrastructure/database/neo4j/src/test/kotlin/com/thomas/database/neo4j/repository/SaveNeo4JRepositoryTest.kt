package com.thomas.database.neo4j.repository

import com.thomas.core.extension.toUUIDOrNull
import com.thomas.core.util.DateUtils
import com.thomas.core.util.NumberUtils.randomInteger
import com.thomas.core.util.StringUtils.randomString
import com.thomas.database.neo4j.node.SavePropsNode
import com.thomas.database.neo4j.util.toZonedDateTime
import java.util.UUID.randomUUID
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.neo4j.ogm.exception.CypherException


class SaveNeo4JRepositoryTest : Neo4JRepositoryTest() {

    companion object {

        private val NODES = listOf(
            SavePropsNode(id = "8cb42a7a-7229-416a-a0d9-8e647d6ad002".toUUIDOrNull()!!, propName = "Águillar Muñoz", propValue = 60, propDatetime = "2049-04-22T16:31:57.816495Z".toZonedDateTime()),
            SavePropsNode(id = "97595f5d-f277-443a-84e6-3cb75f667960".toUUIDOrNull()!!, propName = "Lucas Almeida", propValue = 25, propDatetime = "2093-08-07T10:34:34.756756Z".toZonedDateTime()),
            SavePropsNode(id = "835320c9-6e52-423a-af63-7ef76fdfcf69".toUUIDOrNull()!!, propName = "Mariana Costa", propValue = 76, propDatetime = "2039-05-19T18:38:25.951951Z".toZonedDateTime()),
            SavePropsNode(id = "43780154-453c-4a8d-98ef-507d4372f170".toUUIDOrNull()!!, propName = "Diego Morales", propValue = 61, propDatetime = "2043-08-30T13:30:00.348348Z".toZonedDateTime()),
            SavePropsNode(id = "9c0d4431-37f8-4771-9d03-52757f41590d".toUUIDOrNull()!!, propName = "Camila Herrera", propValue = 43, propDatetime = "2048-08-03T17:43:01.202202Z".toZonedDateTime()),
            SavePropsNode(id = "2f8936e7-6766-4ae2-a694-da864f429222".toUUIDOrNull()!!, propName = "Rafael Mendes", propValue = 60, propDatetime = "2050-04-08T11:30:56.159159Z".toZonedDateTime()),
            SavePropsNode(id = "fc76ef97-a7d8-4ebe-a710-cebcc7d0674e".toUUIDOrNull()!!, propName = "Javier Sánchez", propValue = 48, propDatetime = "2088-09-24T12:41:03.125125Z".toZonedDateTime()),
            SavePropsNode(id = "113253bc-a511-4e89-a1de-3b885644895a".toUUIDOrNull()!!, propName = "Beatriz Oliveira", propValue = 54, propDatetime = "2086-09-29T12:19:57.041041Z".toZonedDateTime()),
            SavePropsNode(id = "91a9561f-1be1-42cd-b7aa-42004d89452f".toUUIDOrNull()!!, propName = "Andrés Gómez", propValue = 78, propDatetime = "2043-05-10T17:17:29.655655Z".toZonedDateTime()),
            SavePropsNode(id = "ee6ee905-bcb6-4d02-bf1f-6b1ee87012c7".toUUIDOrNull()!!, propName = "Fernanda Ruiz", propValue = 81, propDatetime = "2057-06-21T01:18:00.505505Z".toZonedDateTime()),
        )

        private fun randomSaveProps(): SavePropsNode = SavePropsNode(
            id = randomUUID(),
            propName = randomString(),
            propValue = randomInteger(),
            propDatetime = DateUtils.randomZonedDateTime(),
        )

        @JvmStatic
        fun saveEntities() = (1..10).map {
            Arguments.of(randomSaveProps())
        }

        @JvmStatic
        fun updateEntities() = NODES.map {
            Arguments.of(randomSaveProps().copy(id = it.id))
        }

    }

    @ParameterizedTest
    @MethodSource("saveEntities")
    fun `Insert data test`(
        entity: SavePropsNode,
    ) = runTest(StandardTestDispatcher()) {
        runScript("/crud-operation.cypher")
        repository.saveData(entity)
        val result = sessionFactory.openSession().load(SavePropsNode::class.java, entity.id)
        assertEquals(entity, result)
    }

    @ParameterizedTest
    @MethodSource("updateEntities")
    fun `Update data test`(
        entity: SavePropsNode,
    ) = runTest(StandardTestDispatcher()) {
        runScript("/crud-operation.cypher")
        val old = sessionFactory.openSession().load(SavePropsNode::class.java, entity.id)
        assertEquals(NODES.first { it.id == entity.id }, old)
        repository.saveData(entity)
        val result = sessionFactory.openSession().load(SavePropsNode::class.java, entity.id)
        assertEquals(entity, result)
    }

    @Test
    fun `Insert data rollback`() = runTest(StandardTestDispatcher()) {
        runScript("/crud-operation.cypher")
        val existent = NODES.random()
        assertThrows<CypherException> {
            repository.saveData(randomSaveProps().copy(propName = existent.propName))
        }
        val result = sessionFactory.openSession().load(SavePropsNode::class.java, existent.id)
        assertEquals(existent, result)
    }

    @Test
    fun `Update data rollback`() = runTest(StandardTestDispatcher()) {
        runScript("/crud-operation.cypher")
        val existent = NODES.random()
        val update = NODES.filter { it.id != existent.id }.random()
        assertThrows<CypherException> {
            repository.saveData(update.copy(propName = existent.propName))
        }
        val result = sessionFactory.openSession().load(SavePropsNode::class.java, existent.id)
        assertEquals(existent, result)
    }

    @Test
    fun `Delete data`() = runTest(StandardTestDispatcher()) {
        runScript("/crud-operation.cypher")
        val id = NODES.random().id
        val existent = sessionFactory.openSession().load(SavePropsNode::class.java, id)
        repository.deleteData(existent)
        val result = sessionFactory.openSession().load(SavePropsNode::class.java, existent.id)
        assertNull(result)
    }

}
