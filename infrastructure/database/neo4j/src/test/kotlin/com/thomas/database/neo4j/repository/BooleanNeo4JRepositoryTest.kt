package com.thomas.database.neo4j.repository

import com.thomas.core.extension.toUUIDOrNull
import com.thomas.database.neo4j.node.BooleanNestedPropsNode
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
            BooleanPropsNode(id = "78549195-822d-46a6-87c7-137a5e43351a".toUUIDOrNull()!!, propBoolean = true),
            BooleanPropsNode(id = "95a34b09-6b6c-4f67-af40-9b9d0029abd6".toUUIDOrNull()!!, propBoolean = true),
            BooleanPropsNode(id = "cd4101ac-0d7c-4d57-9b21-aa6d03c7a33d".toUUIDOrNull()!!, propBoolean = false),
        )

        private val NESTED = listOf(
            BooleanNestedPropsNode(id = "fc7a2d80-8da1-4721-859c-a4b8d222f7f9".toUUIDOrNull()!!, propBoolean = false, nestedNode = NODES[0]),
            BooleanNestedPropsNode(id = "d8247938-193c-4013-944b-baf952193508".toUUIDOrNull()!!, propBoolean = true, nestedNode = NODES[1]),
            BooleanNestedPropsNode(id = "6c379fd8-efe7-48b6-af9c-aea4c2e4af08".toUUIDOrNull()!!, propBoolean = true, nestedNode = NODES[2]),
        )

        private val isTrue: suspend TestNeo4JRepository.() -> List<BooleanPropsNode> = {
            this.booleanIsTrue(BooleanPropsNode::propBoolean)
        }

        private val isFalse: suspend TestNeo4JRepository.() -> List<BooleanPropsNode> = {
            this.booleanIsFalse(BooleanPropsNode::propBoolean)
        }

        @JvmStatic
        fun booleanValues() = listOf(
            Arguments.of(NODES.filter { it.propBoolean }, isTrue),
            Arguments.of(NODES.filter { !it.propBoolean }, isFalse),
        )

        @JvmStatic
        fun booleanNestedValues() = listOf(
            (true to true).let { pair ->
                Arguments.of(
                    pair.first,
                    pair.second,
                    NESTED.filter { it.propBoolean == pair.first && it.nestedNode.propBoolean == pair.second },

                    )
            },
            (false to false).let { pair ->
                Arguments.of(
                    pair.first,
                    pair.second,
                    NESTED.filter { it.propBoolean == pair.first && it.nestedNode.propBoolean == pair.second },

                    )
            },
            (true to false).let { pair ->
                Arguments.of(
                    pair.first,
                    pair.second,
                    NESTED.filter { it.propBoolean == pair.first && it.nestedNode.propBoolean == pair.second },
                )
            },
            (false to true).let { pair ->
                Arguments.of(
                    pair.first,
                    pair.second,
                    NESTED.filter { it.propBoolean == pair.first && it.nestedNode.propBoolean == pair.second },
                )
            },
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

    @ParameterizedTest
    @MethodSource("booleanNestedValues")
    fun `Boolean nested filter`(
        propBoolean: Boolean,
        nestedBoolean: Boolean,
        nodes: List<BooleanNestedPropsNode>,
    ) = runTest(StandardTestDispatcher()) {
        runScript("/boolean-equals-nested.cypher")
        val result = repository.booleanNestedIs(
            BooleanNestedPropsNode::propBoolean,
            propBoolean,
            BooleanPropsNode::propBoolean,
            BooleanNestedPropsNode::nestedNode,
            nestedBoolean
        )
        assertEquals(nodes.count(), result.count())
        nodes.forEach {
            result.contains(it)
        }
    }

}
