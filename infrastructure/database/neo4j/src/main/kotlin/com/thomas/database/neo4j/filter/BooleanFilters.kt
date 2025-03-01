package com.thomas.database.neo4j.filter

import com.thomas.database.neo4j.node.Neo4JNode
import kotlin.reflect.KProperty
import org.neo4j.ogm.cypher.ComparisonOperator.IS_TRUE
import org.neo4j.ogm.cypher.Filter

fun <T : Any> isTrue(
    property: KProperty<T?>
): Filter = Filter(property.nodePropertyName(), IS_TRUE)

fun <T : Any> isFalse(
    property: KProperty<T?>
): Filter = isTrue(property).apply { isNegated = true }

fun <T : Neo4JNode> isTrue(
    propertyName: String,
    nestedProperty: KProperty<T?>
): Filter = Filter(propertyName, IS_TRUE).applyNested(nestedProperty)

fun <T : Neo4JNode> isFalse(
    propertyName: String,
    nestedProperty: KProperty<T>
): Filter = isTrue(propertyName, nestedProperty).apply { isNegated = true }
