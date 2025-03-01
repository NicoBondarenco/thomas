package com.thomas.database.neo4j.filter

import com.thomas.database.neo4j.function.GenericFilterFunction
import com.thomas.database.neo4j.node.Neo4JNode
import com.thomas.database.neo4j.operator.BetweenOperator.BETWEEN
import com.thomas.database.neo4j.operator.BetweenOperator.BETWEEN_EQUALS
import com.thomas.database.neo4j.operator.BetweenOperator.NOT_BETWEEN
import com.thomas.database.neo4j.operator.BetweenOperator.NOT_BETWEEN_EQUALS
import com.thomas.database.neo4j.operator.SingleOperator.GREATER_THAN
import com.thomas.database.neo4j.operator.SingleOperator.GREATER_THAN_EQUALS
import com.thomas.database.neo4j.operator.SingleOperator.LESS_THAN
import com.thomas.database.neo4j.operator.SingleOperator.LESS_THAN_EQUALS
import java.time.temporal.Temporal
import kotlin.reflect.KProperty
import org.neo4j.ogm.cypher.Filter

fun <T : Any> greaterThan(
    property: KProperty<T?>,
    value: Temporal,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, GREATER_THAN))

fun <T : Any> greaterThanEquals(
    property: KProperty<T?>,
    value: Temporal,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, GREATER_THAN_EQUALS))

fun <T : Any> lessThan(
    property: KProperty<T?>,
    value: Temporal,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, LESS_THAN))

fun <T : Any> lessThanEquals(
    property: KProperty<T?>,
    value: Temporal,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, LESS_THAN_EQUALS))

fun <T : Any> between(
    property: KProperty<T?>,
    min: Temporal,
    max: Temporal,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(Pair(min, max), BETWEEN))

fun <T : Any> betweenEquals(
    property: KProperty<T?>,
    min: Temporal,
    max: Temporal,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(Pair(min, max), BETWEEN_EQUALS))

fun <T : Any> notBetween(
    property: KProperty<T?>,
    min: Temporal,
    max: Temporal,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(Pair(min, max), NOT_BETWEEN))

fun <T : Any> notBetweenEquals(
    property: KProperty<T?>,
    min: Temporal,
    max: Temporal,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(Pair(min, max), NOT_BETWEEN_EQUALS))

fun <K : Any, T : Neo4JNode> greaterThan(
    property: KProperty<K?>,
    nestedProperty: KProperty<T?>,
    value: Temporal,
): Filter = Filter(
    property.nodePropertyName(),
    GenericFilterFunction(value, GREATER_THAN)
).applyNested(nestedProperty)

fun <K : Any, T : Neo4JNode> greaterThanEquals(
    property: KProperty<K?>,
    nestedProperty: KProperty<T?>,
    value: Temporal,
): Filter = Filter(
    property.nodePropertyName(),
    GenericFilterFunction(value, GREATER_THAN_EQUALS)
).applyNested(nestedProperty)

fun <K : Any, T : Neo4JNode> lessThan(
    property: KProperty<K?>,
    nestedProperty: KProperty<T?>,
    value: Temporal,
): Filter = Filter(
    property.nodePropertyName(),
    GenericFilterFunction(value, LESS_THAN)
).applyNested(nestedProperty)

fun <K : Any, T : Neo4JNode> lessThanEquals(
    property: KProperty<K?>,
    nestedProperty: KProperty<T?>,
    value: Temporal,
): Filter = Filter(
    property.nodePropertyName(),
    GenericFilterFunction(value, LESS_THAN_EQUALS)
).applyNested(nestedProperty)

fun <K : Any, T : Neo4JNode> between(
    property: KProperty<K?>,
    nestedProperty: KProperty<T?>,
    min: Temporal,
    max: Temporal,
): Filter = Filter(
    property.nodePropertyName(),
    GenericFilterFunction(Pair(min, max), BETWEEN)
).applyNested(nestedProperty)

fun <K : Any, T : Neo4JNode> betweenEquals(
    property: KProperty<K?>,
    nestedProperty: KProperty<T?>,
    min: Temporal,
    max: Temporal,
): Filter = Filter(
    property.nodePropertyName(),
    GenericFilterFunction(Pair(min, max), BETWEEN_EQUALS)
).applyNested(nestedProperty)

fun <K : Any, T : Neo4JNode> notBetween(
    property: KProperty<K?>,
    nestedProperty: KProperty<T?>,
    min: Temporal,
    max: Temporal,
): Filter = Filter(
    property.nodePropertyName(),
    GenericFilterFunction(Pair(min, max), NOT_BETWEEN)
).applyNested(nestedProperty)

fun <K : Any, T : Neo4JNode> notBetweenEquals(
    property: KProperty<K?>,
    nestedProperty: KProperty<T?>,
    min: Temporal,
    max: Temporal,
): Filter = Filter(
    property.nodePropertyName(),
    GenericFilterFunction(Pair(min, max), NOT_BETWEEN_EQUALS)
).applyNested(nestedProperty)
