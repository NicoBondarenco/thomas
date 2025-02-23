package com.thomas.database.neo4j.filter

import org.neo4j.ogm.cypher.ComparisonOperator.IS_TRUE
import org.neo4j.ogm.cypher.Filter

fun isTrue(
    property: String,
): Filter = Filter(property, IS_TRUE)

fun isFalse(
    property: String,
): Filter = isTrue(property).apply { isNegated = true }