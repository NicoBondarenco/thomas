package com.thomas.database.neo4j.repository

import com.thomas.core.extension.toUUIDOrNull
import com.thomas.database.neo4j.node.GenericNestedPropsNode
import com.thomas.database.neo4j.node.GenericPropsNode
import kotlin.reflect.KProperty
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


class GenericNeo4JRepositoryTest : Neo4JRepositoryTest() {

    companion object {

        private val NODES = listOf(
            GenericPropsNode(id = "3c45bcfd-ab52-4b5c-ab54-19a22d613d31".toUUIDOrNull()!!, propName = "Antonia", propValue = 10),
            GenericPropsNode(id = "f54ad5aa-1613-4f48-9798-b3da90cdd64a".toUUIDOrNull()!!, propName = "Antônia", propValue = 50),
            GenericPropsNode(id = "d861cdc0-912a-4874-b715-c662decdbcbf".toUUIDOrNull()!!, propName = "antônia", propValue = null),
            GenericPropsNode(id = "4c36c19c-21b5-4941-8b58-1723d662d61f".toUUIDOrNull()!!, propName = null, propValue = 17),
            GenericPropsNode(id = "9c346c7d-1798-48f9-9305-fcb6b12134c0".toUUIDOrNull()!!, propName = "José", propValue = 25),
            GenericPropsNode(id = "daa11bfa-e9e2-4897-a68e-208afdf87104".toUUIDOrNull()!!, propName = "Maria", propValue = null),
            GenericPropsNode(id = "7c4069a4-c832-45c1-a746-441155f060bb".toUUIDOrNull()!!, propName = null, propValue = null),
            GenericPropsNode(id = "828f021b-ef6e-438d-9cf5-c3f54038afb7".toUUIDOrNull()!!, propName = null, propValue = null),
            GenericPropsNode(id = "919a9c42-3ec4-4523-b5c0-46b9c6134d37".toUUIDOrNull()!!, propName = "josé", propValue = 99),
            GenericPropsNode(id = "01ffe4d7-f709-43b4-89cc-eb49c540a83a".toUUIDOrNull()!!, propName = "jose", propValue = 44),
        )

        private val NESTEDS = listOf(
            GenericNestedPropsNode(id = "9a035c4c-fa99-4174-bb38-2c3a30af4bb2".toUUIDOrNull()!!, nestedNode = NODES[0]),
            GenericNestedPropsNode(id = "2cb4dee1-ed92-4b0f-9188-b4c0b9757a86".toUUIDOrNull()!!, nestedNode = NODES[1]),
            GenericNestedPropsNode(id = "52abc836-1bce-40f3-a714-51a9698544f1".toUUIDOrNull()!!, nestedNode = NODES[2]),
            GenericNestedPropsNode(id = "8bc5737b-09b8-45a5-a513-aca619be4e61".toUUIDOrNull()!!, nestedNode = NODES[3]),
            GenericNestedPropsNode(id = "19ab6034-1adf-4286-a649-b63abe77d936".toUUIDOrNull()!!, nestedNode = NODES[4]),
            GenericNestedPropsNode(id = "0f582f65-0d03-4b7d-9d57-c949f0138d05".toUUIDOrNull()!!, nestedNode = NODES[5]),
            GenericNestedPropsNode(id = "4c3b28a2-24c6-42a2-b416-d12d876a1ff5".toUUIDOrNull()!!, nestedNode = NODES[6]),
            GenericNestedPropsNode(id = "12799246-c164-4a8b-a372-0f036b17bc83".toUUIDOrNull()!!, nestedNode = NODES[7]),
            GenericNestedPropsNode(id = "af17167a-7240-4cd6-883e-01a391a30574".toUUIDOrNull()!!, nestedNode = NODES[8]),
            GenericNestedPropsNode(id = "b57290f3-fd83-4240-b518-7c153e1fbb4f".toUUIDOrNull()!!, nestedNode = NODES[9]),
        )

        @JvmStatic
        fun isNullValues() = listOf(
            Arguments.of(GenericPropsNode::propName, NODES.filter { it.propName == null }),
            Arguments.of(GenericPropsNode::propValue, NODES.filter { it.propValue == null }),
        )

        @JvmStatic
        fun isNotNullValues() = listOf(
            Arguments.of(GenericPropsNode::propName, NODES.filter { it.propName != null }),
            Arguments.of(GenericPropsNode::propValue, NODES.filter { it.propValue != null }),
        )

        @JvmStatic
        fun inValues() = listOf(
            listOf("Antônia", "José").let { values -> Arguments.of(GenericPropsNode::propName, values, NODES.filter { values.contains(it.propName) }) },
            listOf("antônia", "josé", null).let { values -> Arguments.of(GenericPropsNode::propName, values, NODES.filter { values.contains(it.propName) || it.propName == null }) },
            listOf(17, 25, 99).let { values -> Arguments.of(GenericPropsNode::propValue, values, NODES.filter { values.contains(it.propValue) }) },
            listOf(10, 44, null).let { values -> Arguments.of(GenericPropsNode::propValue, values, NODES.filter { values.contains(it.propValue) || it.propValue == null }) },
        )

        @JvmStatic
        fun notInValues() = listOf(
            listOf("Antônia", "José").let { values -> Arguments.of(GenericPropsNode::propName, values, NODES.filter { !values.contains(it.propName) || it.propName == null }) },
            listOf("antônia", "josé", null).let { values -> Arguments.of(GenericPropsNode::propName, values, NODES.filter { !values.contains(it.propName) && it.propName != null }) },
            listOf(17, 25, 99).let { values -> Arguments.of(GenericPropsNode::propValue, values, NODES.filter { !values.contains(it.propValue) || it.propValue == null }) },
            listOf(10, 44, null).let { values -> Arguments.of(GenericPropsNode::propValue, values, NODES.filter { !values.contains(it.propValue) && it.propValue != null }) },
        )

        @JvmStatic
        fun isNullValuesNested() = listOf(
            Arguments.of(GenericPropsNode::propName, NESTEDS.filter { it.nestedNode.propName == null }),
            Arguments.of(GenericPropsNode::propValue, NESTEDS.filter { it.nestedNode.propValue == null }),
        )

        @JvmStatic
        fun isNotNullValuesNested() = listOf(
            Arguments.of(GenericPropsNode::propName, NESTEDS.filter { it.nestedNode.propName != null }),
            Arguments.of(GenericPropsNode::propValue, NESTEDS.filter { it.nestedNode.propValue != null }),
        )

        @JvmStatic
        fun inValuesNested() = listOf(
            listOf("Antônia", "José").let { values -> Arguments.of(GenericPropsNode::propName, values, NESTEDS.filter { values.contains(it.nestedNode.propName) }) },
            listOf("antônia", "josé", null).let { values -> Arguments.of(GenericPropsNode::propName, values, NESTEDS.filter { values.contains(it.nestedNode.propName) || it.nestedNode.propName == null }) },
            listOf(17, 25, 99).let { values -> Arguments.of(GenericPropsNode::propValue, values, NESTEDS.filter { values.contains(it.nestedNode.propValue) }) },
            listOf(10, 44, null).let { values -> Arguments.of(GenericPropsNode::propValue, values, NESTEDS.filter { values.contains(it.nestedNode.propValue) || it.nestedNode.propValue == null }) },
        )

        @JvmStatic
        fun notInValuesNested() = listOf(
            listOf("Antônia", "José").let { values -> Arguments.of(GenericPropsNode::propName, values, NESTEDS.filter { !values.contains(it.nestedNode.propName) || it.nestedNode.propName == null }) },
            listOf("antônia", "josé", null).let { values -> Arguments.of(GenericPropsNode::propName, values, NESTEDS.filter { !values.contains(it.nestedNode.propName) && it.nestedNode.propName != null }) },
            listOf(17, 25, 99).let { values -> Arguments.of(GenericPropsNode::propValue, values, NESTEDS.filter { !values.contains(it.nestedNode.propValue) || it.nestedNode.propValue == null }) },
            listOf(10, 44, null).let { values -> Arguments.of(GenericPropsNode::propValue, values, NESTEDS.filter { !values.contains(it.nestedNode.propValue) && it.nestedNode.propValue != null }) },
        )

    }

    @ParameterizedTest
    @MethodSource("isNullValues")
    fun `Property is null`(
        property: KProperty<Any>,
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
    @MethodSource("isNullValuesNested")
    fun `Property is null nested`(
        property: KProperty<Any>,
        nodes: List<GenericNestedPropsNode>,
    ) = runTest(StandardTestDispatcher()) {
        runScript("/generic-data.cypher")
        val result = repository.propertyIsNullNested(property)
        assertEquals(nodes.count(), result.count())
        nodes.forEach {
            result.contains(it)
        }
    }

    @ParameterizedTest
    @MethodSource("isNotNullValues")
    fun `Property is not null`(
        property: KProperty<Any>,
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
    @MethodSource("isNotNullValuesNested")
    fun `Property is not null nested`(
        property: KProperty<Any>,
        nodes: List<GenericNestedPropsNode>,
    ) = runTest(StandardTestDispatcher()) {
        runScript("/generic-data.cypher")
        val result = repository.propertyIsNotNullNested(property)
        assertEquals(nodes.count(), result.count())
        nodes.forEach {
            result.contains(it)
        }
    }

    @ParameterizedTest
    @MethodSource("inValues")
    fun `Property is in values`(
        property: KProperty<Any>,
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
    @MethodSource("inValuesNested")
    fun `Property is in values nested`(
        property: KProperty<Any>,
        values: Collection<Any>,
        nodes: List<GenericNestedPropsNode>,
    ) = runTest(StandardTestDispatcher()) {
        runScript("/generic-data.cypher")
        val result = repository.propertyInValuesNested(property, values)
        assertEquals(nodes.count(), result.count())
        nodes.forEach {
            result.contains(it)
        }
    }

    @ParameterizedTest
    @MethodSource("notInValues")
    fun `Property is not in values`(
        property: KProperty<Any>,
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

    @ParameterizedTest
    @MethodSource("notInValuesNested")
    fun `Property is not in values nested`(
        property: KProperty<Any>,
        values: Collection<Any>,
        nodes: List<GenericNestedPropsNode>,
    ) = runTest(StandardTestDispatcher()) {
        runScript("/generic-data.cypher")
        val result = repository.propertyNotInValuesNested(property, values)
        assertEquals(nodes.count(), result.count())
        nodes.forEach {
            result.contains(it)
        }
    }

}
