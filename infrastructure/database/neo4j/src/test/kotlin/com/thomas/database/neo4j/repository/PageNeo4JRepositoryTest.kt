package com.thomas.database.neo4j.repository

import com.thomas.core.extension.toUUIDOrNull
import com.thomas.core.extension.unaccentedLower
import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection.ASC
import com.thomas.core.model.pagination.PageSortDirection.DESC
import com.thomas.database.neo4j.filter.betweenEquals
import com.thomas.database.neo4j.filter.equalsUnaccentedLower
import com.thomas.database.neo4j.filter.greaterThanEquals
import com.thomas.database.neo4j.filter.likeUnaccentedLower
import com.thomas.database.neo4j.filter.or
import com.thomas.database.neo4j.node.PagePropsNode
import com.thomas.database.neo4j.util.toZonedDateTimeEnd
import com.thomas.database.neo4j.util.toZonedDateTimeStart
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME
import java.util.UUID
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.neo4j.ogm.cypher.Filter


class PageNeo4JRepositoryTest : Neo4JRepositoryTest() {

    companion object {

        private val NODES = listOf(
            PagePropsNode(id = "1b1c3e05-e734-4f9c-a6eb-09c75ee2e014".toUUIDOrNull()!!, propName = "João Silva", propValue = 45, propDatetime = ZonedDateTime.parse("2025-04-06T09:00:19.272067Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "21ea5281-a21c-4f7f-a87a-2f101d22b69d".toUUIDOrNull()!!, propName = "Maria Souza", propValue = 25, propDatetime = ZonedDateTime.parse("2025-10-24T22:29:55.571038Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "db27bb86-7abc-486b-9923-6dc60ce5db03".toUUIDOrNull()!!, propName = "Pedro Almeida", propValue = 39, propDatetime = ZonedDateTime.parse("2025-04-24T03:14:29.681897Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "ef1558f9-fe33-484f-88cc-254e23674f7d".toUUIDOrNull()!!, propName = "Ana Oliveira", propValue = 36, propDatetime = ZonedDateTime.parse("2025-05-01T03:49:02.437687Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "3cdbc11c-72ca-4b2c-8180-6b6051360000".toUUIDOrNull()!!, propName = "Lucas Ferreira", propValue = 34, propDatetime = ZonedDateTime.parse("2025-12-26T13:52:21.445129Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "d8b635ca-9167-490a-a9e2-427ba17dcfe2".toUUIDOrNull()!!, propName = "Mariana Costa", propValue = 33, propDatetime = ZonedDateTime.parse("2025-06-05T04:26:02.920447Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "a2322bc2-17ff-4676-ad68-4284ef54bfe8".toUUIDOrNull()!!, propName = "Felipe Santos", propValue = 13, propDatetime = ZonedDateTime.parse("2025-04-16T16:17:56.734987Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "01fa659b-94fa-4145-8ccf-950914d38ea1".toUUIDOrNull()!!, propName = "Juliana Rocha", propValue = 31, propDatetime = ZonedDateTime.parse("2025-01-05T13:15:34.825530Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "a0586eed-764d-4132-9969-259746d54540".toUUIDOrNull()!!, propName = "Rafael Mendes", propValue = 98, propDatetime = ZonedDateTime.parse("2025-11-22T01:37:26.066523Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "87c4e6ba-066c-477e-8eb9-b6a261e08627".toUUIDOrNull()!!, propName = "Camila Lima", propValue = 90, propDatetime = ZonedDateTime.parse("2025-10-30T15:57:23.231331Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "dbb2b43a-0c74-4c05-aff1-ba209b962800".toUUIDOrNull()!!, propName = "Bruno Martins", propValue = 28, propDatetime = ZonedDateTime.parse("2025-03-26T11:30:14.731590Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "1c43c000-bc2b-4419-ba12-bbda5f8de2aa".toUUIDOrNull()!!, propName = "Vanessa Cardoso", propValue = 72, propDatetime = ZonedDateTime.parse("2025-05-09T15:57:48.584007Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "d2b06723-677b-4221-8258-ba07d01d412e".toUUIDOrNull()!!, propName = "Gabriel Teixeira", propValue = 54, propDatetime = ZonedDateTime.parse("2025-06-26T09:15:09.311436Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "f1492ef2-c0f1-446d-a0ff-e31307f31661".toUUIDOrNull()!!, propName = "Bianca Nogueira", propValue = 48, propDatetime = ZonedDateTime.parse("2025-05-07T23:58:39.222734Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "b7f169d5-ba62-4150-ac36-c8253fb66a1c".toUUIDOrNull()!!, propName = "Eduardo Carvalho", propValue = 52, propDatetime = ZonedDateTime.parse("2025-10-10T17:05:37.427399Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "84ff838b-50fb-408d-917f-ece995416fa3".toUUIDOrNull()!!, propName = "Tatiane Ribeiro", propValue = 75, propDatetime = ZonedDateTime.parse("2025-11-26T16:31:32.421107Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "762648c3-829f-40d1-92b9-4e9157dbb19a".toUUIDOrNull()!!, propName = "André Barbosa", propValue = 31, propDatetime = ZonedDateTime.parse("2025-12-07T13:11:11.103898Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "2c51fcbc-a795-41b4-9404-86b23ad4d80f".toUUIDOrNull()!!, propName = "Aline Duarte", propValue = 56, propDatetime = ZonedDateTime.parse("2025-04-17T22:07:04.523514Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "d3e47652-cc85-4066-ab1b-4d20b3cb0c5a".toUUIDOrNull()!!, propName = "Rodrigo Moreira", propValue = 40, propDatetime = ZonedDateTime.parse("2025-09-27T06:10:20.491515Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "5f045844-d97d-40da-b0ba-0367daeb6380".toUUIDOrNull()!!, propName = "Larissa Pires", propValue = 75, propDatetime = ZonedDateTime.parse("2025-01-08T15:04:33.055173Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "44c16f71-3861-45a7-bf2d-2b9419ac4691".toUUIDOrNull()!!, propName = "Daniel Monteiro", propValue = 76, propDatetime = ZonedDateTime.parse("2025-01-01T08:04:44.137564Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "57128477-beaa-4ec4-a76a-d527d2d1f33f".toUUIDOrNull()!!, propName = "Priscila Azevedo", propValue = 61, propDatetime = ZonedDateTime.parse("2025-04-03T08:23:13.610366Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "5e897e4a-075d-428b-8198-f943901c42a0".toUUIDOrNull()!!, propName = "Thiago Gomes", propValue = 11, propDatetime = ZonedDateTime.parse("2025-02-10T21:45:05.396686Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "2b49f6ae-b442-4e1e-b7a6-c65656e3e4a5".toUUIDOrNull()!!, propName = "Renata Figueiredo", propValue = 11, propDatetime = ZonedDateTime.parse("2025-10-25T13:39:07.178843Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "be677f48-9609-43a7-b3cf-87c5c196f10f".toUUIDOrNull()!!, propName = "Carlos Batista", propValue = 18, propDatetime = ZonedDateTime.parse("2025-04-06T01:05:57.141941Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "0a3be14e-4489-4e24-a365-2b2ff43051f4".toUUIDOrNull()!!, propName = "Vanessa Melo", propValue = 99, propDatetime = ZonedDateTime.parse("2025-11-23T19:51:24.260731Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "d284b293-9802-4ddf-a282-3fd2aa3b8a40".toUUIDOrNull()!!, propName = "Diego Maciel", propValue = 31, propDatetime = ZonedDateTime.parse("2025-11-04T19:30:13.338861Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "0d2e6063-6671-498a-93d5-c19d3e4ca423".toUUIDOrNull()!!, propName = "Rafaela Rezende", propValue = 28, propDatetime = ZonedDateTime.parse("2025-05-10T21:19:30.642523Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "d5f9b6f7-b17a-4ec2-a799-7d7acdaf2268".toUUIDOrNull()!!, propName = "Leonardo Antunes", propValue = 23, propDatetime = ZonedDateTime.parse("2025-10-14T14:03:07.256714Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "69665095-eb6b-4f6c-9132-35b3174c2bfb".toUUIDOrNull()!!, propName = "Cristiane Freitas", propValue = 32, propDatetime = ZonedDateTime.parse("2025-01-20T01:52:39.030690Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "12e48fea-2b5a-4316-abf3-d87c41de1256".toUUIDOrNull()!!, propName = "Hugo Correia", propValue = 73, propDatetime = ZonedDateTime.parse("2025-12-18T22:42:09.305577Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "6cef65a6-6d94-46b6-bf8d-0cb20101e8d6".toUUIDOrNull()!!, propName = "Natália Moura", propValue = 67, propDatetime = ZonedDateTime.parse("2025-01-14T19:58:38.168333Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "e18eb408-4631-48ef-abb6-f1d7819619bc".toUUIDOrNull()!!, propName = "Alexandre Fonseca", propValue = 13, propDatetime = ZonedDateTime.parse("2025-07-19T07:19:55.873939Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "44f53286-0466-4325-befe-af6b76488535".toUUIDOrNull()!!, propName = "Fernanda Brito", propValue = 99, propDatetime = ZonedDateTime.parse("2025-09-02T06:57:04.998633Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "5809113d-b061-4ff5-b872-66ce786a5141".toUUIDOrNull()!!, propName = "Marcelo Vasconcelos", propValue = 17, propDatetime = ZonedDateTime.parse("2025-05-08T03:50:27.103026Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "7e1360e6-759a-42b8-9c78-7dac2042fb8d".toUUIDOrNull()!!, propName = "Tatiana Campos", propValue = 88, propDatetime = ZonedDateTime.parse("2025-12-25T20:56:01.393326Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "b8151fb4-626a-40ea-8038-fcb83142f532".toUUIDOrNull()!!, propName = "Igor Rezende", propValue = 13, propDatetime = ZonedDateTime.parse("2025-08-21T18:13:47.013201Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "870aeb91-e7ee-4549-a3ff-686dccd6d1bf".toUUIDOrNull()!!, propName = "Patrícia Siqueira", propValue = 39, propDatetime = ZonedDateTime.parse("2025-04-30T03:04:12.841335Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "1a1acc5f-c98c-4685-af1b-c1885d884fce".toUUIDOrNull()!!, propName = "Fernando Tavares", propValue = 60, propDatetime = ZonedDateTime.parse("2025-06-22T07:07:36.220582Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "74382194-c075-444a-a33c-7e1b815be02c".toUUIDOrNull()!!, propName = "Kelly Bezerra", propValue = 40, propDatetime = ZonedDateTime.parse("2025-08-16T05:21:56.958405Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "d4047101-c74d-426f-9fb5-adef5a32242c".toUUIDOrNull()!!, propName = "Ricardo Sales", propValue = 41, propDatetime = ZonedDateTime.parse("2025-11-15T12:36:11.953799Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "db291105-36f5-4b44-abdd-a47edfe5b789".toUUIDOrNull()!!, propName = "Jéssica Amaral", propValue = 26, propDatetime = ZonedDateTime.parse("2025-10-11T16:37:05.364614Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "44d1f3e7-460b-4454-bdbc-68c363bc1372".toUUIDOrNull()!!, propName = "Emerson Barreto", propValue = 28, propDatetime = ZonedDateTime.parse("2025-11-11T12:13:27.493984Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "108a0e39-4614-4d8e-8f16-fd44513d1f0d".toUUIDOrNull()!!, propName = "Luana Viana", propValue = 95, propDatetime = ZonedDateTime.parse("2025-11-20T20:58:23.191394Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "86fc7ac6-8f68-4733-babd-90fa6eb75bef".toUUIDOrNull()!!, propName = "Gustavo Silveira", propValue = 56, propDatetime = ZonedDateTime.parse("2025-03-15T11:48:08.130572Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "a6a1054a-6f92-4235-9e20-5f91c129a9a0".toUUIDOrNull()!!, propName = "Danielle Couto", propValue = 88, propDatetime = ZonedDateTime.parse("2025-02-12T17:16:18.958328Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "a49aeb83-acfb-4822-b034-bbfa4ccd90f5".toUUIDOrNull()!!, propName = "Roberto Queiroz", propValue = 40, propDatetime = ZonedDateTime.parse("2025-04-14T14:16:45.030419Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "112b4ef0-c2c7-483e-a4d1-6b49e8af6fe3".toUUIDOrNull()!!, propName = "Sabrina Nascimento", propValue = 94, propDatetime = ZonedDateTime.parse("2025-05-24T23:10:04.657969Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "883041e7-f4d2-4a8f-aed3-eff7fe4b8272".toUUIDOrNull()!!, propName = "Victor Assis", propValue = 30, propDatetime = ZonedDateTime.parse("2025-08-17T03:18:34.165639Z", ISO_ZONED_DATE_TIME)),
            PagePropsNode(id = "f4cf2fa2-8466-4bc6-ae64-7ce720362091".toUUIDOrNull()!!, propName = "Simone Cunha", propValue = 92, propDatetime = ZonedDateTime.parse("2025-02-25T23:30:47.909304Z", ISO_ZONED_DATE_TIME)),
        )

        private val propNameValue: PagePropsNode.() -> Any = { this.propName }
        private val propValueValue: PagePropsNode.() -> Any = { this.propValue }
        private val propDatetimeValue: PagePropsNode.() -> Any = { this.propDatetime }

        @JvmStatic
        fun sortingValuesSingle() = listOf(
            PageSort("prop_name", ASC).let { s -> Arguments.of(s, NODES.sortedBy { it.propName }, propNameValue) },
            PageSort("prop_value", ASC).let { s -> Arguments.of(s, NODES.sortedBy { it.propValue }, propValueValue) },
            PageSort("prop_datetime", ASC).let { s -> Arguments.of(s, NODES.sortedBy { it.propDatetime }, propDatetimeValue) },
            PageSort("prop_name", DESC).let { s -> Arguments.of(s, NODES.sortedByDescending { it.propName }, propNameValue) },
            PageSort("prop_value", DESC).let { s -> Arguments.of(s, NODES.sortedByDescending { it.propValue }, propValueValue) },
            PageSort("prop_datetime", DESC).let { s -> Arguments.of(s, NODES.sortedByDescending { it.propDatetime }, propDatetimeValue) },
        )

        @JvmStatic
        fun sortingValuesList() = listOf(
            listOf(
                PageSort("prop_name", ASC),
                PageSort("prop_value", DESC),
            ).let { s -> Arguments.of(s, NODES.sortedWith(compareBy<PagePropsNode> { it.propName }.thenByDescending { it.propValue })) },
            listOf(
                PageSort("prop_value", DESC),
                PageSort("prop_name", ASC),
            ).let { s -> Arguments.of(s, NODES.sortedWith(compareByDescending<PagePropsNode> { it.propValue }.thenBy { it.propName })) },
            listOf(
                PageSort("prop_datetime", DESC),
                PageSort("prop_name", DESC),
            ).let { s -> Arguments.of(s, NODES.sortedWith(compareByDescending<PagePropsNode> { it.propDatetime }.thenByDescending { it.propName })) },
            listOf(
                PageSort("prop_value", ASC),
                PageSort("prop_name", DESC),
                PageSort("prop_datetime", DESC),
            ).let { s -> Arguments.of(s, NODES.sortedWith(compareBy<PagePropsNode> { it.propValue }.thenByDescending { it.propName }.thenByDescending { it.propDatetime })) },
        )

        @JvmStatic
        fun findByIds() = listOf(
            "a2322bc2-17ff-4676-ad68-4284ef54bfe8".toUUIDOrNull()!!.let { id -> Arguments.of(id, NODES.firstOrNull { it.id == id }) },
            "2c51fcbc-a795-41b4-9404-86b23ad4d80f".toUUIDOrNull()!!.let { id -> Arguments.of(id, NODES.firstOrNull { it.id == id }) },
            "2b49f6ae-b442-4e1e-b7a6-c65656e3e4a5".toUUIDOrNull()!!.let { id -> Arguments.of(id, NODES.firstOrNull { it.id == id }) },
            "74295629-c797-4c34-b3fd-0c19015d9183".toUUIDOrNull()!!.let { id -> Arguments.of(id, NODES.firstOrNull { it.id == id }) },
            "7b0ef13a-e6a8-449e-8708-122f71a406ca".toUUIDOrNull()!!.let { id -> Arguments.of(id, NODES.firstOrNull { it.id == id }) },
            "9684433a-9f5a-4b60-85e8-d4cd3ee58fe5".toUUIDOrNull()!!.let { id -> Arguments.of(id, NODES.firstOrNull { it.id == id }) },
        )

        @JvmStatic
        fun findByLists() = listOf(
            Arguments.of(
                listOf(
                    betweenEquals(PagePropsNode::propDatetime, "2025-11-01".toZonedDateTimeStart(), "2025-12-31".toZonedDateTimeEnd()),
                    greaterThanEquals(PagePropsNode::propValue, 30),
                    likeUnaccentedLower(PagePropsNode::propName, "ana"),
                ),
                listOf(
                    PageSort("prop_datetime", DESC),
                ),
                NODES.filter {
                    it.propDatetime >= "2025-11-01".toZonedDateTimeStart() &&
                            it.propDatetime <= "2025-12-31".toZonedDateTimeEnd() &&
                            it.propValue >= 30 &&
                            it.propName.unaccentedLower().contains("ana")

                }.sortedByDescending { it.propDatetime },
            ),
            Arguments.of(
                listOf(
                    greaterThanEquals(PagePropsNode::propDatetime, "2025-03-01".toZonedDateTimeStart()),
                    or(
                        betweenEquals(PagePropsNode::propValue, 50, 99),
                        equalsUnaccentedLower(PagePropsNode::propName, "jessica amaral")
                    )
                ),
                listOf(
                    PageSort("prop_value", DESC),
                    PageSort("prop_name", ASC),
                ),
                NODES.filter {
                    it.propDatetime >= "2025-03-01".toZonedDateTimeStart() &&
                            ((it.propValue in 50..99) || it.propName.unaccentedLower() == "jessica amaral")
                }.sortedWith(compareByDescending<PagePropsNode> { it.propValue }.thenBy { it.propName })
            ),
        )

        @JvmStatic
        fun counts() = listOf(
            Arguments.of(
                listOf(
                    betweenEquals(PagePropsNode::propDatetime, "2025-11-01".toZonedDateTimeStart(), "2025-12-31".toZonedDateTimeEnd()),
                    greaterThanEquals(PagePropsNode::propValue, 30),
                    likeUnaccentedLower(PagePropsNode::propName, "ana"),
                ),
                NODES.filter {
                    it.propDatetime >= "2025-11-01".toZonedDateTimeStart() &&
                            it.propDatetime <= "2025-12-31".toZonedDateTimeEnd() &&
                            it.propValue >= 30 &&
                            it.propName.unaccentedLower().contains("ana")

                }.sortedByDescending { it.propDatetime },
            ),
            Arguments.of(
                listOf(
                    greaterThanEquals(PagePropsNode::propDatetime, "2025-03-01".toZonedDateTimeStart()),
                    or(
                        betweenEquals(PagePropsNode::propValue, 50, 99),
                        equalsUnaccentedLower(PagePropsNode::propName, "jessica amaral")
                    )
                ),
                NODES.filter {
                    it.propDatetime >= "2025-03-01".toZonedDateTimeStart() &&
                            ((it.propValue in 50..99) || it.propName.unaccentedLower() == "jessica amaral")
                }.sortedWith(compareByDescending<PagePropsNode> { it.propValue }.thenBy { it.propName })
            ),
        )

        @JvmStatic
        fun findByPages() = listOf(
            Arguments.of(
                listOf(
                    greaterThanEquals(PagePropsNode::propDatetime, "2025-03-01".toZonedDateTimeStart()),
                    or(
                        betweenEquals(PagePropsNode::propValue, 50, 99),
                        equalsUnaccentedLower(PagePropsNode::propName, "jessica amaral")
                    )
                ),
                PageRequest(2, 4, listOf(PageSort("prop_value", DESC), PageSort("prop_name", ASC))),
                NODES.filter {
                    it.propDatetime >= "2025-03-01".toZonedDateTimeStart() &&
                            ((it.propValue in 50..99) || it.propName.unaccentedLower() == "jessica amaral")
                }.sortedWith(compareByDescending<PagePropsNode> { it.propValue }.thenBy { it.propName }).drop(4).take(4)
            ),
            Arguments.of(
                listOf(
                    greaterThanEquals(PagePropsNode::propDatetime, "2025-03-01".toZonedDateTimeStart()),
                    or(
                        betweenEquals(PagePropsNode::propValue, 50, 99),
                        equalsUnaccentedLower(PagePropsNode::propName, "jessica amaral")
                    )
                ),
                PageRequest(4, 5, listOf(PageSort("prop_value", DESC), PageSort("prop_name", ASC))),
                NODES.filter {
                    it.propDatetime >= "2025-03-01".toZonedDateTimeStart() &&
                            ((it.propValue in 50..99) || it.propName.unaccentedLower() == "jessica amaral")
                }.sortedWith(compareByDescending<PagePropsNode> { it.propValue }.thenBy { it.propName }).drop(15).take(5)
            ),
        )

    }

    @ParameterizedTest
    @MethodSource("sortingValuesSingle")
    fun `Sort single`(
        sort: PageSort,
        nodes: List<PagePropsNode>,
        property: PagePropsNode.() -> Any
    ) = runTest(StandardTestDispatcher()) {
        runScript("/page-sort.cypher")
        val result = repository.sortPageSearch(sort)
        nodes.forEachIndexed { index, node ->
            assertEquals(node.property(), result[index].property())
        }
    }

    @ParameterizedTest
    @MethodSource("sortingValuesList")
    fun `Sort list`(
        sorts: List<PageSort>,
        nodes: List<PagePropsNode>,
    ) = runTest(StandardTestDispatcher()) {
        runScript("/page-sort.cypher")
        val result = repository.sortPageSearch(sorts)
        nodes.forEachIndexed { index, node ->
            assertEquals(node, result[index])
        }
    }

    @ParameterizedTest
    @MethodSource("findByIds")
    fun `Find by id`(
        id: UUID,
        node: PagePropsNode?,
    ) = runTest(StandardTestDispatcher()) {
        runScript("/page-sort.cypher")
        val result = repository.findById(id)
        assertEquals(node, result)
    }

    @ParameterizedTest
    @MethodSource("findByLists")
    fun `Find by list`(
        filters: List<Filter>,
        sorts: List<PageSort>,
        nodes: List<PagePropsNode>,
    ) = runTest(StandardTestDispatcher()) {
        runScript("/page-sort.cypher")
        val result = repository.findList(filters, sorts)
        assertEquals(nodes.size, result.size)
        nodes.forEachIndexed { index, node ->
            assertEquals(node, result[index])
        }
    }

    @ParameterizedTest
    @MethodSource("counts")
    fun `Count total`(
        filters: List<Filter>,
        nodes: List<PagePropsNode>,
    ) = runTest(StandardTestDispatcher()) {
        runScript("/page-sort.cypher")
        val result = repository.countTotal(filters)
        assertEquals(nodes.size, result.toInt())
    }

    @ParameterizedTest
    @MethodSource("findByPages")
    fun `Find by page`(
        filters: List<Filter>,
        pageable: PageRequest,
        nodes: List<PagePropsNode>,
    ) = runTest(StandardTestDispatcher()) {
        runScript("/page-sort.cypher")
        val result = repository.findPage(filters, pageable)
        assertEquals(nodes.size, result.contentList.size)
        nodes.forEachIndexed { index, node ->
            assertEquals(node, result.contentList[index])
        }
    }

}
