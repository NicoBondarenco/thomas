package com.thomas.management.data.neo4j.filter

import com.thomas.core.extension.unaccentedLower
import java.util.function.UnaryOperator
import org.neo4j.ogm.cypher.ComparisonOperator.EQUALS
import org.neo4j.ogm.cypher.ComparisonOperator.GREATER_THAN
import org.neo4j.ogm.cypher.ComparisonOperator.GREATER_THAN_EQUAL
import org.neo4j.ogm.cypher.ComparisonOperator.LESS_THAN
import org.neo4j.ogm.cypher.ComparisonOperator.LESS_THAN_EQUAL
import org.neo4j.ogm.cypher.Filter
import org.neo4j.ogm.cypher.PropertyValueTransformer
import org.neo4j.ogm.cypher.function.FilterFunction

interface Neo4JOperator<V : Any> {

    val operator: String

    fun expression(property: String, parameter: String): String

    fun valueTransformer(value: V): Any = value

}

class UnaccentedLowerNeo4JOperator(
    override val operator: String,
) : Neo4JOperator<String> {

    override fun expression(
        property: String,
        parameter: String,
    ): String = "thomas.unaccentLower($property) $operator \$`$parameter` "

    override fun valueTransformer(value: String): Any = value.unaccentedLower()
}

class Neo4JFilterFunction<V : Any>(
    private val value: V,
    private val operator: Neo4JOperator<V>
) : FilterFunction<Any> {

    companion object {
        private const val PARAMETER_NAME = "property"
    }

    override fun getValue(): V = value

    private fun transformedValue(): Any = operator.valueTransformer(getValue())

    override fun expression(
        nodeIdentifier: String,
        filteredProperty: String,
        createUniqueParameterName: UnaryOperator<String>
    ): String = operator.expression(
        "$nodeIdentifier.`$filteredProperty`",
        createUniqueParameterName.apply(PARAMETER_NAME)
    )

    override fun parameters(
        createUniqueParameterName: UnaryOperator<String>,
        valueTransformer: PropertyValueTransformer
    ): Map<String, Any> = mapOf(
        createUniqueParameterName.apply(PARAMETER_NAME) to valueTransformer.transformPropertyValue(transformedValue())
    )

}

fun equal(
    property: String,
    value: Any,
): Filter = Filter(property, EQUALS, value)

fun notEqual(
    property: String,
    value: Any,
): Filter = equal(property, value).apply { isNegated = true }

fun equalUnaccentedLower(
    property: String,
    value: String,
): Filter = Filter(property, Neo4JFilterFunction(value, UnaccentedLowerNeo4JOperator(EQUALS.value)))

fun notEqualUnaccentedLower(
    property: String,
    value: String,
): Filter = equalUnaccentedLower(property, value).apply { isNegated = true }

fun greaterThan(
    property: String,
    value: Number,
): Filter = Filter(property, GREATER_THAN, value)

fun greaterThanEqual(
    property: String,
    value: Number,
): Filter = Filter(property, GREATER_THAN_EQUAL, value)

fun lessThan(
    property: String,
    value: Number,
): Filter = Filter(property, LESS_THAN, value)

fun lessThanEqual(
    property: String,
    value: Number,
): Filter = Filter(property, LESS_THAN_EQUAL, value)
