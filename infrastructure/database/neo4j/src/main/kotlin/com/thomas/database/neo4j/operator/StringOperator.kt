package com.thomas.database.neo4j.operator

import com.thomas.core.extension.unaccentedLower
import org.neo4j.ogm.cypher.PropertyValueTransformer

enum class StringOperator(
    private val operator: String,
    private val builder: StringOperator.(prop: String, param: String) -> String,
    private val transformer: (value: String) -> String,
) : FunctionOperator<String> {

    EQUALS_UNACCENTED_LOWER(
        "=",
        { prop, param -> "${prop.unaccentLower()} $operator \$`$param`" },
        { it.unaccentedLower() }),
    NOT_EQUALS_UNACCENTED_LOWER(
        "=",
        { prop, param -> "${prop.unaccentLower()} $operator \$`$param`".negate() },
        { it.unaccentedLower() }),
    LIKE(
        "=~",
        { prop, param -> "$prop $operator \$`$param`" },
        { it.toLike() }),
    NOT_LIKE(
        "=~",
        { prop, param -> "$prop $operator \$`$param`".negate() },
        { it.toLike() }),
    LIKE_UNACCENTED_LOWER(
        "=~",
        { prop, param -> "${prop.unaccentLower()} $operator \$`$param`" },
        { it.unaccentedLower().toLike() }),
    NOT_LIKE_UNACCENTED_LOWER(
        "=~",
        { prop, param -> "${prop.unaccentLower()} $operator \$`$param`".negate() },
        { it.unaccentedLower().toLike() }),
    STARTS_WITH(
        "STARTS WITH",
        { prop, param -> "$prop $operator \$`$param`" },
        { it }),
    NOT_STARTS_WITH(
        "STARTS WITH",
        { prop, param -> "$prop $operator \$`$param`".negate() },
        { it }),
    STARTS_WITH_UNACCENTED_LOWER(
        "STARTS WITH",
        { prop, param -> "${prop.unaccentLower()} $operator \$`$param`" },
        { it.unaccentedLower() }),
    NOT_STARTS_WITH_UNACCENTED_LOWER(
        "STARTS WITH",
        { prop, param -> "${prop.unaccentLower()} $operator \$`$param`".negate() },
        { it.unaccentedLower() }),
    ENDS_WITH(
        "ENDS WITH",
        { prop, param -> "$prop $operator \$`$param`" },
        { it }),
    NOT_ENDS_WITH(
        "ENDS WITH",
        { prop, param -> "$prop $operator \$`$param`".negate() },
        { it }),
    ENDS_WITH_UNACCENTED_LOWER(
        "ENDS WITH",
        { prop, param -> "${prop.unaccentLower()} $operator \$`$param`" },
        { it.unaccentedLower() }),
    NOT_ENDS_WITH_UNACCENTED_LOWER(
        "ENDS WITH",
        { prop, param -> "${prop.unaccentLower()} $operator \$`$param`".negate() },
        { it.unaccentedLower() });

    override fun expression(
        property: String,
        parameter: String,
    ): String = "(${this.builder(property, parameter)}) "

    override fun parametersMap(
        parameter: String,
        value: String,
        transformer: PropertyValueTransformer
    ): Map<String, Any> = mapOf(
        parameter to transformer.transformPropertyValue(this.transformer(value)),
    )


}
