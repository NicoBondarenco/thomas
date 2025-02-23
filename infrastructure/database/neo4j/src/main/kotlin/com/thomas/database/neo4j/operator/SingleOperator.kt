package com.thomas.database.neo4j.operator

enum class SingleOperator(
    private val operator: String,
) : FunctionOperator<Any> {

    GREATER_THAN(">"),
    GREATER_THAN_EQUALS(">="),
    LESS_THAN("<"),
    LESS_THAN_EQUALS("<=");

    override fun expression(
        property: String,
        parameter: String,
    ): String = "($property $operator \$`$parameter`) "

}