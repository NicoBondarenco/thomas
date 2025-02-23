package com.thomas.database.neo4j.repository

import com.thomas.database.neo4j.node.BooleanPropsNode
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


class BooleanNeo4JRepositoryTest : Neo4JRepositoryTest() {

    companion object {

        private val NODES = listOf(
            BooleanPropsNode(id = "78549195-822d-46a6-87c7-137a5e43351a", propBoolean = true),
            BooleanPropsNode(id = "95a34b09-6b6c-4f67-af40-9b9d0029abd6", propBoolean = true),
            BooleanPropsNode(id = "cd4101ac-0d7c-4d57-9b21-aa6d03c7a33d", propBoolean = false),
        )

        private val isTrue: suspend TestNeo4JRepository.() -> List<BooleanPropsNode> = {
            this.booleanIsTrue("prop_boolean")
        }

        private val isFalse: suspend TestNeo4JRepository.() -> List<BooleanPropsNode> = {
            this.booleanIsFalse("prop_boolean")
        }

        @JvmStatic
        fun booleanValues() = listOf(
            Arguments.of(NODES.filter { it.propBoolean }, isTrue),
            Arguments.of(NODES.filter { !it.propBoolean }, isFalse),
        )

    }

    @ParameterizedTest
    @MethodSource("booleanValues")
    fun `Boolean is true`(
        nodes: List<BooleanPropsNode>,
        function: suspend TestNeo4JRepository.() -> List<BooleanPropsNode>,
    ) = runTest(StandardTestDispatcher()) {
        runScript("/boolean-equals.cypher")
        val result = repository.function()
        assertEquals(nodes.count(), result.count())
        nodes.forEach {
            result.contains(it)
        }
    }

}
