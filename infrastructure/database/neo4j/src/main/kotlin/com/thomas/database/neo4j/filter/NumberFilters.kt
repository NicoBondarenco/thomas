package com.thomas.database.neo4j.filter

import com.thomas.database.neo4j.function.GenericFilterFunction
import com.thomas.database.neo4j.operator.BetweenOperator.BETWEEN
import com.thomas.database.neo4j.operator.BetweenOperator.BETWEEN_EQUALS
import com.thomas.database.neo4j.operator.BetweenOperator.NOT_BETWEEN
import com.thomas.database.neo4j.operator.BetweenOperator.NOT_BETWEEN_EQUALS
import com.thomas.database.neo4j.operator.SingleOperator
import com.thomas.database.neo4j.operator.SingleOperator.GREATER_THAN_EQUALS
import com.thomas.database.neo4j.operator.SingleOperator.LESS_THAN_EQUALS
import org.neo4j.ogm.cypher.Filter

fun greaterThan(
    property: String,
    value: Number,
): Filter = Filter(property, GenericFilterFunction(value, SingleOperator.GREATER_THAN))

fun greaterThanEquals(
    property: String,
    value: Number,
): Filter = Filter(property, GenericFilterFunction(value, GREATER_THAN_EQUALS))

fun lessThan(
    property: String,
    value: Number,
): Filter = Filter(property, GenericFilterFunction(value, SingleOperator.LESS_THAN))

fun lessThanEquals(
    property: String,
    value: Number,
): Filter = Filter(property, GenericFilterFunction(value, LESS_THAN_EQUALS))

fun between(
    property: String,
    min: Number,
    max: Number,
): Filter = Filter(property, GenericFilterFunction(Pair(min, max), BETWEEN))

fun betweenEquals(
    property: String,
    min: Number,
    max: Number,
): Filter = Filter(property, GenericFilterFunction(Pair(min, max), BETWEEN_EQUALS))

fun notBetween(
    property: String,
    min: Number,
    max: Number,
): Filter = Filter(property, GenericFilterFunction(Pair(min, max), NOT_BETWEEN))

fun notBetweenEquals(
    property: String,
    min: Number,
    max: Number,
): Filter = Filter(property, GenericFilterFunction(Pair(min, max), NOT_BETWEEN_EQUALS))
