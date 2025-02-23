package com.thomas.database.neo4j.function

import java.util.function.UnaryOperator
import org.neo4j.ogm.cypher.BooleanOperator
import org.neo4j.ogm.cypher.Filter
import org.neo4j.ogm.cypher.PropertyValueTransformer
import org.neo4j.ogm.cypher.function.FilterFunction

open class ConditionalFilterFunction(
    private val value: List<Filter>,
    private val condition: BooleanOperator
) : FilterFunction<List<Filter>> {

    override fun getValue(): List<Filter> = value

    override fun expression(
        nodeIdentifier: String,
        filteredProperty: String,
        createUniqueParameterName: UnaryOperator<String>
    ): String = value.joinToString(" $condition ") {
        it.toCypher(nodeIdentifier, false)
    }.isolate()

    override fun parameters(
        createUniqueParameterName: UnaryOperator<String>,
        valueTransformer: PropertyValueTransformer
    ): Map<String, Any> = value.map {
        it.parameters()
    }.reduce { a, b -> a + b }

    private fun String.isolate() = "($this) "

}
