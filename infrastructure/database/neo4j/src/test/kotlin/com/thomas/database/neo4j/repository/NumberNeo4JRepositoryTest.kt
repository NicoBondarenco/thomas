package com.thomas.database.neo4j.repository

import com.thomas.database.neo4j.node.NumberPropsNode
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.test.assertTrue
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


class NumberNeo4JRepositoryTest : Neo4JRepositoryTest() {

    companion object {

        private val NODES = listOf(
            NumberPropsNode(id = "96f46101-90ea-46f2-acc5-4e3c388ddd16", propInteger = 42, propLong = 907L, propBiginteger = BigInteger.valueOf(5500), propDouble = 5.75, propBigdecimal = BigDecimal("17.734")),
            NumberPropsNode(id = "95807ea5-ac39-4541-8d0d-73f433dab847", propInteger = 39, propLong = 283L, propBiginteger = BigInteger.valueOf(6712), propDouble = 47.844, propBigdecimal = BigDecimal("90.80")),
            NumberPropsNode(id = "bffdcfe2-03a6-4264-a84b-1018635697e2", propInteger = 87, propLong = 355L, propBiginteger = BigInteger.valueOf(7924), propDouble = 9.8852, propBigdecimal = BigDecimal("4105.4")),
            NumberPropsNode(id = "f0015df4-ed37-4751-a110-c271c21c8914", propInteger = 46, propLong = 291L, propBiginteger = BigInteger.valueOf(7681), propDouble = 958.64, propBigdecimal = BigDecimal("62.70")),
            NumberPropsNode(id = "b02de741-485d-4782-aea0-ff319a8545c1", propInteger = 35, propLong = 841L, propBiginteger = BigInteger.valueOf(5183), propDouble = 395.73, propBigdecimal = BigDecimal("62.52")),
            NumberPropsNode(id = "3e4f1a96-ce6a-4f83-b5c5-937f99f0ce8b", propInteger = 13, propLong = 149L, propBiginteger = BigInteger.valueOf(8477), propDouble = 40.71, propBigdecimal = BigDecimal("188.00")),
            NumberPropsNode(id = "ae410e61-09d6-422f-ac71-9db285cb419a", propInteger = 99, propLong = 784L, propBiginteger = BigInteger.valueOf(4810), propDouble = 790.34, propBigdecimal = BigDecimal("0.08")),
            NumberPropsNode(id = "fb9aa4ab-71a5-4178-8529-24697ebe6508", propInteger = 80, propLong = 117L, propBiginteger = BigInteger.valueOf(7245), propDouble = 32.0, propBigdecimal = BigDecimal("6.9642")),
            NumberPropsNode(id = "741f7742-dad6-48c5-a618-c5f8d8a866ef", propInteger = 28, propLong = 507L, propBiginteger = BigInteger.valueOf(5381), propDouble = 922.82, propBigdecimal = BigDecimal("305.86")),
            NumberPropsNode(id = "48b9f7bd-e619-4a0a-b854-9c8896c50de5", propInteger = 47, propLong = 320L, propBiginteger = BigInteger.valueOf(6573), propDouble = 409.71, propBigdecimal = BigDecimal("382.53")),
            NumberPropsNode(id = "868387b5-8a4d-4f28-ba55-bc332143291d", propInteger = 32, propLong = 613L, propBiginteger = BigInteger.valueOf(9054), propDouble = 707.00, propBigdecimal = BigDecimal("527.82")),
            NumberPropsNode(id = "396901c3-695b-4c5c-8757-d17cfdb17a8f", propInteger = 45, propLong = 947L, propBiginteger = BigInteger.valueOf(7914), propDouble = 682.26, propBigdecimal = BigDecimal("905.07")),
            NumberPropsNode(id = "3024bc15-8682-43af-9c9b-9791953c515b", propInteger = 78, propLong = 827L, propBiginteger = BigInteger.valueOf(2715), propDouble = 69.8980, propBigdecimal = BigDecimal("551.311")),
            NumberPropsNode(id = "3cecf044-e6fd-4af4-a50d-e791c0ceac48", propInteger = 30, propLong = 456L, propBiginteger = BigInteger.valueOf(5757), propDouble = 805.79, propBigdecimal = BigDecimal("3.58125")),
            NumberPropsNode(id = "cb49ed13-fdad-4a1e-8ab0-b5fa28945978", propInteger = 74, propLong = 313L, propBiginteger = BigInteger.valueOf(8071), propDouble = 0.04, propBigdecimal = BigDecimal("730.47")),
        )

        @JvmStatic
        fun numbersGreater() = listOf(
            46.let { number -> Arguments.of("prop_integer", number, NODES.filter { it.propInteger > number }) },
            80.let { number -> Arguments.of("prop_integer", number, NODES.filter { it.propInteger > number }) },
            0.let { number -> Arguments.of("prop_integer", number, NODES.filter { it.propInteger > number }) },
            507L.let { number -> Arguments.of("prop_long", number, NODES.filter { it.propLong > number }) },
            827L.let { number -> Arguments.of("prop_long", number, NODES.filter { it.propLong > number }) },
            0L.let { number -> Arguments.of("prop_long", number, NODES.filter { it.propLong > number }) },
            BigInteger.valueOf(5500).let { number -> Arguments.of("prop_biginteger", number, NODES.filter { it.propBiginteger > number }) },
            BigInteger.valueOf(7245).let { number -> Arguments.of("prop_biginteger", number, NODES.filter { it.propBiginteger > number }) },
            BigInteger.valueOf(0).let { number -> Arguments.of("prop_biginteger", number, NODES.filter { it.propBiginteger > number }) },
            32.0.let { number -> Arguments.of("prop_double", number, NODES.filter { it.propDouble.compareTo(number) > 0 }) },
            682.26.let { number -> Arguments.of("prop_double", number, NODES.filter { it.propDouble.compareTo(number) > 0 }) },
            0.0.let { number -> Arguments.of("prop_double", number, NODES.filter { it.propDouble.compareTo(number) > 0 }) },
            BigDecimal("188.00").let { number -> Arguments.of("prop_bigdecimal", number, NODES.filter { it.propBigdecimal.compareTo(number) > 0 }) },
            BigDecimal("551.31").let { number -> Arguments.of("prop_bigdecimal", number, NODES.filter { it.propBigdecimal.compareTo(number) > 0 }) },
            BigDecimal("0.0").let { number -> Arguments.of("prop_bigdecimal", number, NODES.filter { it.propBigdecimal.compareTo(number) > 0 }) },
        )

        @JvmStatic
        fun numbersGreaterEquals() = listOf(
            46.let { number -> Arguments.of("prop_integer", number, NODES.filter { it.propInteger >= number }) },
            80.let { number -> Arguments.of("prop_integer", number, NODES.filter { it.propInteger >= number }) },
            0.let { number -> Arguments.of("prop_integer", number, NODES.filter { it.propInteger >= number }) },
            507L.let { number -> Arguments.of("prop_long", number, NODES.filter { it.propLong >= number }) },
            827L.let { number -> Arguments.of("prop_long", number, NODES.filter { it.propLong >= number }) },
            0L.let { number -> Arguments.of("prop_long", number, NODES.filter { it.propLong >= number }) },
            BigInteger.valueOf(5500).let { number -> Arguments.of("prop_biginteger", number, NODES.filter { it.propBiginteger >= number }) },
            BigInteger.valueOf(7245).let { number -> Arguments.of("prop_biginteger", number, NODES.filter { it.propBiginteger >= number }) },
            BigInteger.valueOf(0).let { number -> Arguments.of("prop_biginteger", number, NODES.filter { it.propBiginteger >= number }) },
            32.0.let { number -> Arguments.of("prop_double", number, NODES.filter { it.propDouble.compareTo(number) >= 0 }) },
            682.26.let { number -> Arguments.of("prop_double", number, NODES.filter { it.propDouble.compareTo(number) >= 0 }) },
            0.0.let { number -> Arguments.of("prop_double", number, NODES.filter { it.propDouble.compareTo(number) >= 0 }) },
            BigDecimal("188.00").let { number -> Arguments.of("prop_bigdecimal", number, NODES.filter { it.propBigdecimal.compareTo(number) >= 0 }) },
            BigDecimal("551.31").let { number -> Arguments.of("prop_bigdecimal", number, NODES.filter { it.propBigdecimal.compareTo(number) >= 0 }) },
            BigDecimal("0.0").let { number -> Arguments.of("prop_bigdecimal", number, NODES.filter { it.propBigdecimal.compareTo(number) >= 0 }) },
        )

        @JvmStatic
        fun numbersLess() = listOf(
            39.let { number -> Arguments.of("prop_integer", number, NODES.filter { it.propInteger < number }) },
            47.let { number -> Arguments.of("prop_integer", number, NODES.filter { it.propInteger < number }) },
            0.let { number -> Arguments.of("prop_integer", number, NODES.filter { it.propInteger < number }) },
            841L.let { number -> Arguments.of("prop_long", number, NODES.filter { it.propLong < number }) },
            355L.let { number -> Arguments.of("prop_long", number, NODES.filter { it.propLong < number }) },
            0L.let { number -> Arguments.of("prop_long", number, NODES.filter { it.propLong < number }) },
            BigInteger.valueOf(5500).let { number -> Arguments.of("prop_biginteger", number, NODES.filter { it.propBiginteger < number }) },
            BigInteger.valueOf(8071).let { number -> Arguments.of("prop_biginteger", number, NODES.filter { it.propBiginteger < number }) },
            BigInteger.valueOf(0).let { number -> Arguments.of("prop_biginteger", number, NODES.filter { it.propBiginteger < number }) },
            46.71.let { number -> Arguments.of("prop_double", number, NODES.filter { it.propDouble.compareTo(number) < 0 }) },
            707.00.let { number -> Arguments.of("prop_double", number, NODES.filter { it.propDouble.compareTo(number) < 0 }) },
            0.0.let { number -> Arguments.of("prop_double", number, NODES.filter { it.propDouble.compareTo(number) < 0 }) },
            BigDecimal("188.00").let { number -> Arguments.of("prop_bigdecimal", number, NODES.filter { it.propBigdecimal.compareTo(number) < 0 }) },
            BigDecimal("551.31").let { number -> Arguments.of("prop_bigdecimal", number, NODES.filter { it.propBigdecimal.compareTo(number) < 0 }) },
            BigDecimal("0.0").let { number -> Arguments.of("prop_bigdecimal", number, NODES.filter { it.propBigdecimal.compareTo(number) < 0 }) },
        )

        @JvmStatic
        fun numbersLessEquals() = listOf(
            39.let { number -> Arguments.of("prop_integer", number, NODES.filter { it.propInteger <= number }) },
            47.let { number -> Arguments.of("prop_integer", number, NODES.filter { it.propInteger <= number }) },
            0.let { number -> Arguments.of("prop_integer", number, NODES.filter { it.propInteger <= number }) },
            841L.let { number -> Arguments.of("prop_long", number, NODES.filter { it.propLong <= number }) },
            355L.let { number -> Arguments.of("prop_long", number, NODES.filter { it.propLong <= number }) },
            0L.let { number -> Arguments.of("prop_long", number, NODES.filter { it.propLong <= number }) },
            BigInteger.valueOf(5500).let { number -> Arguments.of("prop_biginteger", number, NODES.filter { it.propBiginteger <= number }) },
            BigInteger.valueOf(8071).let { number -> Arguments.of("prop_biginteger", number, NODES.filter { it.propBiginteger <= number }) },
            BigInteger.valueOf(0).let { number -> Arguments.of("prop_biginteger", number, NODES.filter { it.propBiginteger <= number }) },
            46.71.let { number -> Arguments.of("prop_double", number, NODES.filter { it.propDouble.compareTo(number) <= 0 }) },
            707.00.let { number -> Arguments.of("prop_double", number, NODES.filter { it.propDouble.compareTo(number) <= 0 }) },
            0.0.let { number -> Arguments.of("prop_double", number, NODES.filter { it.propDouble.compareTo(number) <= 0 }) },
            BigDecimal("188.00").let { number -> Arguments.of("prop_bigdecimal", number, NODES.filter { it.propBigdecimal.compareTo(number) <= 0 }) },
            BigDecimal("551.31").let { number -> Arguments.of("prop_bigdecimal", number, NODES.filter { it.propBigdecimal.compareTo(number) <= 0 }) },
            BigDecimal("0.0").let { number -> Arguments.of("prop_bigdecimal", number, NODES.filter { it.propBigdecimal.compareTo(number) <= 0 }) },
        )

        @JvmStatic
        fun numbersBetween() = listOf(
            Pair(39, 78).let { (n1, n2) ->
                Arguments.of("prop_integer", n1, n2, NODES.filter {
                    it.propInteger in (n1 + 1)..<n2
                })
            },
            Pair(355L, 841L).let { (n1, n2) ->
                Arguments.of("prop_long", n1, n2, NODES.filter {
                    it.propLong in (n1 + 1)..<n2
                })
            },
            Pair(BigInteger.valueOf(5500), BigInteger.valueOf(7245)).let { (n1, n2) ->
                Arguments.of("prop_biginteger", n1, n2, NODES.filter {
                    it.propBiginteger in (n1.plus(BigInteger.ONE))..<n2
                })
            },
            Pair(46.71, 409.71).let { (n1, n2) ->
                Arguments.of("prop_double", n1, n2, NODES.filter {
                    it.propDouble.compareTo(n1) > 0 && it.propDouble.compareTo(n2) < 0
                })
            },
            Pair(BigDecimal("188.00"), BigDecimal("382.53")).let { (n1, n2) ->
                Arguments.of("prop_bigdecimal", n1, n2, NODES.filter {
                    it.propBigdecimal.compareTo(n1) > 0 && it.propBigdecimal.compareTo(n2) < 0
                })
            },
        )

        @JvmStatic
        fun numbersBetweenEquals() = listOf(
            Pair(39, 78).let { (n1, n2) ->
                Arguments.of("prop_integer", n1, n2, NODES.filter {
                    it.propInteger in n1..n2
                })
            },
            Pair(355L, 841L).let { (n1, n2) ->
                Arguments.of("prop_long", n1, n2, NODES.filter {
                    it.propLong in n1..n2
                })
            },
            Pair(BigInteger.valueOf(5500), BigInteger.valueOf(7245)).let { (n1, n2) ->
                Arguments.of("prop_biginteger", n1, n2, NODES.filter {
                    it.propBiginteger in n1..n2
                })
            },
            Pair(46.71, 409.71).let { (n1, n2) ->
                Arguments.of("prop_double", n1, n2, NODES.filter {
                    it.propDouble.compareTo(n1) >= 0 && it.propDouble.compareTo(n2) <= 0
                })
            },
            Pair(BigDecimal("188.00"), BigDecimal("382.53")).let { (n1, n2) ->
                Arguments.of("prop_bigdecimal", n1, n2, NODES.filter {
                    it.propBigdecimal.compareTo(n1) >= 0 && it.propBigdecimal.compareTo(n2) <= 0
                })
            },
        )

        @JvmStatic
        fun numbersNotBetween() = listOf(
            Pair(39, 78).let { (n1, n2) ->
                Arguments.of("prop_integer", n1, n2, NODES.filter {
                    it.propInteger in (n2 + 1)..<n1
                })
            },
            Pair(355L, 841L).let { (n1, n2) ->
                Arguments.of("prop_long", n1, n2, NODES.filter {
                    it.propLong in (n2 + 1)..<n1
                })
            },
            Pair(BigInteger.valueOf(5500), BigInteger.valueOf(7245)).let { (n1, n2) ->
                Arguments.of("prop_biginteger", n1, n2, NODES.filter {
                    it.propBiginteger < n1 && it.propBiginteger > n2
                })
            },
            Pair(46.71, 409.71).let { (n1, n2) ->
                Arguments.of("prop_double", n1, n2, NODES.filter {
                    it.propDouble.compareTo(n1) < 0 && it.propDouble.compareTo(n2) > 0
                })
            },
            Pair(BigDecimal("188.00"), BigDecimal("382.53")).let { (n1, n2) ->
                Arguments.of("prop_bigdecimal", n1, n2, NODES.filter {
                    it.propBigdecimal.compareTo(n1) < 0 && it.propBigdecimal.compareTo(n2) > 0
                })
            },
        )

        @JvmStatic
        fun numbersNotBetweenEquals() = listOf(
            Pair(39, 78).let { (n1, n2) ->
                Arguments.of("prop_integer", n1, n2, NODES.filter {
                    it.propInteger in n2..n1
                })
            },
            Pair(355L, 841L).let { (n1, n2) ->
                Arguments.of("prop_long", n1, n2, NODES.filter {
                    it.propLong in (n2 + 1)..<n1
                })
            },
            Pair(BigInteger.valueOf(5500), BigInteger.valueOf(7245)).let { (n1, n2) ->
                Arguments.of("prop_biginteger", n1, n2, NODES.filter {
                    it.propBiginteger < n1 && it.propBiginteger > n2
                })
            },
            Pair(46.71, 409.71).let { (n1, n2) ->
                Arguments.of("prop_double", n1, n2, NODES.filter {
                    it.propDouble.compareTo(n1) < 0 && it.propDouble.compareTo(n2) > 0
                })
            },
            Pair(BigDecimal("188.00"), BigDecimal("382.53")).let { (n1, n2) ->
                Arguments.of("prop_bigdecimal", n1, n2, NODES.filter {
                    it.propBigdecimal.compareTo(n1) < 0 && it.propBigdecimal.compareTo(n2) > 0
                })
            },
        )

    }

    @ParameterizedTest
    @MethodSource("numbersGreater")
    fun `Numbers greater than`(
        property: String,
        value: Number,
        nodes: List<NumberPropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/numbers-range.cypher")
        val result = repository.numberGreaterThan(property, value)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("numbersGreaterEquals")
    fun `Numbers greater than equals`(
        property: String,
        value: Number,
        nodes: List<NumberPropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/numbers-range.cypher")
        val result = repository.numberGreaterThanEquals(property, value)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("numbersLess")
    fun `Numbers less than`(
        property: String,
        value: Number,
        nodes: List<NumberPropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/numbers-range.cypher")
        val result = repository.numberLessThan(property, value)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("numbersLessEquals")
    fun `Numbers less than equals`(
        property: String,
        value: Number,
        nodes: List<NumberPropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/numbers-range.cypher")
        val result = repository.numberLessThanEquals(property, value)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("numbersBetween")
    fun `Numbers between`(
        property: String,
        min: Number,
        max: Number,
        nodes: List<NumberPropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/numbers-range.cypher")
        val result = repository.numberBetween(property, min, max)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("numbersBetweenEquals")
    fun `Numbers between equals`(
        property: String,
        min: Number,
        max: Number,
        nodes: List<NumberPropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/numbers-range.cypher")
        val result = repository.numberBetweenEquals(property, min, max)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("numbersNotBetween")
    fun `Numbers not between`(
        property: String,
        min: Number,
        max: Number,
        nodes: List<NumberPropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/numbers-range.cypher")
        val result = repository.numberNotBetween(property, min, max)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

    @ParameterizedTest
    @MethodSource("numbersNotBetweenEquals")
    fun `Numbers not between equals`(
        property: String,
        min: Number,
        max: Number,
        nodes: List<NumberPropsNode>
    ) = runTest(StandardTestDispatcher()) {
        runScript("/numbers-range.cypher")
        val result = repository.numberNotBetweenEquals(property, min, max)
        assertEquals(nodes.size, result.size)
        nodes.forEach { node ->
            assertTrue(result.contains(node))
        }
    }

}
