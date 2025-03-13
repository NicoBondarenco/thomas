package com.thomas.database.neo4j.repository

import com.thomas.core.extension.toUUIDOrNull
import com.thomas.database.neo4j.filter.isEquals
import com.thomas.database.neo4j.filter.inValues
import com.thomas.database.neo4j.filter.isNotNull
import com.thomas.database.neo4j.filter.isTrue
import com.thomas.database.neo4j.node.ComplexPropsNode
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class ComplexNeo4JRepositoryTest : Neo4JRepositoryTest() {

    companion object {

        private val NODES = listOf(
            ComplexPropsNode(id = "9f441bce-3f08-4f61-af4c-ac4ac4b94ae1".toUUIDOrNull()!!, propName = "Antonia", propValue = 10, propDatetime = ZonedDateTime.parse("2025-01-01T03:33:47.996854Z", ISO_ZONED_DATE_TIME), propBoolean = true),
            ComplexPropsNode(id = "9b97d8b2-fa88-45bf-bf36-4bce21a904b4".toUUIDOrNull()!!, propName = "Antônia", propValue = 50, propDatetime = null, propBoolean = true),
            ComplexPropsNode(id = "1ab03503-6090-4426-bc06-c2fafa961d40".toUUIDOrNull()!!, propName = "antônia", propValue = null, propDatetime = ZonedDateTime.parse("2025-01-01T23:59:59.999999Z", ISO_ZONED_DATE_TIME), propBoolean = null),
            ComplexPropsNode(id = "408bf7fe-41c4-422a-a334-ddeef8284e28".toUUIDOrNull()!!, propName = null, propValue = 17, propDatetime = ZonedDateTime.parse("2025-01-02T05:00:00.000000Z", ISO_ZONED_DATE_TIME), propBoolean = null),
            ComplexPropsNode(id = "cfb865c2-a0b8-4772-9076-7b4be72e0f3f".toUUIDOrNull()!!, propName = "José", propValue = 25, propDatetime = ZonedDateTime.parse("2025-01-02T08:01:54.115335Z", ISO_ZONED_DATE_TIME), propBoolean = true),
            ComplexPropsNode(id = "304e6c34-15ef-40e6-925f-6d62fbd3b964".toUUIDOrNull()!!, propName = "Maria", propValue = null, propDatetime = ZonedDateTime.parse("2025-01-02T19:48:13.710658Z", ISO_ZONED_DATE_TIME), propBoolean = true),
            ComplexPropsNode(id = "08cad7a9-0158-409d-bcf1-abf6207acfff".toUUIDOrNull()!!, propName = null, propValue = 25, propDatetime = ZonedDateTime.parse("2025-01-02T22:27:35.265104Z", ISO_ZONED_DATE_TIME), propBoolean = false),
            ComplexPropsNode(id = "0c50426d-fe5b-459e-9e40-490f09162a43".toUUIDOrNull()!!, propName = null, propValue = null, propDatetime = ZonedDateTime.parse("2025-01-03T16:48:10.033296Z", ISO_ZONED_DATE_TIME), propBoolean = null),
            ComplexPropsNode(id = "2ea2e162-0a8a-44c0-861d-ff093f04e27b".toUUIDOrNull()!!, propName = "josé", propValue = 99, propDatetime = ZonedDateTime.parse("2025-01-03T18:14:37.283850Z", ISO_ZONED_DATE_TIME), propBoolean = false),
            ComplexPropsNode(id = "079d5151-40c5-468a-9f93-24429fd6dc2a".toUUIDOrNull()!!, propName = "jose", propValue = 44, propDatetime = null, propBoolean = false),
        )

    }

    @Test
    fun `Complex filter and`() = runTest(StandardTestDispatcher()) {
        runScript("/complex-data.cypher")

        val names = listOf("José", null)
        val nodes = NODES.filter {
            names.contains(it.propName) && it.propValue == 25 && it.propDatetime != null && it.propBoolean == true
        }
        val result = repository.complexFilterSearch(
            listOf(inValues(ComplexPropsNode::propName, names), isEquals(ComplexPropsNode::propValue, 25), isNotNull(ComplexPropsNode::propDatetime), isTrue(ComplexPropsNode::propBoolean)),
        )
        assertEquals(nodes.count(), result.count())
        nodes.forEach {
            result.contains(it)
        }
    }

}
