package com.thomas.database.neo4j.repository

import com.thomas.core.extension.toUUIDOrNull
import com.thomas.database.neo4j.node.DatetimeNestedPropsNode
import com.thomas.database.neo4j.node.DatetimePropsNode
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.time.format.DateTimeFormatter.ISO_LOCAL_TIME
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
import java.time.format.DateTimeFormatter.ISO_OFFSET_TIME
import java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME
import java.time.temporal.Temporal
import kotlin.reflect.KProperty
import kotlin.test.assertTrue
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


class DateTimeNeo4JRepositoryTest : Neo4JRepositoryTest() {

    companion object {

        private val NODES = listOf(
            DatetimePropsNode(id = "c32aa911-ff7d-44b2-b736-7cc8ce28ec73".toUUIDOrNull()!!, offsetDatetime = OffsetDateTime.parse("2025-01-01T03:33:47.996854Z", ISO_OFFSET_DATE_TIME), offsetTime = OffsetTime.parse("11:04:21.820664Z", ISO_OFFSET_TIME), zonedDatetime = ZonedDateTime.parse("2025-01-01T03:33:47.996854Z", ISO_ZONED_DATE_TIME), localDatetime = LocalDateTime.parse("2025-01-01T03:33:47.996854", ISO_LOCAL_DATE_TIME), localDate = LocalDate.parse("2025-01-01", ISO_LOCAL_DATE), localTime = LocalTime.parse("11:04:21.820664", ISO_LOCAL_TIME)),
            DatetimePropsNode(id = "d34ccbfc-f5fd-4944-8b7e-5e14011aab1d".toUUIDOrNull()!!, offsetDatetime = OffsetDateTime.parse("2025-01-01T14:05:29.096345Z", ISO_OFFSET_DATE_TIME), offsetTime = OffsetTime.parse("02:00:06.723208Z", ISO_OFFSET_TIME), zonedDatetime = ZonedDateTime.parse("2025-01-01T14:05:29.096345Z", ISO_ZONED_DATE_TIME), localDatetime = LocalDateTime.parse("2025-01-01T14:05:29.096345", ISO_LOCAL_DATE_TIME), localDate = LocalDate.parse("2025-01-01", ISO_LOCAL_DATE), localTime = LocalTime.parse("02:00:06.723208", ISO_LOCAL_TIME)),
            DatetimePropsNode(id = "9656d2ed-037b-4c7a-8fe7-f6d23880eba0".toUUIDOrNull()!!, offsetDatetime = OffsetDateTime.parse("2025-01-01T23:59:59.999999Z", ISO_OFFSET_DATE_TIME), offsetTime = OffsetTime.parse("08:03:54.489223Z", ISO_OFFSET_TIME), zonedDatetime = ZonedDateTime.parse("2025-01-01T23:59:59.999999Z", ISO_ZONED_DATE_TIME), localDatetime = LocalDateTime.parse("2025-01-01T23:59:59.999999", ISO_LOCAL_DATE_TIME), localDate = LocalDate.parse("2025-01-01", ISO_LOCAL_DATE), localTime = LocalTime.parse("08:03:54.489223", ISO_LOCAL_TIME)),
            DatetimePropsNode(id = "51789339-f446-4324-a830-e1039b88c7d4".toUUIDOrNull()!!, offsetDatetime = OffsetDateTime.parse("2025-01-02T05:00:00.000000Z", ISO_OFFSET_DATE_TIME), offsetTime = OffsetTime.parse("01:36:37.941997Z", ISO_OFFSET_TIME), zonedDatetime = ZonedDateTime.parse("2025-01-02T05:00:00.000000Z", ISO_ZONED_DATE_TIME), localDatetime = LocalDateTime.parse("2025-01-02T05:00:00.000000", ISO_LOCAL_DATE_TIME), localDate = LocalDate.parse("2025-01-02", ISO_LOCAL_DATE), localTime = LocalTime.parse("01:36:37.941997", ISO_LOCAL_TIME)),
            DatetimePropsNode(id = "19cee4b0-d870-4323-b3fd-824912c91228".toUUIDOrNull()!!, offsetDatetime = OffsetDateTime.parse("2025-01-02T08:01:54.115335Z", ISO_OFFSET_DATE_TIME), offsetTime = OffsetTime.parse("21:57:44.375780Z", ISO_OFFSET_TIME), zonedDatetime = ZonedDateTime.parse("2025-01-02T08:01:54.115335Z", ISO_ZONED_DATE_TIME), localDatetime = LocalDateTime.parse("2025-01-02T08:01:54.115335", ISO_LOCAL_DATE_TIME), localDate = LocalDate.parse("2025-01-02", ISO_LOCAL_DATE), localTime = LocalTime.parse("21:57:44.375780", ISO_LOCAL_TIME)),
            DatetimePropsNode(id = "90ea543b-543f-493d-a980-9e75c201c264".toUUIDOrNull()!!, offsetDatetime = OffsetDateTime.parse("2025-01-02T19:48:13.710658Z", ISO_OFFSET_DATE_TIME), offsetTime = OffsetTime.parse("03:06:55.912321Z", ISO_OFFSET_TIME), zonedDatetime = ZonedDateTime.parse("2025-01-02T19:48:13.710658Z", ISO_ZONED_DATE_TIME), localDatetime = LocalDateTime.parse("2025-01-02T19:48:13.710658", ISO_LOCAL_DATE_TIME), localDate = LocalDate.parse("2025-01-02", ISO_LOCAL_DATE), localTime = LocalTime.parse("03:06:55.912321", ISO_LOCAL_TIME)),
            DatetimePropsNode(id = "a546b8f7-93e4-4d18-ae6f-97f73c4673ba".toUUIDOrNull()!!, offsetDatetime = OffsetDateTime.parse("2025-01-02T22:27:35.265104Z", ISO_OFFSET_DATE_TIME), offsetTime = OffsetTime.parse("00:45:34.475278Z", ISO_OFFSET_TIME), zonedDatetime = ZonedDateTime.parse("2025-01-02T22:27:35.265104Z", ISO_ZONED_DATE_TIME), localDatetime = LocalDateTime.parse("2025-01-02T22:27:35.265104", ISO_LOCAL_DATE_TIME), localDate = LocalDate.parse("2025-01-02", ISO_LOCAL_DATE), localTime = LocalTime.parse("00:45:34.475278", ISO_LOCAL_TIME)),
            DatetimePropsNode(id = "df753ed0-55ab-4b31-83f4-3cf51a912e57".toUUIDOrNull()!!, offsetDatetime = OffsetDateTime.parse("2025-01-03T16:48:10.033296Z", ISO_OFFSET_DATE_TIME), offsetTime = OffsetTime.parse("20:15:10.521469Z", ISO_OFFSET_TIME), zonedDatetime = ZonedDateTime.parse("2025-01-03T16:48:10.033296Z", ISO_ZONED_DATE_TIME), localDatetime = LocalDateTime.parse("2025-01-03T16:48:10.033296", ISO_LOCAL_DATE_TIME), localDate = LocalDate.parse("2025-01-03", ISO_LOCAL_DATE), localTime = LocalTime.parse("20:15:10.521469", ISO_LOCAL_TIME)),
            DatetimePropsNode(id = "7bce4b61-4872-4a1c-b5af-0682a52e85eb".toUUIDOrNull()!!, offsetDatetime = OffsetDateTime.parse("2025-01-03T18:14:37.283850Z", ISO_OFFSET_DATE_TIME), offsetTime = OffsetTime.parse("23:45:25.437358Z", ISO_OFFSET_TIME), zonedDatetime = ZonedDateTime.parse("2025-01-03T18:14:37.283850Z", ISO_ZONED_DATE_TIME), localDatetime = LocalDateTime.parse("2025-01-03T18:14:37.283850", ISO_LOCAL_DATE_TIME), localDate = LocalDate.parse("2025-01-03", ISO_LOCAL_DATE), localTime = LocalTime.parse("23:45:25.437358", ISO_LOCAL_TIME)),
            DatetimePropsNode(id = "48950243-606c-4de4-a7b4-95961af5a2c3".toUUIDOrNull()!!, offsetDatetime = OffsetDateTime.parse("2025-01-03T11:12:25.269422Z", ISO_OFFSET_DATE_TIME), offsetTime = OffsetTime.parse("07:34:14.657927Z", ISO_OFFSET_TIME), zonedDatetime = ZonedDateTime.parse("2025-01-03T11:12:25.269422Z", ISO_ZONED_DATE_TIME), localDatetime = LocalDateTime.parse("2025-01-03T11:12:25.269422", ISO_LOCAL_DATE_TIME), localDate = LocalDate.parse("2025-01-03", ISO_LOCAL_DATE), localTime = LocalTime.parse("07:34:14.657927", ISO_LOCAL_TIME)),
            DatetimePropsNode(id = "08813973-eaa8-4599-a863-bc6243202ba6".toUUIDOrNull()!!, offsetDatetime = OffsetDateTime.parse("2025-01-04T10:44:26.422912Z", ISO_OFFSET_DATE_TIME), offsetTime = OffsetTime.parse("01:09:21.000000Z", ISO_OFFSET_TIME), zonedDatetime = ZonedDateTime.parse("2025-01-04T10:44:26.422912Z", ISO_ZONED_DATE_TIME), localDatetime = LocalDateTime.parse("2025-01-04T10:44:26.422912", ISO_LOCAL_DATE_TIME), localDate = LocalDate.parse("2025-01-04", ISO_LOCAL_DATE), localTime = LocalTime.parse("01:09:21.000000", ISO_LOCAL_TIME)),
            DatetimePropsNode(id = "71e16707-edd3-4a39-a1e8-2e060e4663eb".toUUIDOrNull()!!, offsetDatetime = OffsetDateTime.parse("2025-01-04T03:35:25.512102Z", ISO_OFFSET_DATE_TIME), offsetTime = OffsetTime.parse("12:55:05.999999Z", ISO_OFFSET_TIME), zonedDatetime = ZonedDateTime.parse("2025-01-04T03:35:25.512102Z", ISO_ZONED_DATE_TIME), localDatetime = LocalDateTime.parse("2025-01-04T03:35:25.512102", ISO_LOCAL_DATE_TIME), localDate = LocalDate.parse("2025-01-04", ISO_LOCAL_DATE), localTime = LocalTime.parse("12:55:05.999999", ISO_LOCAL_TIME)),
            DatetimePropsNode(id = "4316e808-7295-4f5d-b78a-f362964cc316".toUUIDOrNull()!!, offsetDatetime = OffsetDateTime.parse("2025-01-04T11:09:13.838161Z", ISO_OFFSET_DATE_TIME), offsetTime = OffsetTime.parse("13:39:05.002569Z", ISO_OFFSET_TIME), zonedDatetime = ZonedDateTime.parse("2025-01-04T11:09:13.838161Z", ISO_ZONED_DATE_TIME), localDatetime = LocalDateTime.parse("2025-01-04T11:09:13.838161", ISO_LOCAL_DATE_TIME), localDate = LocalDate.parse("2025-01-04", ISO_LOCAL_DATE), localTime = LocalTime.parse("13:39:05.002569", ISO_LOCAL_TIME)),
            DatetimePropsNode(id = "432de6fe-48e1-487a-9afb-611962287861".toUUIDOrNull()!!, offsetDatetime = OffsetDateTime.parse("2025-01-05T00:27:41.446075Z", ISO_OFFSET_DATE_TIME), offsetTime = OffsetTime.parse("06:33:09.012952Z", ISO_OFFSET_TIME), zonedDatetime = ZonedDateTime.parse("2025-01-05T00:27:41.446075Z", ISO_ZONED_DATE_TIME), localDatetime = LocalDateTime.parse("2025-01-05T00:27:41.446075", ISO_LOCAL_DATE_TIME), localDate = LocalDate.parse("2025-01-05", ISO_LOCAL_DATE), localTime = LocalTime.parse("06:33:09.012952", ISO_LOCAL_TIME)),
            DatetimePropsNode(id = "530b587e-9a6a-4a48-bf0a-e4498b598c96".toUUIDOrNull()!!, offsetDatetime = OffsetDateTime.parse("2025-01-05T10:09:59.665863Z", ISO_OFFSET_DATE_TIME), offsetTime = OffsetTime.parse("05:47:01.339745Z", ISO_OFFSET_TIME), zonedDatetime = ZonedDateTime.parse("2025-01-05T10:09:59.665863Z", ISO_ZONED_DATE_TIME), localDatetime = LocalDateTime.parse("2025-01-05T10:09:59.665863", ISO_LOCAL_DATE_TIME), localDate = LocalDate.parse("2025-01-05", ISO_LOCAL_DATE), localTime = LocalTime.parse("05:47:01.339745", ISO_LOCAL_TIME)),
        )

        private val NESTEDS = listOf(
            DatetimeNestedPropsNode(id = "95ca3d37-d19a-4d20-94c7-26a1318f0a22".toUUIDOrNull()!!, propName = "qwerty", nestedNode = NODES[0]),
            DatetimeNestedPropsNode(id = "eac93bfc-1200-4bc9-afa9-02c6e37a365d".toUUIDOrNull()!!, propName = "qwerty", nestedNode = NODES[1]),
            DatetimeNestedPropsNode(id = "a6df2203-98de-4ef9-a609-d0c9366d7497".toUUIDOrNull()!!, propName = "qwerty", nestedNode = NODES[2]),
            DatetimeNestedPropsNode(id = "d9b3fee7-281f-4e8f-95d4-ce8112d02375".toUUIDOrNull()!!, propName = "qwerty", nestedNode = NODES[3]),
            DatetimeNestedPropsNode(id = "30aef4ca-7bd6-42f0-af6d-866b43ed4704".toUUIDOrNull()!!, propName = "qwerty", nestedNode = NODES[4]),
            DatetimeNestedPropsNode(id = "e6c1e7f6-5501-4206-8f64-ed385d16ca52".toUUIDOrNull()!!, propName = "qwerty", nestedNode = NODES[5]),
            DatetimeNestedPropsNode(id = "e8cb7d65-d4f3-431f-a960-4ec34a86e377".toUUIDOrNull()!!, propName = "qwerty", nestedNode = NODES[6]),
            DatetimeNestedPropsNode(id = "e343fa49-52b0-4703-9c31-1ae735e14a0b".toUUIDOrNull()!!, propName = "qwerty", nestedNode = NODES[7]),
            DatetimeNestedPropsNode(id = "28df9977-d7c8-47ab-b02e-282d777fe05d".toUUIDOrNull()!!, propName = "qwerty", nestedNode = NODES[8]),
            DatetimeNestedPropsNode(id = "3ac91dc3-a79f-4bbd-ad38-9f3ba14f3c09".toUUIDOrNull()!!, propName = "qwerty", nestedNode = NODES[9]),
            DatetimeNestedPropsNode(id = "b8b09330-2f4b-42b3-a780-66572d448586".toUUIDOrNull()!!, propName = "qwerty", nestedNode = NODES[10]),
            DatetimeNestedPropsNode(id = "30d0e6b8-e0cd-4442-b875-4e00d6fb1d9f".toUUIDOrNull()!!, propName = "qwerty", nestedNode = NODES[11]),
            DatetimeNestedPropsNode(id = "88d0d350-5f10-49d0-9535-95f1524e018d".toUUIDOrNull()!!, propName = "qwerty", nestedNode = NODES[12]),
            DatetimeNestedPropsNode(id = "e9bc712e-c2cf-42da-8878-8930e1735e9d".toUUIDOrNull()!!, propName = "qwerty", nestedNode = NODES[13]),
            DatetimeNestedPropsNode(id = "694975fe-392f-4c17-993b-5848c9a7305f".toUUIDOrNull()!!, propName = "qwerty", nestedNode = NODES[14]),
        )

        @JvmStatic
        fun datetimesGreater() = listOf(
            OffsetDateTime.parse("2025-01-02T19:48:13.710658Z", ISO_OFFSET_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::offsetDatetime, datetime, NODES.filter { it.offsetDatetime > datetime })
            },
            OffsetTime.parse("01:09:21Z", ISO_OFFSET_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::offsetTime, datetime, NODES.filter { it.offsetTime > datetime })
            },
            ZonedDateTime.parse("2025-01-02T19:48:13.710658Z", ISO_ZONED_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::zonedDatetime, datetime, NODES.filter { it.zonedDatetime > datetime })
            },
            LocalDateTime.parse("2025-01-02T19:48:13.710658", ISO_LOCAL_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::localDatetime, datetime, NODES.filter { it.localDatetime > datetime })
            },
            LocalDate.parse("2025-01-02", ISO_LOCAL_DATE).let { datetime ->
                Arguments.of(DatetimePropsNode::localDate, datetime, NODES.filter { it.localDate > datetime })
            },
            LocalTime.parse("01:09:21", ISO_LOCAL_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::localTime, datetime, NODES.filter { it.localTime > datetime })
            },
        )

        @JvmStatic
        fun datetimesGreaterEquals() = listOf(
            OffsetDateTime.parse("2025-01-02T19:48:13.710658Z", ISO_OFFSET_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::offsetDatetime, datetime, NODES.filter { it.offsetDatetime >= datetime })
            },
            OffsetTime.parse("01:09:21Z", ISO_OFFSET_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::offsetTime, datetime, NODES.filter { it.offsetTime >= datetime })
            },
            ZonedDateTime.parse("2025-01-02T19:48:13.710658Z", ISO_ZONED_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::zonedDatetime, datetime, NODES.filter { it.zonedDatetime >= datetime })
            },
            LocalDateTime.parse("2025-01-02T19:48:13.710658", ISO_LOCAL_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::localDatetime, datetime, NODES.filter { it.localDatetime >= datetime })
            },
            LocalDate.parse("2025-01-02", ISO_LOCAL_DATE).let { datetime ->
                Arguments.of(DatetimePropsNode::localDate, datetime, NODES.filter { it.localDate >= datetime })
            },
            LocalTime.parse("01:09:21", ISO_LOCAL_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::localTime, datetime, NODES.filter { it.localTime >= datetime })
            },
        )

        @JvmStatic
        fun datetimesLess() = listOf(
            OffsetDateTime.parse("2025-01-02T19:48:13.710658Z", ISO_OFFSET_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::offsetDatetime, datetime, NODES.filter { it.offsetDatetime < datetime })
            },
            OffsetTime.parse("01:09:21Z", ISO_OFFSET_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::offsetTime, datetime, NODES.filter { it.offsetTime < datetime })
            },
            ZonedDateTime.parse("2025-01-02T19:48:13.710658Z", ISO_ZONED_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::zonedDatetime, datetime, NODES.filter { it.zonedDatetime < datetime })
            },
            LocalDateTime.parse("2025-01-02T19:48:13.710658", ISO_LOCAL_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::localDatetime, datetime, NODES.filter { it.localDatetime < datetime })
            },
            LocalDate.parse("2025-01-02", ISO_LOCAL_DATE).let { datetime ->
                Arguments.of(DatetimePropsNode::localDate, datetime, NODES.filter { it.localDate < datetime })
            },
            LocalTime.parse("01:09:21", ISO_LOCAL_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::localTime, datetime, NODES.filter { it.localTime < datetime })
            },
        )

        @JvmStatic
        fun datetimesLessEquals() = listOf(
            OffsetDateTime.parse("2025-01-02T19:48:13.710658Z", ISO_OFFSET_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::offsetDatetime, datetime, NODES.filter { it.offsetDatetime <= datetime })
            },
            OffsetTime.parse("01:09:21Z", ISO_OFFSET_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::offsetTime, datetime, NODES.filter { it.offsetTime <= datetime })
            },
            ZonedDateTime.parse("2025-01-02T19:48:13.710658Z", ISO_ZONED_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::zonedDatetime, datetime, NODES.filter { it.zonedDatetime <= datetime })
            },
            LocalDateTime.parse("2025-01-02T19:48:13.710658", ISO_LOCAL_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::localDatetime, datetime, NODES.filter { it.localDatetime <= datetime })
            },
            LocalDate.parse("2025-01-02", ISO_LOCAL_DATE).let { datetime ->
                Arguments.of(DatetimePropsNode::localDate, datetime, NODES.filter { it.localDate <= datetime })
            },
            LocalTime.parse("01:09:21", ISO_LOCAL_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::localTime, datetime, NODES.filter { it.localTime <= datetime })
            },
        )

        @JvmStatic
        fun datetimesBetween() = listOf(
            (OffsetDateTime.parse("2025-01-02T08:01:54.115335Z", ISO_OFFSET_DATE_TIME)
                    to OffsetDateTime.parse("2025-01-04T03:35:25.512102Z", ISO_OFFSET_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::offsetDatetime, d1, d2, NODES.filter { it.offsetDatetime > d1 && it.offsetDatetime < d2 })
            },
            (OffsetTime.parse("03:06:55.912321Z", ISO_OFFSET_TIME) to
                    OffsetTime.parse("11:04:21.820664Z", ISO_OFFSET_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::offsetTime, d1, d2, NODES.filter { it.offsetTime > d1 && it.offsetTime < d2 })
            },
            (ZonedDateTime.parse("2025-01-02T08:01:54.115335Z", ISO_ZONED_DATE_TIME)
                    to ZonedDateTime.parse("2025-01-04T03:35:25.512102Z", ISO_ZONED_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::zonedDatetime, d1, d2, NODES.filter { it.zonedDatetime > d1 && it.zonedDatetime < d2 })
            },
            (LocalDateTime.parse("2025-01-02T08:01:54.115335", ISO_LOCAL_DATE_TIME)
                    to LocalDateTime.parse("2025-01-04T03:35:25.512102", ISO_LOCAL_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localDatetime, d1, d2, NODES.filter { it.localDatetime > d1 && it.localDatetime < d2 })
            },
            (LocalDate.parse("2025-01-02", ISO_LOCAL_DATE) to
                    LocalDate.parse("2025-01-04", ISO_LOCAL_DATE)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localDate, d1, d2, NODES.filter { it.localDate > d1 && it.localDate < d2 })
            },
            (LocalTime.parse("03:06:55.912321", ISO_LOCAL_TIME) to
                    LocalTime.parse("11:04:21.820664", ISO_LOCAL_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localTime, d1, d2, NODES.filter { it.localTime > d1 && it.localTime < d2 })
            },
        )

        @JvmStatic
        fun datetimesBetweenEquals() = listOf(
            (OffsetDateTime.parse("2025-01-02T08:01:54.115335Z", ISO_OFFSET_DATE_TIME)
                    to OffsetDateTime.parse("2025-01-04T03:35:25.512102Z", ISO_OFFSET_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::offsetDatetime, d1, d2, NODES.filter { it.offsetDatetime in d1..d2 })
            },
            (OffsetTime.parse("03:06:55.912321Z", ISO_OFFSET_TIME) to
                    OffsetTime.parse("11:04:21.820664Z", ISO_OFFSET_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::offsetTime, d1, d2, NODES.filter { it.offsetTime in d1..d2 })
            },
            (ZonedDateTime.parse("2025-01-02T08:01:54.115335Z", ISO_ZONED_DATE_TIME)
                    to ZonedDateTime.parse("2025-01-04T03:35:25.512102Z", ISO_ZONED_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::zonedDatetime, d1, d2, NODES.filter { it.zonedDatetime in d1..d2 })
            },
            (LocalDateTime.parse("2025-01-02T08:01:54.115335", ISO_LOCAL_DATE_TIME)
                    to LocalDateTime.parse("2025-01-04T03:35:25.512102", ISO_LOCAL_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localDatetime, d1, d2, NODES.filter { it.localDatetime in d1..d2 })
            },
            (LocalDate.parse("2025-01-02", ISO_LOCAL_DATE) to
                    LocalDate.parse("2025-01-04", ISO_LOCAL_DATE)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localDate, d1, d2, NODES.filter { it.localDate in d1..d2 })
            },
            (LocalTime.parse("03:06:55.912321", ISO_LOCAL_TIME) to
                    LocalTime.parse("11:04:21.820664", ISO_LOCAL_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localTime, d1, d2, NODES.filter { it.localTime in d1..d2 })
            },
        )

        @JvmStatic
        fun datetimesNotBetween() = listOf(
            (OffsetDateTime.parse("2025-01-02T08:01:54.115335Z", ISO_OFFSET_DATE_TIME)
                    to OffsetDateTime.parse("2025-01-04T03:35:25.512102Z", ISO_OFFSET_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::offsetDatetime, d1, d2, NODES.filter { it.offsetDatetime < d1 && it.offsetDatetime > d2 })
            },
            (OffsetTime.parse("03:06:55.912321Z", ISO_OFFSET_TIME) to
                    OffsetTime.parse("11:04:21.820664Z", ISO_OFFSET_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::offsetTime, d1, d2, NODES.filter { it.offsetTime < d1 && it.offsetTime > d2 })
            },
            (ZonedDateTime.parse("2025-01-02T08:01:54.115335Z", ISO_ZONED_DATE_TIME)
                    to ZonedDateTime.parse("2025-01-04T03:35:25.512102Z", ISO_ZONED_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::zonedDatetime, d1, d2, NODES.filter { it.zonedDatetime < d1 && it.zonedDatetime > d2 })
            },
            (LocalDateTime.parse("2025-01-02T08:01:54.115335", ISO_LOCAL_DATE_TIME)
                    to LocalDateTime.parse("2025-01-04T03:35:25.512102", ISO_LOCAL_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localDatetime, d1, d2, NODES.filter { it.localDatetime < d1 && it.localDatetime > d2 })
            },
            (LocalDate.parse("2025-01-02", ISO_LOCAL_DATE) to
                    LocalDate.parse("2025-01-04", ISO_LOCAL_DATE)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localDate, d1, d2, NODES.filter { it.localDate < d1 && it.localDate > d2 })
            },
            (LocalTime.parse("03:06:55.912321", ISO_LOCAL_TIME) to
                    LocalTime.parse("11:04:21.820664", ISO_LOCAL_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localTime, d1, d2, NODES.filter { it.localTime < d1 && it.localTime > d2 })
            },
        )

        @JvmStatic
        fun datetimesNotBetweenEquals() = listOf(
            (OffsetDateTime.parse("2025-01-02T08:01:54.115335Z", ISO_OFFSET_DATE_TIME)
                    to OffsetDateTime.parse("2025-01-04T03:35:25.512102Z", ISO_OFFSET_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::offsetDatetime, d1, d2, NODES.filter { it.offsetDatetime in d2..d1 })
            },
            (OffsetTime.parse("03:06:55.912321Z", ISO_OFFSET_TIME) to
                    OffsetTime.parse("11:04:21.820664Z", ISO_OFFSET_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::offsetTime, d1, d2, NODES.filter { it.offsetTime in d2..d1 })
            },
            (ZonedDateTime.parse("2025-01-02T08:01:54.115335Z", ISO_ZONED_DATE_TIME)
                    to ZonedDateTime.parse("2025-01-04T03:35:25.512102Z", ISO_ZONED_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::zonedDatetime, d1, d2, NODES.filter { it.zonedDatetime in d2..d1 })
            },
            (LocalDateTime.parse("2025-01-02T08:01:54.115335", ISO_LOCAL_DATE_TIME)
                    to LocalDateTime.parse("2025-01-04T03:35:25.512102", ISO_LOCAL_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localDatetime, d1, d2, NODES.filter { it.localDatetime in d2..d1 })
            },
            (LocalDate.parse("2025-01-02", ISO_LOCAL_DATE) to
                    LocalDate.parse("2025-01-04", ISO_LOCAL_DATE)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localDate, d1, d2, NODES.filter { it.localDate in d2..d1 })
            },
            (LocalTime.parse("03:06:55.912321", ISO_LOCAL_TIME) to
                    LocalTime.parse("11:04:21.820664", ISO_LOCAL_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localTime, d1, d2, NODES.filter { it.localTime in d2..d1 })
            },
        )

        @JvmStatic
        fun datetimesNestedGreater() = listOf(
            OffsetDateTime.parse("2025-01-02T19:48:13.710658Z", ISO_OFFSET_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::offsetDatetime, datetime, NESTEDS.filter { it.nestedNode.offsetDatetime > datetime })
            },
            OffsetTime.parse("01:09:21Z", ISO_OFFSET_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::offsetTime, datetime, NESTEDS.filter { it.nestedNode.offsetTime > datetime })
            },
            ZonedDateTime.parse("2025-01-02T19:48:13.710658Z", ISO_ZONED_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::zonedDatetime, datetime, NESTEDS.filter { it.nestedNode.zonedDatetime > datetime })
            },
            LocalDateTime.parse("2025-01-02T19:48:13.710658", ISO_LOCAL_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::localDatetime, datetime, NESTEDS.filter { it.nestedNode.localDatetime > datetime })
            },
            LocalDate.parse("2025-01-02", ISO_LOCAL_DATE).let { datetime ->
                Arguments.of(DatetimePropsNode::localDate, datetime, NESTEDS.filter { it.nestedNode.localDate > datetime })
            },
            LocalTime.parse("01:09:21", ISO_LOCAL_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::localTime, datetime, NESTEDS.filter { it.nestedNode.localTime > datetime })
            },
        )

        @JvmStatic
        fun datetimesNestedGreaterEquals() = listOf(
            OffsetDateTime.parse("2025-01-02T19:48:13.710658Z", ISO_OFFSET_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::offsetDatetime, datetime, NESTEDS.filter { it.nestedNode.offsetDatetime >= datetime })
            },
            OffsetTime.parse("01:09:21Z", ISO_OFFSET_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::offsetTime, datetime, NESTEDS.filter { it.nestedNode.offsetTime >= datetime })
            },
            ZonedDateTime.parse("2025-01-02T19:48:13.710658Z", ISO_ZONED_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::zonedDatetime, datetime, NESTEDS.filter { it.nestedNode.zonedDatetime >= datetime })
            },
            LocalDateTime.parse("2025-01-02T19:48:13.710658", ISO_LOCAL_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::localDatetime, datetime, NESTEDS.filter { it.nestedNode.localDatetime >= datetime })
            },
            LocalDate.parse("2025-01-02", ISO_LOCAL_DATE).let { datetime ->
                Arguments.of(DatetimePropsNode::localDate, datetime, NESTEDS.filter { it.nestedNode.localDate >= datetime })
            },
            LocalTime.parse("01:09:21", ISO_LOCAL_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::localTime, datetime, NESTEDS.filter { it.nestedNode.localTime >= datetime })
            },
        )

        @JvmStatic
        fun datetimesNestedLess() = listOf(
            OffsetDateTime.parse("2025-01-02T19:48:13.710658Z", ISO_OFFSET_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::offsetDatetime, datetime, NESTEDS.filter { it.nestedNode.offsetDatetime < datetime })
            },
            OffsetTime.parse("01:09:21Z", ISO_OFFSET_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::offsetTime, datetime, NESTEDS.filter { it.nestedNode.offsetTime < datetime })
            },
            ZonedDateTime.parse("2025-01-02T19:48:13.710658Z", ISO_ZONED_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::zonedDatetime, datetime, NESTEDS.filter { it.nestedNode.zonedDatetime < datetime })
            },
            LocalDateTime.parse("2025-01-02T19:48:13.710658", ISO_LOCAL_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::localDatetime, datetime, NESTEDS.filter { it.nestedNode.localDatetime < datetime })
            },
            LocalDate.parse("2025-01-02", ISO_LOCAL_DATE).let { datetime ->
                Arguments.of(DatetimePropsNode::localDate, datetime, NESTEDS.filter { it.nestedNode.localDate < datetime })
            },
            LocalTime.parse("01:09:21", ISO_LOCAL_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::localTime, datetime, NESTEDS.filter { it.nestedNode.localTime < datetime })
            },
        )

        @JvmStatic
        fun datetimesNestedLessEquals() = listOf(
            OffsetDateTime.parse("2025-01-02T19:48:13.710658Z", ISO_OFFSET_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::offsetDatetime, datetime, NESTEDS.filter { it.nestedNode.offsetDatetime <= datetime })
            },
            OffsetTime.parse("01:09:21Z", ISO_OFFSET_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::offsetTime, datetime, NESTEDS.filter { it.nestedNode.offsetTime <= datetime })
            },
            ZonedDateTime.parse("2025-01-02T19:48:13.710658Z", ISO_ZONED_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::zonedDatetime, datetime, NESTEDS.filter { it.nestedNode.zonedDatetime <= datetime })
            },
            LocalDateTime.parse("2025-01-02T19:48:13.710658", ISO_LOCAL_DATE_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::localDatetime, datetime, NESTEDS.filter { it.nestedNode.localDatetime <= datetime })
            },
            LocalDate.parse("2025-01-02", ISO_LOCAL_DATE).let { datetime ->
                Arguments.of(DatetimePropsNode::localDate, datetime, NESTEDS.filter { it.nestedNode.localDate <= datetime })
            },
            LocalTime.parse("01:09:21", ISO_LOCAL_TIME).let { datetime ->
                Arguments.of(DatetimePropsNode::localTime, datetime, NESTEDS.filter { it.nestedNode.localTime <= datetime })
            },
        )

        @JvmStatic
        fun datetimesNestedBetween() = listOf(
            (OffsetDateTime.parse("2025-01-02T08:01:54.115335Z", ISO_OFFSET_DATE_TIME)
                    to OffsetDateTime.parse("2025-01-04T03:35:25.512102Z", ISO_OFFSET_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::offsetDatetime, d1, d2, NESTEDS.filter { it.nestedNode.offsetDatetime > d1 && it.nestedNode.offsetDatetime < d2 })
            },
            (OffsetTime.parse("03:06:55.912321Z", ISO_OFFSET_TIME) to
                    OffsetTime.parse("11:04:21.820664Z", ISO_OFFSET_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::offsetTime, d1, d2, NESTEDS.filter { it.nestedNode.offsetTime > d1 && it.nestedNode.offsetTime < d2 })
            },
            (ZonedDateTime.parse("2025-01-02T08:01:54.115335Z", ISO_ZONED_DATE_TIME)
                    to ZonedDateTime.parse("2025-01-04T03:35:25.512102Z", ISO_ZONED_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::zonedDatetime, d1, d2, NESTEDS.filter { it.nestedNode.zonedDatetime > d1 && it.nestedNode.zonedDatetime < d2 })
            },
            (LocalDateTime.parse("2025-01-02T08:01:54.115335", ISO_LOCAL_DATE_TIME)
                    to LocalDateTime.parse("2025-01-04T03:35:25.512102", ISO_LOCAL_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localDatetime, d1, d2, NESTEDS.filter { it.nestedNode.localDatetime > d1 && it.nestedNode.localDatetime < d2 })
            },
            (LocalDate.parse("2025-01-02", ISO_LOCAL_DATE) to
                    LocalDate.parse("2025-01-04", ISO_LOCAL_DATE)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localDate, d1, d2, NESTEDS.filter { it.nestedNode.localDate > d1 && it.nestedNode.localDate < d2 })
            },
            (LocalTime.parse("03:06:55.912321", ISO_LOCAL_TIME) to
                    LocalTime.parse("11:04:21.820664", ISO_LOCAL_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localTime, d1, d2, NESTEDS.filter { it.nestedNode.localTime > d1 && it.nestedNode.localTime < d2 })
            },
        )

        @JvmStatic
        fun datetimesNestedBetweenEquals() = listOf(
            (OffsetDateTime.parse("2025-01-02T08:01:54.115335Z", ISO_OFFSET_DATE_TIME)
                    to OffsetDateTime.parse("2025-01-04T03:35:25.512102Z", ISO_OFFSET_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::offsetDatetime, d1, d2, NESTEDS.filter { it.nestedNode.offsetDatetime in d1..d2 })
            },
            (OffsetTime.parse("03:06:55.912321Z", ISO_OFFSET_TIME) to
                    OffsetTime.parse("11:04:21.820664Z", ISO_OFFSET_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::offsetTime, d1, d2, NESTEDS.filter { it.nestedNode.offsetTime in d1..d2 })
            },
            (ZonedDateTime.parse("2025-01-02T08:01:54.115335Z", ISO_ZONED_DATE_TIME)
                    to ZonedDateTime.parse("2025-01-04T03:35:25.512102Z", ISO_ZONED_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::zonedDatetime, d1, d2, NESTEDS.filter { it.nestedNode.zonedDatetime in d1..d2 })
            },
            (LocalDateTime.parse("2025-01-02T08:01:54.115335", ISO_LOCAL_DATE_TIME)
                    to LocalDateTime.parse("2025-01-04T03:35:25.512102", ISO_LOCAL_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localDatetime, d1, d2, NESTEDS.filter { it.nestedNode.localDatetime in d1..d2 })
            },
            (LocalDate.parse("2025-01-02", ISO_LOCAL_DATE) to
                    LocalDate.parse("2025-01-04", ISO_LOCAL_DATE)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localDate, d1, d2, NESTEDS.filter { it.nestedNode.localDate in d1..d2 })
            },
            (LocalTime.parse("03:06:55.912321", ISO_LOCAL_TIME) to
                    LocalTime.parse("11:04:21.820664", ISO_LOCAL_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localTime, d1, d2, NESTEDS.filter { it.nestedNode.localTime in d1..d2 })
            },
        )

        @JvmStatic
        fun datetimesNestedNotBetween() = listOf(
            (OffsetDateTime.parse("2025-01-02T08:01:54.115335Z", ISO_OFFSET_DATE_TIME)
                    to OffsetDateTime.parse("2025-01-04T03:35:25.512102Z", ISO_OFFSET_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::offsetDatetime, d1, d2, NESTEDS.filter { it.nestedNode.offsetDatetime < d1 && it.nestedNode.offsetDatetime > d2 })
            },
            (OffsetTime.parse("03:06:55.912321Z", ISO_OFFSET_TIME) to
                    OffsetTime.parse("11:04:21.820664Z", ISO_OFFSET_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::offsetTime, d1, d2, NESTEDS.filter { it.nestedNode.offsetTime < d1 && it.nestedNode.offsetTime > d2 })
            },
            (ZonedDateTime.parse("2025-01-02T08:01:54.115335Z", ISO_ZONED_DATE_TIME)
                    to ZonedDateTime.parse("2025-01-04T03:35:25.512102Z", ISO_ZONED_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::zonedDatetime, d1, d2, NESTEDS.filter { it.nestedNode.zonedDatetime < d1 && it.nestedNode.zonedDatetime > d2 })
            },
            (LocalDateTime.parse("2025-01-02T08:01:54.115335", ISO_LOCAL_DATE_TIME)
                    to LocalDateTime.parse("2025-01-04T03:35:25.512102", ISO_LOCAL_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localDatetime, d1, d2, NESTEDS.filter { it.nestedNode.localDatetime < d1 && it.nestedNode.localDatetime > d2 })
            },
            (LocalDate.parse("2025-01-02", ISO_LOCAL_DATE) to
                    LocalDate.parse("2025-01-04", ISO_LOCAL_DATE)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localDate, d1, d2, NESTEDS.filter { it.nestedNode.localDate < d1 && it.nestedNode.localDate > d2 })
            },
            (LocalTime.parse("03:06:55.912321", ISO_LOCAL_TIME) to
                    LocalTime.parse("11:04:21.820664", ISO_LOCAL_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localTime, d1, d2, NESTEDS.filter { it.nestedNode.localTime < d1 && it.nestedNode.localTime > d2 })
            },
        )

        @JvmStatic
        fun datetimesNestedNotBetweenEquals() = listOf(
            (OffsetDateTime.parse("2025-01-02T08:01:54.115335Z", ISO_OFFSET_DATE_TIME)
                    to OffsetDateTime.parse("2025-01-04T03:35:25.512102Z", ISO_OFFSET_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::offsetDatetime, d1, d2, NESTEDS.filter { it.nestedNode.offsetDatetime in d2..d1 })
            },
            (OffsetTime.parse("03:06:55.912321Z", ISO_OFFSET_TIME) to
                    OffsetTime.parse("11:04:21.820664Z", ISO_OFFSET_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::offsetTime, d1, d2, NESTEDS.filter { it.nestedNode.offsetTime in d2..d1 })
            },
            (ZonedDateTime.parse("2025-01-02T08:01:54.115335Z", ISO_ZONED_DATE_TIME)
                    to ZonedDateTime.parse("2025-01-04T03:35:25.512102Z", ISO_ZONED_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::zonedDatetime, d1, d2, NESTEDS.filter { it.nestedNode.zonedDatetime in d2..d1 })
            },
            (LocalDateTime.parse("2025-01-02T08:01:54.115335", ISO_LOCAL_DATE_TIME)
                    to LocalDateTime.parse("2025-01-04T03:35:25.512102", ISO_LOCAL_DATE_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localDatetime, d1, d2, NESTEDS.filter { it.nestedNode.localDatetime in d2..d1 })
            },
            (LocalDate.parse("2025-01-02", ISO_LOCAL_DATE) to
                    LocalDate.parse("2025-01-04", ISO_LOCAL_DATE)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localDate, d1, d2, NESTEDS.filter { it.nestedNode.localDate in d2..d1 })
            },
            (LocalTime.parse("03:06:55.912321", ISO_LOCAL_TIME) to
                    LocalTime.parse("11:04:21.820664", ISO_LOCAL_TIME)).let { (d1, d2) ->
                Arguments.of(DatetimePropsNode::localTime, d1, d2, NESTEDS.filter { it.nestedNode.localTime in d2..d1 })
            },
        )

    }

    @ParameterizedTest
    @MethodSource("datetimesGreater")
    fun `Date time greater than`(
        property: KProperty<Any>,
        value: Temporal,
        nodes: List<DatetimePropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/datetime-range.cypher")
        val result = repository.datetimeGreaterThan(property, value)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("datetimesNestedGreater")
    fun `Date time nested greater than`(
        property: KProperty<Any>,
        value: Temporal,
        nodes: List<DatetimeNestedPropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/datetime-range.cypher")
        val result = repository.datetimeNestedGreaterThan(property, value)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("datetimesGreaterEquals")
    fun `Date time greater than equals`(
        property: KProperty<Any>,
        value: Temporal,
        nodes: List<DatetimePropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/datetime-range.cypher")
        val result = repository.datetimeGreaterThanEquals(property, value)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("datetimesNestedGreaterEquals")
    fun `Date time nested greater than equals`(
        property: KProperty<Any>,
        value: Temporal,
        nodes: List<DatetimeNestedPropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/datetime-range.cypher")
        val result = repository.datetimeNestedGreaterThanEquals(property, value)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("datetimesLess")
    fun `Date time less than`(
        property: KProperty<Any>,
        value: Temporal,
        nodes: List<DatetimePropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/datetime-range.cypher")
        val result = repository.datetimeLessThan(property, value)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("datetimesNestedLess")
    fun `Date time nested less than`(
        property: KProperty<Any>,
        value: Temporal,
        nodes: List<DatetimeNestedPropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/datetime-range.cypher")
        val result = repository.datetimeNestedLessThan(property, value)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("datetimesLessEquals")
    fun `Date time less than equals`(
        property: KProperty<Any>,
        value: Temporal,
        nodes: List<DatetimePropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/datetime-range.cypher")
        val result = repository.datetimeLessThanEquals(property, value)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("datetimesNestedLessEquals")
    fun `Date time nested less than equals`(
        property: KProperty<Any>,
        value: Temporal,
        nodes: List<DatetimeNestedPropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/datetime-range.cypher")
        val result = repository.datetimeNestedLessThanEquals(property, value)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("datetimesBetween")
    fun `Date time between`(
        property: KProperty<Any>,
        min: Temporal,
        max: Temporal,
        nodes: List<DatetimePropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/datetime-range.cypher")
        val result = repository.datetimeBetween(property, min, max)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("datetimesNestedBetween")
    fun `Date time nested between`(
        property: KProperty<Any>,
        min: Temporal,
        max: Temporal,
        nodes: List<DatetimeNestedPropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/datetime-range.cypher")
        val result = repository.datetimeNestedBetween(property, min, max)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("datetimesBetweenEquals")
    fun `Date time between equals`(
        property: KProperty<Any>,
        min: Temporal,
        max: Temporal,
        nodes: List<DatetimePropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/datetime-range.cypher")
        val result = repository.datetimeBetweenEquals(property, min, max)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("datetimesNestedBetweenEquals")
    fun `Date time nested between equals`(
        property: KProperty<Any>,
        min: Temporal,
        max: Temporal,
        nodes: List<DatetimeNestedPropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/datetime-range.cypher")
        val result = repository.datetimeNestedBetweenEquals(property, min, max)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("datetimesNotBetween")
    fun `Date time not between`(
        property: KProperty<Any>,
        min: Temporal,
        max: Temporal,
        nodes: List<DatetimePropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/datetime-range.cypher")
        val result = repository.datetimeNotBetween(property, min, max)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("datetimesNestedNotBetween")
    fun `Date time nested not between`(
        property: KProperty<Any>,
        min: Temporal,
        max: Temporal,
        nodes: List<DatetimeNestedPropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/datetime-range.cypher")
        val result = repository.datetimeNestedNotBetween(property, min, max)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("datetimesNotBetweenEquals")
    fun `Date time not between equals`(
        property: KProperty<Any>,
        min: Temporal,
        max: Temporal,
        nodes: List<DatetimePropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/datetime-range.cypher")
        val result = repository.datetimeNotBetweenEquals(property, min, max)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("datetimesNestedNotBetweenEquals")
    fun `Date time nested not between equals`(
        property: KProperty<Any>,
        min: Temporal,
        max: Temporal,
        nodes: List<DatetimeNestedPropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/datetime-range.cypher")
        val result = repository.datetimeNestedNotBetweenEquals(property, min, max)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

}
