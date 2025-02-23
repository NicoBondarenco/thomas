package com.thomas.database.neo4j.operator

import org.neo4j.ogm.cypher.PropertyValueTransformer

interface FunctionOperator<V : Any> {

    fun expression(property: String, parameter: String): String

    fun parametersMap(
        parameter: String,
        value: V,
        transformer: PropertyValueTransformer
    ): Map<String, Any> = mapOf(
        parameter to transformer.transformPropertyValue(value),
    )

}
