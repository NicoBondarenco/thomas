package com.thomas.database.neo4j.operator

import org.neo4j.ogm.cypher.PropertyValueTransformer

enum class BetweenOperator(
    private val minOperator: String,
    private val maxOperator: String,
) : FunctionOperator<Pair<Any, Any>> {

    BETWEEN(">", "<"),
    BETWEEN_EQUALS(">=", "<="),
    NOT_BETWEEN("<", ">"),
    NOT_BETWEEN_EQUALS("<=", ">=");

    override fun expression(
        property: String,
        parameter: String,
    ): String = "($property $minOperator \$`${parameter.minParameter()}` AND $property $maxOperator \$`${parameter.maxParameter()}`) "

    override fun parametersMap(
        parameter: String,
        value: Pair<Any, Any>,
        transformer: PropertyValueTransformer
    ) = mapOf(
        parameter.minParameter() to transformer.transformPropertyValue(value.first),
        parameter.maxParameter() to transformer.transformPropertyValue(value.second),
    )

    private fun String.minParameter() = "min_${this}"

    private fun String.maxParameter() = "max_${this}"

}