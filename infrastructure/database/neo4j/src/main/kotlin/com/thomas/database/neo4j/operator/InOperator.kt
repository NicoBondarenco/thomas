package com.thomas.database.neo4j.operator

import org.neo4j.ogm.cypher.PropertyValueTransformer

enum class InOperator(
    private val operator: String,
    private val condition: String,
    private val builder: (prop: String, param: String) -> String,
) : FunctionOperator<Collection<Any?>> {

    IN("AND", "IS NOT", { prop, param -> "$prop IN \$`$param`" }),
    NOT_IN("OR", "IS", { prop, param -> "NOT($prop IN \$`$param`)" }),
    IN_INCLUDE_NULL("OR", "IS", { prop, param -> "$prop IN \$`$param`" }),
    NOT_IN_INCLUDE_NULL("AND", "IS NOT", { prop, param -> "NOT($prop IN \$`$param`)" });

    override fun expression(
        property: String,
        parameter: String,
    ): String = "(${this.builder(property, parameter)} ${this.operator} $property ${this.condition} NULL) "

    override fun parametersMap(
        parameter: String,
        value: Collection<Any?>,
        transformer: PropertyValueTransformer
    ): Map<String, Any> = mapOf(
        parameter to transformer.transformPropertyValue(value.filterNotNull()),
    )

}