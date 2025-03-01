package com.thomas.database.neo4j.repository

import com.thomas.core.extension.toUUIDOrNull
import com.thomas.core.extension.unaccentedLower
import com.thomas.database.neo4j.node.StringNestedPropsNode
import com.thomas.database.neo4j.node.StringPropsNode
import java.util.UUID
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
            StringPropsNode(id = "8190ea93-7a86-4b73-8cad-80fbe997c4b5".toUUIDOrNull()!!, propName = "José Teixeira"),
            StringPropsNode(id = "63490ff1-b531-4405-9ee6-6c2a2c286ba9".toUUIDOrNull()!!, propName = "Josefa Dias"),
            StringPropsNode(id = "f10f988d-f714-4cbc-9d0c-d29a98af1b6b".toUUIDOrNull()!!, propName = "Jôseane Moreira"),
            StringPropsNode(id = "95ba9651-ff2e-469f-9202-e9a5e7abbc63".toUUIDOrNull()!!, propName = "Joseas Gomes"),
            StringPropsNode(id = "3dca88ed-ce60-4adb-9f4a-fe87d074685d".toUUIDOrNull()!!, propName = "Josefína Ribeiro"),
            StringPropsNode(id = "9f2ee658-956b-4d50-8ded-49fd04edd81a".toUUIDOrNull()!!, propName = "Maria José"),
            StringPropsNode(id = "fb0421ba-1b92-4722-92aa-de0591ad2ec1".toUUIDOrNull()!!, propName = "Leandro Joséas"),
            StringPropsNode(id = "ee3a2202-de4a-4847-aa83-aacb83e15b59".toUUIDOrNull()!!, propName = "Rejose Farias"),
            StringPropsNode(id = "6fd12324-eaa8-4736-a99b-2c5ed3d7b474".toUUIDOrNull()!!, propName = "Sojôseas Monteiro"),
            StringPropsNode(id = "f2a7c423-e20a-41c1-ab17-3a54a4333347".toUUIDOrNull()!!, propName = "josé teixeira"),
            StringPropsNode(id = "4fa86910-6424-4b53-8444-e8e5297f2707".toUUIDOrNull()!!, propName = "josefa dias"),
            StringPropsNode(id = "9c715ea4-0110-4016-9f93-6c34d796f92a".toUUIDOrNull()!!, propName = "jôseane moreira"),
            StringPropsNode(id = "fa5415df-7df9-4183-93f3-4c2f9b4e0ced".toUUIDOrNull()!!, propName = "joseas gomes"),
            StringPropsNode(id = "f781adab-d361-46e4-97ca-3a5fecaf80da".toUUIDOrNull()!!, propName = "josefína ribeiro"),
            StringPropsNode(id = "333556a1-38ee-413e-b9c6-73494cf40ea0".toUUIDOrNull()!!, propName = "maria josé"),
            StringPropsNode(id = "46016e93-a496-4252-8d88-4e05d86f3cbc".toUUIDOrNull()!!, propName = "leandro joséas"),
            StringPropsNode(id = "10f62941-be96-40aa-943b-0045ed7d71d6".toUUIDOrNull()!!, propName = "rejose farias"),
            StringPropsNode(id = "321a0267-2f21-4948-8048-d037b09662fd".toUUIDOrNull()!!, propName = "sojôseas monteiro"),
            StringPropsNode(id = "35c84255-542c-4ec4-a872-45b1c89dfcb7".toUUIDOrNull()!!, propName = "Aparecida Lima"),
            StringPropsNode(id = "f78f7495-eaf7-4e16-a9b5-7ca86e42aedc".toUUIDOrNull()!!, propName = "Beatriz dos Santos"),
            StringPropsNode(id = "5257f535-712f-4939-aa75-a08b879aa5b2".toUUIDOrNull()!!, propName = "Bruna Dias"),
            StringPropsNode(id = "10e91a84-e4ea-4114-83e1-4b1cc8e2d3bc".toUUIDOrNull()!!, propName = "Bruna Pereira"),
            StringPropsNode(id = "9fa5e064-c087-4183-96b5-d10d79788f49".toUUIDOrNull()!!, propName = "Camila Rodrigues"),
            StringPropsNode(id = "b5da7930-4e2d-48e4-a6f1-a615be294ec2".toUUIDOrNull()!!, propName = "Carla Alves"),
            StringPropsNode(id = "25012683-83c1-40b8-9c58-2237b7f785e4".toUUIDOrNull()!!, propName = "Cláudia Machado"),
            StringPropsNode(id = "cec55f5c-4efa-45c4-a606-e6867b433117".toUUIDOrNull()!!, propName = "Cristiane Gonçalves"),
            StringPropsNode(id = "e6e43c95-7557-47a5-8082-85bb5c7926d0".toUUIDOrNull()!!, propName = "Cristiane Santos"),
            StringPropsNode(id = "2023abfa-1622-400b-ba37-a164912e27c4".toUUIDOrNull()!!, propName = "Daniela Batista"),
            StringPropsNode(id = "b6a1d7d0-a602-42c2-9336-04f4cb1c9c04".toUUIDOrNull()!!, propName = "Luana de Souza"),
            StringPropsNode(id = "beb466be-f338-4a7a-9bb0-8f76d5f2d5b3".toUUIDOrNull()!!, propName = "Lúcia Fernandes"),
            StringPropsNode(id = "a5b0a4ad-6799-4ec7-9952-88c7aaedd363".toUUIDOrNull()!!, propName = "Vera Nunes"),
            StringPropsNode(id = "836b827f-986a-4fef-85da-55acb7a94698".toUUIDOrNull()!!, propName = "Vitória Batista"),
            StringPropsNode(id = "d703e421-3a88-4d27-830b-51a6b65281e2".toUUIDOrNull()!!, propName = "Patrícia Soares"),
        )

        private val NESTEDS = listOf(
            StringNestedPropsNode(id = "81e3aa04-f895-4177-b623-7a1612ed382d".toUUIDOrNull()!!, nestedNode = NODES[0]),
            StringNestedPropsNode(id = "3c73b9c1-bf8f-4d2d-bf9e-cfd1de8e4755".toUUIDOrNull()!!, nestedNode = NODES[1]),
            StringNestedPropsNode(id = "3937fa51-9ec6-46a5-81b5-c62a0609b5aa".toUUIDOrNull()!!, nestedNode = NODES[2]),
            StringNestedPropsNode(id = "21d9ecf1-eaca-42cf-8e1d-42c664a98859".toUUIDOrNull()!!, nestedNode = NODES[3]),
            StringNestedPropsNode(id = "8074540a-b9c1-4072-bacc-2ed78750fd05".toUUIDOrNull()!!, nestedNode = NODES[4]),
            StringNestedPropsNode(id = "fdf2af54-2a69-4013-a253-953c387c9bba".toUUIDOrNull()!!, nestedNode = NODES[5]),
            StringNestedPropsNode(id = "22dffacd-2d4b-420f-9fd4-ca811bb4185a".toUUIDOrNull()!!, nestedNode = NODES[6]),
            StringNestedPropsNode(id = "e4b38d7a-5ab5-4531-8c4f-91bc5538bdb5".toUUIDOrNull()!!, nestedNode = NODES[7]),
            StringNestedPropsNode(id = "9d6d9d8d-d7f9-4cc1-a10b-862eb4dad8b9".toUUIDOrNull()!!, nestedNode = NODES[8]),
            StringNestedPropsNode(id = "fb2ebb1a-1b90-4808-85c9-e5c571e2829b".toUUIDOrNull()!!, nestedNode = NODES[9]),
            StringNestedPropsNode(id = "67cb7ed4-2db3-43cf-87b2-bd799cdca146".toUUIDOrNull()!!, nestedNode = NODES[10]),
            StringNestedPropsNode(id = "c2d1c580-a020-466d-b605-a2dc39ee784d".toUUIDOrNull()!!, nestedNode = NODES[11]),
            StringNestedPropsNode(id = "37f2fa9d-2b81-4a27-a8ce-b6609c55bf83".toUUIDOrNull()!!, nestedNode = NODES[12]),
            StringNestedPropsNode(id = "8148bdf9-a378-4b4b-b682-17252d99a0a1".toUUIDOrNull()!!, nestedNode = NODES[13]),
            StringNestedPropsNode(id = "86c85973-090e-4fd8-bd64-be986361a7f0".toUUIDOrNull()!!, nestedNode = NODES[14]),
            StringNestedPropsNode(id = "768eb187-d95a-4600-b345-cbcf28c4e308".toUUIDOrNull()!!, nestedNode = NODES[15]),
            StringNestedPropsNode(id = "19ff6270-4b8d-4cde-b01d-380d080f821c".toUUIDOrNull()!!, nestedNode = NODES[16]),
            StringNestedPropsNode(id = "824544ba-bdbe-4465-8ee1-af6c971cbf58".toUUIDOrNull()!!, nestedNode = NODES[17]),
            StringNestedPropsNode(id = "2ba92c43-d4fa-419f-92f3-47152b503197".toUUIDOrNull()!!, nestedNode = NODES[18]),
            StringNestedPropsNode(id = "e3c5d2c4-036c-49af-80ac-489afbc7a619".toUUIDOrNull()!!, nestedNode = NODES[19]),
            StringNestedPropsNode(id = "5f0734b0-0d1d-44b9-83eb-9554d298b892".toUUIDOrNull()!!, nestedNode = NODES[20]),
            StringNestedPropsNode(id = "af6d71c0-3c42-4793-ac50-c9bfa379a638".toUUIDOrNull()!!, nestedNode = NODES[21]),
            StringNestedPropsNode(id = "8a3ac199-22ee-4b30-813e-5f028c93f5bc".toUUIDOrNull()!!, nestedNode = NODES[22]),
            StringNestedPropsNode(id = "cc97497e-dd4f-40ee-830d-56ebfc6b84ce".toUUIDOrNull()!!, nestedNode = NODES[23]),
            StringNestedPropsNode(id = "392752ea-faeb-469e-9e77-d731364c2347".toUUIDOrNull()!!, nestedNode = NODES[24]),
            StringNestedPropsNode(id = "1627345e-fbf9-42dd-a991-f3c8b1cdf5fd".toUUIDOrNull()!!, nestedNode = NODES[25]),
            StringNestedPropsNode(id = "68d73830-cc41-4fd7-b478-3f79eacb5dd5".toUUIDOrNull()!!, nestedNode = NODES[26]),
            StringNestedPropsNode(id = "5753a67f-e145-4691-9153-826dee0e1dac".toUUIDOrNull()!!, nestedNode = NODES[27]),
            StringNestedPropsNode(id = "04f99654-e585-45d6-9bfd-a1cb93901d05".toUUIDOrNull()!!, nestedNode = NODES[28]),
            StringNestedPropsNode(id = "49bb77ed-a944-4a06-b891-c20ef1aa43c9".toUUIDOrNull()!!, nestedNode = NODES[29]),
            StringNestedPropsNode(id = "5ed34ca6-a278-42fd-9718-e6bc6102dc42".toUUIDOrNull()!!, nestedNode = NODES[30]),
            StringNestedPropsNode(id = "3b979ccf-5ad4-4958-b885-b345a43facc1".toUUIDOrNull()!!, nestedNode = NODES[31]),
            StringNestedPropsNode(id = "e5c88f32-ae51-457c-9be1-3abafdce91f3".toUUIDOrNull()!!, nestedNode = NODES[32]),
        )

        @JvmStatic
        fun names() = listOf(
            Arguments.of("38e93cf3-ce63-4759-a2af-57c3113a2267".toUUIDOrNull()!!, "Antonia Batista"),
            Arguments.of("2ed8297a-e2bd-4bcd-b229-8f0da72467b0".toUUIDOrNull()!!, "Antônia Batista"),
            Arguments.of("08141fdc-9c1e-4d95-9e23-6d96d9c1ec8e".toUUIDOrNull()!!, "antônia batista"),
        )

        @JvmStatic
        fun namesNested() = listOf(
            Arguments.of("5f510e8f-f429-4b07-aa21-a94eef462a50".toUUIDOrNull()!!, "38e93cf3-ce63-4759-a2af-57c3113a2267".toUUIDOrNull()!!, "Antonia Batista"),
            Arguments.of("ae5f0143-dbec-499e-9f24-f7db86b89562".toUUIDOrNull()!!, "2ed8297a-e2bd-4bcd-b229-8f0da72467b0".toUUIDOrNull()!!, "Antônia Batista"),
            Arguments.of("d104a2ed-d4b2-4a45-b13d-2bb335f2fd15".toUUIDOrNull()!!, "08141fdc-9c1e-4d95-9e23-6d96d9c1ec8e".toUUIDOrNull()!!, "antônia batista"),
        )

    }

    @ParameterizedTest
    @MethodSource("names")
    fun `String equals`(id: UUID, name: String) = runTest(StandardTestDispatcher()) {
        runScript("/string-equals.cypher")
        val result = repository.stringEquals(StringPropsNode::propName, name)
        assertEquals(1, result.count())
        assertEquals(id, result.first().id)
        assertEquals(name, result.first().propName)
    }

    @ParameterizedTest
    @MethodSource("namesNested")
    fun `String equals nested`(id: UUID, nested: UUID, name: String) = runTest(StandardTestDispatcher()) {
        runScript("/string-equals.cypher")
        val result = repository.stringEqualsNested(StringPropsNode::propName, name)
        assertEquals(1, result.count())
        assertEquals(id, result.first().id)
        assertEquals(nested, result.first().nestedNode.id)
        assertEquals(name, result.first().nestedNode.propName)
    }

    @ParameterizedTest
    @MethodSource("names")
    fun `String not equals`(id: UUID, name: String) = runTest(StandardTestDispatcher()) {
        runScript("/string-equals.cypher")
        val result = repository.stringNotEquals(StringPropsNode::propName, name)
        assertEquals(2, result.count())
        assertFalse(result.any { it.id == id })
        assertFalse(result.any { it.propName == name })
    }

    @ParameterizedTest
    @MethodSource("namesNested")
    fun `String not equals nested`(id: UUID, nested: UUID, name: String) = runTest(StandardTestDispatcher()) {
        runScript("/string-equals.cypher")
        val result = repository.stringNotEqualsNested(StringPropsNode::propName, name)
        assertEquals(2, result.count())
        assertFalse(result.any { it.id == id })
        assertFalse(result.any { it.nestedNode.id == nested })
        assertFalse(result.any { it.nestedNode.propName == name })
    }

    @Test
    fun `String equals unaccented lower`() = runTest(StandardTestDispatcher()) {
        runScript("/string-equals.cypher")
        val result = repository.stringEqualsUnaccentedLower(StringPropsNode::propName, "Antônia Batista")
        assertEquals(3, result.count())

        names().forEach { arg ->
            assertTrue(result.any { it.id == arg.get()[0] })
            assertTrue(result.any { it.propName == arg.get()[1] })
        }
    }

    @Test
    fun `String equals unaccented lower nested`() = runTest(StandardTestDispatcher()) {
        runScript("/string-equals.cypher")
        val result = repository.stringEqualsUnaccentedLowerNested(StringPropsNode::propName, "Antônia Batista")
        assertEquals(3, result.count())

        namesNested().forEach { arg ->
            assertTrue(result.any { it.id == arg.get()[0] })
            assertTrue(result.any { it.nestedNode.id == arg.get()[1] })
            assertTrue(result.any { it.nestedNode.propName == arg.get()[2] })
        }
    }

    @Test
    fun `String not equals unaccented lower`() = runTest(StandardTestDispatcher()) {
        runScript("/string-equals.cypher")
        val result = repository.stringNotEqualsUnaccentedLower(StringPropsNode::propName, "Antônia Batista")
        assertEquals(0, result.count())
    }

    @Test
    fun `String not equals unaccented lower nested`() = runTest(StandardTestDispatcher()) {
        runScript("/string-equals.cypher")
        val result = repository.stringNotEqualsUnaccentedLowerNested(StringPropsNode::propName, "Antônia Batista")
        assertEquals(0, result.count())
    }

    @Test
    fun `String like`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { it.propName.unaccentedLower().contains("José") }
        val result = repository.stringLike(StringPropsNode::propName, "José")
        assertEquals(3, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String like nested`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NESTEDS.filter { it.nestedNode.propName.unaccentedLower().contains("José") }
        val result = repository.stringLikeNested(StringPropsNode::propName, "José")
        assertEquals(3, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String not like`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { !it.propName.contains("José") }
        val result = repository.stringNotLike(StringPropsNode::propName, "José")
        assertEquals(30, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String not like nested`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NESTEDS.filter { !it.nestedNode.propName.contains("José") }
        val result = repository.stringNotLikeNested(StringPropsNode::propName, "José")
        assertEquals(30, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String like unaccented lower`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { it.propName.unaccentedLower().contains("jose") }
        val result = repository.stringLikeUnaccentedLower(StringPropsNode::propName, "José")
        assertEquals(18, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String like unaccented lower nested`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NESTEDS.filter { it.nestedNode.propName.unaccentedLower().contains("jose") }
        val result = repository.stringLikeUnaccentedLowerNested(StringPropsNode::propName, "José")
        assertEquals(18, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String not like unaccented lower`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { !it.propName.unaccentedLower().contains("jose") }
        val result = repository.stringNotLikeUnaccentedLower(StringPropsNode::propName, "José")
        assertEquals(nodes.count(), result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String not like unaccented lower nested`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NESTEDS.filter { !it.nestedNode.propName.unaccentedLower().contains("jose") }
        val result = repository.stringNotLikeUnaccentedLowerNested(StringPropsNode::propName, "José")
        assertEquals(nodes.count(), result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String starts with`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { it.propName.startsWith("José") }
        val result = repository.stringStartsWith(StringPropsNode::propName, "José")
        assertEquals(1, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String starts with nested`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NESTEDS.filter { it.nestedNode.propName.startsWith("José") }
        val result = repository.stringStartsWithNested(StringPropsNode::propName, "José")
        assertEquals(1, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String not starts with`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { !it.propName.startsWith("José") }
        val result = repository.stringNotStartsWith(StringPropsNode::propName, "José")
        assertEquals(32, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String not starts with nested`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NESTEDS.filter { !it.nestedNode.propName.startsWith("José") }
        val result = repository.stringNotStartsWithNested(StringPropsNode::propName, "José")
        assertEquals(32, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String starts with unaccented lower`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { it.propName.unaccentedLower().startsWith("jose") }
        val result = repository.stringStartsWithUnaccentedLower(StringPropsNode::propName, "José")
        assertEquals(10, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String starts with unaccented lower nested`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NESTEDS.filter { it.nestedNode.propName.unaccentedLower().startsWith("jose") }
        val result = repository.stringStartsWithUnaccentedLowerNested(StringPropsNode::propName, "José")
        assertEquals(10, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String not starts with unaccented lower`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { !it.propName.unaccentedLower().startsWith("jose") }
        val result = repository.stringNotStartsWithUnaccentedLower(StringPropsNode::propName, "José")
        assertEquals(23, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String not starts with unaccented lower nested`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NESTEDS.filter { !it.nestedNode.propName.unaccentedLower().startsWith("jose") }
        val result = repository.stringNotStartsWithUnaccentedLowerNested(StringPropsNode::propName, "José")
        assertEquals(23, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String ends with`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { it.propName.endsWith("José") }
        val result = repository.stringEndsWith(StringPropsNode::propName, "José")
        assertEquals(1, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String ends with nested`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NESTEDS.filter { it.nestedNode.propName.endsWith("José") }
        val result = repository.stringEndsWithNested(StringPropsNode::propName, "José")
        assertEquals(1, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String not ends with`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { !it.propName.endsWith("José") }
        val result = repository.stringNotEndsWith(StringPropsNode::propName, "José")
        assertEquals(32, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String not ends with nested`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NESTEDS.filter { !it.nestedNode.propName.endsWith("José") }
        val result = repository.stringNotEndsWithNested(StringPropsNode::propName, "José")
        assertEquals(32, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String ends with unaccented lower`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { it.propName.unaccentedLower().endsWith("jose") }
        val result = repository.stringEndsWithUnaccentedLower(StringPropsNode::propName, "José")
        assertEquals(2, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String ends with unaccented lower nested`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NESTEDS.filter { it.nestedNode.propName.unaccentedLower().endsWith("jose") }
        val result = repository.stringEndsWithUnaccentedLowerNested(StringPropsNode::propName, "José")
        assertEquals(2, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String not ends with unaccented lower`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NODES.filter { !it.propName.unaccentedLower().endsWith("jose") }
        val result = repository.stringNotEndsWithUnaccentedLower(StringPropsNode::propName, "José")
        assertEquals(31, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @Test
    fun `String not ends with unaccented lower nested`() = runTest(StandardTestDispatcher()) {
        runScript("/string-like.cypher")
        val nodes = NESTEDS.filter { !it.nestedNode.propName.unaccentedLower().endsWith("jose") }
        val result = repository.stringNotEndsWithUnaccentedLowerNested(StringPropsNode::propName, "José")
        assertEquals(31, result.count())

        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

}
