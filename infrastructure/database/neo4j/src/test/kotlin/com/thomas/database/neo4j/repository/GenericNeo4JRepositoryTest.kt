package com.thomas.database.neo4j.repository

import com.thomas.database.neo4j.node.GenericPropsNode
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


class GenericNeo4JRepositoryTest : Neo4JRepositoryTest() {

    companion object {

        private val NODES = listOf(
            GenericPropsNode(id = "3c45bcfd-ab52-4b5c-ab54-19a22d613d31", propName = "Antonia", propValue = 10),
            GenericPropsNode(id = "f54ad5aa-1613-4f48-9798-b3da90cdd64a", propName = "Antônia", propValue = 50),
            GenericPropsNode(id = "d861cdc0-912a-4874-b715-c662decdbcbf", propName = "antônia", propValue = null),
            GenericPropsNode(id = "4c36c19c-21b5-4941-8b58-1723d662d61f", propName = null, propValue = 17),
            GenericPropsNode(id = "9c346c7d-1798-48f9-9305-fcb6b12134c0", propName = "José", propValue = 25),
            GenericPropsNode(id = "daa11bfa-e9e2-4897-a68e-208afdf87104", propName = "Maria", propValue = null),
            GenericPropsNode(id = "7c4069a4-c832-45c1-a746-441155f060bb", propName = null, propValue = null),
            GenericPropsNode(id = "828f021b-ef6e-438d-9cf5-c3f54038afb7", propName = null, propValue = null),
            GenericPropsNode(id = "919a9c42-3ec4-4523-b5c0-46b9c6134d37", propName = "josé", propValue = 99),
            GenericPropsNode(id = "01ffe4d7-f709-43b4-89cc-eb49c540a83a", propName = "jose", propValue = 44),
        )

        @JvmStatic
        fun isNullValues() = listOf(
            Arguments.of("prop_name", NODES.filter { it.propName == null }),
            Arguments.of("prop_value", NODES.filter { it.propValue == null }),
        )

        @JvmStatic
        fun isNotNullValues() = listOf(
            Arguments.of("prop_name", NODES.filter { it.propName != null }),
            Arguments.of("prop_value", NODES.filter { it.propValue != null }),
        )

        @JvmStatic
        fun inValues() = listOf(
            listOf("Antônia", "José").let { values -> Arguments.of("prop_name", values, NODES.filter { values.contains(it.propName) }) },
            listOf("antônia", "josé", null).let { values -> Arguments.of("prop_name", values, NODES.filter { values.contains(it.propName) || it.propName == null }) },
            listOf(17, 25, 99).let { values -> Arguments.of("prop_value", values, NODES.filter { values.contains(it.propValue) }) },
            listOf(10, 44, null).let { values -> Arguments.of("prop_value", values, NODES.filter { values.contains(it.propValue) || it.propValue == null }) },
        )

        @JvmStatic
        fun notInValues() = listOf(
            listOf("Antônia", "José").let { values -> Arguments.of("prop_name", values, NODES.filter { !values.contains(it.propName) || it.propName == null }) },
            listOf("antônia", "josé", null).let { values -> Arguments.of("prop_name", values, NODES.filter { !values.contains(it.propName) && it.propName != null }) },
            listOf(17, 25, 99).let { values -> Arguments.of("prop_value", values, NODES.filter { !values.contains(it.propValue) || it.propValue == null }) },
            listOf(10, 44, null).let { values -> Arguments.of("prop_value", values, NODES.filter { !values.contains(it.propValue) && it.propValue != null }) },
        )

    }

    @ParameterizedTest
    @MethodSource("isNullValues")
    fun `Property is null`(
        property: String,
        nodes: List<GenericPropsNode>,
    ) = runTest(StandardTestDispatcher()) {
        runScript("/generic-data.cypher")
        val result = repository.propertyIsNull(property)
        assertEquals(nodes.count(), result.count())
        nodes.forEach {
            result.contains(it)
        }
    }

    @ParameterizedTest
    @MethodSource("isNotNullValues")
    fun `Property is not null`(
        property: String,
        nodes: List<GenericPropsNode>,
    ) = runTest(StandardTestDispatcher()) {
        runScript("/generic-data.cypher")
        val result = repository.propertyIsNotNull(property)
        assertEquals(nodes.count(), result.count())
        nodes.forEach {
            result.contains(it)
        }
    }

    @ParameterizedTest
    @MethodSource("inValues")
    fun `Property is in values`(
        property: String,
        values: Collection<Any>,
        nodes: List<GenericPropsNode>,
    ) = runTest(StandardTestDispatcher()) {
        runScript("/generic-data.cypher")
        val result = repository.propertyInValues(property, values)
        assertEquals(nodes.count(), result.count())
        nodes.forEach {
            result.contains(it)
        }
    }

    @ParameterizedTest
    @MethodSource("notInValues")
    fun `Property is not in values`(
        property: String,
        values: Collection<Any>,
        nodes: List<GenericPropsNode>,
    ) = runTest(StandardTestDispatcher()) {
        runScript("/generic-data.cypher")
        val result = repository.propertyNotInValues(property, values)
        assertEquals(nodes.count(), result.count())
        nodes.forEach {
            result.contains(it)
        }
    }

}
