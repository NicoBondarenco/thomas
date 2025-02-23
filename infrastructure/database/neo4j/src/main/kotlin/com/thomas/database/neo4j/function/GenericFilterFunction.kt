package com.thomas.database.neo4j.function

import com.thomas.database.neo4j.operator.FunctionOperator
import java.util.function.UnaryOperator
import org.neo4j.ogm.cypher.PropertyValueTransformer
import org.neo4j.ogm.cypher.function.FilterFunction

open class GenericFilterFunction<V : Any>(
    private val value: V,
    private val operator: FunctionOperator<V>
) : FilterFunction<Any> {

    companion object {
        private const val PARAMETER_NAME = "property"
    }

    override fun getValue(): V = value

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
    ): Map<String, Any> = operator.parametersMap(
        createUniqueParameterName.apply(PARAMETER_NAME),
        getValue(),
        valueTransformer
    )

}
