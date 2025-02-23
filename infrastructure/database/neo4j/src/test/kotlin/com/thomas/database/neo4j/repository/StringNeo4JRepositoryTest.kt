package com.thomas.database.neo4j.repository

import com.thomas.core.extension.unaccentedLower
import com.thomas.database.neo4j.node.StringPropsNode
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


class StringNeo4JRepositoryTest : Neo4JRepositoryTest() {

    companion object {

        private val NODES = listOf(
            StringPropsNode(id = "8190ea93-7a86-4b73-8cad-80fbe997c4b5", propName = "José Teixeira"),
            StringPropsNode(id = "63490ff1-b531-4405-9ee6-6c2a2c286ba9", propName = "Josefa Dias"),
            StringPropsNode(id = "f10f988d-f714-4cbc-9d0c-d29a98af1b6b", propName = "Jôseane Moreira"),
            StringPropsNode(id = "95ba9651-ff2e-469f-9202-e9a5e7abbc63", propName = "Joseas Gomes"),
            StringPropsNode(id = "3dca88ed-ce60-4adb-9f4a-fe87d074685d", propName = "Josefína Ribeiro"),
            StringPropsNode(id = "9f2ee658-956b-4d50-8ded-49fd04edd81a", propName = "Maria José"),
            StringPropsNode(id = "fb0421ba-1b92-4722-92aa-de0591ad2ec1", propName = "Leandro Joséas"),
            StringPropsNode(id = "ee3a2202-de4a-4847-aa83-aacb83e15b59", propName = "Rejose Farias"),
            StringPropsNode(id = "6fd12324-eaa8-4736-a99b-2c5ed3d7b474", propName = "Sojôseas Monteiro"),
            StringPropsNode(id = "f2a7c423-e20a-41c1-ab17-3a54a4333347", propName = "josé teixeira"),
            StringPropsNode(id = "4fa86910-6424-4b53-8444-e8e5297f2707", propName = "josefa dias"),
            StringPropsNode(id = "9c715ea4-0110-4016-9f93-6c34d796f92a", propName = "jôseane moreira"),
            StringPropsNode(id = "fa5415df-7df9-4183-93f3-4c2f9b4e0ced", propName = "joseas gomes"),
            StringPropsNode(id = "f781adab-d361-46e4-97ca-3a5fecaf80da", propName = "josefína ribeiro"),
            StringPropsNode(id = "333556a1-38ee-413e-b9c6-73494cf40ea0", propName = "maria josé"),
            StringPropsNode(id = "46016e93-a496-4252-8d88-4e05d86f3cbc", propName = "leandro joséas"),
            StringPropsNode(id = "10f62941-be96-40aa-943b-0045ed7d71d6", propName = "rejose farias"),
            StringPropsNode(id = "321a0267-2f21-4948-8048-d037b09662fd", propName = "sojôseas monteiro"),
            StringPropsNode(id = "35c84255-542c-4ec4-a872-45b1c89dfcb7", propName = "Aparecida Lima"),
            StringPropsNode(id = "f78f7495-eaf7-4e16-a9b5-7ca86e42aedc", propName = "Beatriz dos Santos"),
            StringPropsNode(id = "5257f535-712f-4939-aa75-a08b879aa5b2", propName = "Bruna Dias"),
            StringPropsNode(id = "10e91a84-e4ea-4114-83e1-4b1cc8e2d3bc", propName = "Bruna Pereira"),
            StringPropsNode(id = "9fa5e064-c087-4183-96b5-d10d79788f49", propName = "Camila Rodrigues"),
            StringPropsNode(id = "b5da7930-4e2d-48e4-a6f1-a615be294ec2", propName = "Carla Alves"),
            StringPropsNode(id = "25012683-83c1-40b8-9c58-2237b7f785e4", propName = "Cláudia Machado"),
            StringPropsNode(id = "cec55f5c-4efa-45c4-a606-e6867b433117", propName = "Cristiane Gonçalves"),
            StringPropsNode(id = "e6e43c95-7557-47a5-8082-85bb5c7926d0", propName = "Cristiane Santos"),
            StringPropsNode(id = "2023abfa-1622-400b-ba37-a164912e27c4", propName = "Daniela Batista"),
            StringPropsNode(id = "b6a1d7d0-a602-42c2-9336-04f4cb1c9c04", propName = "Luana de Souza"),
            StringPropsNode(id = "beb466be-f338-4a7a-9bb0-8f76d5f2d5b3", propName = "Lúcia Fernandes"),
            StringPropsNode(id = "a5b0a4ad-6799-4ec7-9952-88c7aaedd363", propName = "Vera Nunes"),
            StringPropsNode(id = "836b827f-986a-4fef-85da-55acb7a94698", propName = "Vitória Batista"),
            StringPropsNode(id = "d703e421-3a88-4d27-830b-51a6b65281e2", propName = "Patrícia Soares"),
        )

        @JvmStatic
        fun names() = listOf(
            Arguments.of("38e93cf3-ce63-4759-a2af-57c3113a2267", "Antonia Batista"),
            Arguments.of("2ed8297a-e2bd-4bcd-b229-8f0da72467b0", "Antônia Batista"),
            Arguments.of("08141fdc-9c1e-4d95-9e23-6d96d9c1ec8e", "antônia batista"),
        )

    }

    @ParameterizedTest
    @MethodSource("names")
    fun `String equals`(id: String, name: String) = runTest(StandardTestDispatcher()) {
        runScript("/string-equals.cypher")
        val result = repository.stringEquals(name)
        assertEquals(1, result.count())
        assertEquals(id, result.first().id)
        assertEquals(name, result.first().propName)
    }

    @ParameterizedTest
    @MethodSource("names")
    fun `String not equals`(id: String, name: String) = runTest(StandardTestDispatcher()) {
        runScript("/string-equals.cypher")
        val result = repository.stringNotEquals(name)
        assertEquals(2, result.count())
        assertFalse(result.any { it.id == id })
        assertFalse(result.any { it.propName == name })
    }

    @Test
    fun `String equals unaccented lower`() = runTest(StandardTestDispatcher()) {
        runScript("/string-equals.cypher")
        val result = repository.stringEqualsUnaccentedLower("Antônia Batista")
        assertEquals(3, result.count())

        names().forEach { arg ->
            assertTrue(result.any { it.id == arg.get()[0] })
            assertTrue(result.any { it.propName == arg.get()[1] })
        }
    }

    @Test
    fun `String not equals unaccented lower`() = runTest(StandardTestDispatcher()) {
        runScript("/string-equals.cypher")
        val result = repository.stringNotEqualsUnaccentedLower("Antônia Batista")
        assertEquals(0, result.count())
    }

    @Test
    fun `String like`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { it.propName.unaccentedLower().contains("José") }
        val result = repository.stringLike("José")
        assertEquals(3, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String not like`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { !it.propName.contains("José") }
        val result = repository.stringNotLike("José")
        assertEquals(30, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String like unaccented lower`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { it.propName.unaccentedLower().contains("jose") }
        val result = repository.stringLikeUnaccentedLower("José")
        assertEquals(18, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String not like unaccented lower`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { !it.propName.unaccentedLower().contains("jose") }
        val result = repository.stringNotLikeUnaccentedLower("José")
        assertEquals(nodes.count(), result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String starts with`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { it.propName.startsWith("José") }
        val result = repository.stringStartsWith("José")
        assertEquals(1, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String not starts with`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { !it.propName.startsWith("José") }
        val result = repository.stringNotStartsWith("José")
        assertEquals(32, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String starts with unaccented lower`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { it.propName.unaccentedLower().startsWith("jose") }
        val result = repository.stringStartsWithUnaccentedLower("José")
        assertEquals(10, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String not starts with unaccented lower`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { !it.propName.unaccentedLower().startsWith("jose") }
        val result = repository.stringNotStartsWithUnaccentedLower("José")
        assertEquals(23, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String ends with`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { it.propName.endsWith("José") }
        val result = repository.stringEndsWith("José")
        assertEquals(1, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String not ends with`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { !it.propName.endsWith("José") }
        val result = repository.stringNotEndsWith("José")
        assertEquals(32, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String ends with unaccented lower`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { it.propName.unaccentedLower().endsWith("jose") }
        val result = repository.stringEndsWithUnaccentedLower("José")
        assertEquals(2, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String not ends with unaccented lower`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { !it.propName.unaccentedLower().endsWith("jose") }
        val result = repository.stringNotEndsWithUnaccentedLower("José")
        assertEquals(31, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

}
