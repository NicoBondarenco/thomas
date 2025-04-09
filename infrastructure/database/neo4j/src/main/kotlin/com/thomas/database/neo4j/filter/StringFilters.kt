package com.thomas.database.neo4j.filter

import com.thomas.database.neo4j.function.GenericFilterFunction
import com.thomas.database.neo4j.node.Neo4JNode
import com.thomas.database.neo4j.operator.StringOperator.ENDS_WITH
import com.thomas.database.neo4j.operator.StringOperator.ENDS_WITH_UNACCENTED_LOWER
import com.thomas.database.neo4j.operator.StringOperator.EQUALS_UNACCENTED_LOWER
import com.thomas.database.neo4j.operator.StringOperator.LIKE
import com.thomas.database.neo4j.operator.StringOperator.LIKE_UNACCENTED_LOWER
import com.thomas.database.neo4j.operator.StringOperator.NOT_ENDS_WITH
import com.thomas.database.neo4j.operator.StringOperator.NOT_ENDS_WITH_UNACCENTED_LOWER
import com.thomas.database.neo4j.operator.StringOperator.NOT_EQUALS_UNACCENTED_LOWER
import com.thomas.database.neo4j.operator.StringOperator.NOT_LIKE
import com.thomas.database.neo4j.operator.StringOperator.NOT_LIKE_UNACCENTED_LOWER
import com.thomas.database.neo4j.operator.StringOperator.NOT_STARTS_WITH
import com.thomas.database.neo4j.operator.StringOperator.NOT_STARTS_WITH_UNACCENTED_LOWER
import com.thomas.database.neo4j.operator.StringOperator.STARTS_WITH
import com.thomas.database.neo4j.operator.StringOperator.STARTS_WITH_UNACCENTED_LOWER
import java.io.Serializable
import kotlin.reflect.KProperty
import org.neo4j.ogm.cypher.Filter

fun <K : Any> equalsUnaccentedLower(
    property: KProperty<K?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, EQUALS_UNACCENTED_LOWER))

fun <K : Any> notEqualsUnaccentedLower(
    property: KProperty<K?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, NOT_EQUALS_UNACCENTED_LOWER))

fun <K : Any> like(
    property: KProperty<K?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, LIKE))

fun <K : Any> notLike(
    property: KProperty<K?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, NOT_LIKE))

fun <K : Any> likeUnaccentedLower(
    property: KProperty<K?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, LIKE_UNACCENTED_LOWER))

fun <K : Any> notLikeUnaccentedLower(
    property: KProperty<K?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, NOT_LIKE_UNACCENTED_LOWER))

fun <K : Any> startsWith(
    property: KProperty<K?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, STARTS_WITH))

fun <K : Any> notStartsWith(
    property: KProperty<K?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, NOT_STARTS_WITH))

fun <K : Any> startsWithUnaccentedLower(
    property: KProperty<K?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, STARTS_WITH_UNACCENTED_LOWER))

fun <K : Any> notStartsWithUnaccentedLower(
    property: KProperty<K?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, NOT_STARTS_WITH_UNACCENTED_LOWER))

fun <K : Any> endsWith(
    property: KProperty<K?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, ENDS_WITH))

fun <K : Any> notEndsWith(
    property: KProperty<K?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, NOT_ENDS_WITH))

fun <K : Any> endsWithUnaccentedLower(
    property: KProperty<K?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, ENDS_WITH_UNACCENTED_LOWER))

fun <K : Any> notEndsWithUnaccentedLower(
    property: KProperty<K?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, NOT_ENDS_WITH_UNACCENTED_LOWER))

fun <ID : Serializable, K : Any, T : Neo4JNode<ID>> equalsUnaccentedLower(
    property: KProperty<K?>,
    nested: KProperty<T?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, EQUALS_UNACCENTED_LOWER)).applyNested(nested)

fun <ID : Serializable, K : Any, T : Neo4JNode<ID>> notEqualsUnaccentedLower(
    property: KProperty<K?>,
    nested: KProperty<T?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, NOT_EQUALS_UNACCENTED_LOWER)).applyNested(nested)

fun <ID : Serializable, K : Any, T : Neo4JNode<ID>> like(
    property: KProperty<K?>,
    nested: KProperty<T?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, LIKE)).applyNested(nested)

fun <ID : Serializable, K : Any, T : Neo4JNode<ID>> notLike(
    property: KProperty<K?>,
    nested: KProperty<T?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, NOT_LIKE)).applyNested(nested)

fun <ID : Serializable, K : Any, T : Neo4JNode<ID>> likeUnaccentedLower(
    property: KProperty<K?>,
    nested: KProperty<T?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, LIKE_UNACCENTED_LOWER)).applyNested(nested)

fun <ID : Serializable, K : Any, T : Neo4JNode<ID>> notLikeUnaccentedLower(
    property: KProperty<K?>,
    nested: KProperty<T?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, NOT_LIKE_UNACCENTED_LOWER)).applyNested(nested)

fun <ID : Serializable, K : Any, T : Neo4JNode<ID>> startsWith(
    property: KProperty<K?>,
    nested: KProperty<T?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, STARTS_WITH)).applyNested(nested)

fun <ID : Serializable, K : Any, T : Neo4JNode<ID>> notStartsWith(
    property: KProperty<K?>,
    nested: KProperty<T?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, NOT_STARTS_WITH)).applyNested(nested)

fun <ID : Serializable, K : Any, T : Neo4JNode<ID>> startsWithUnaccentedLower(
    property: KProperty<K?>,
    nested: KProperty<T?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, STARTS_WITH_UNACCENTED_LOWER)).applyNested(nested)

fun <ID : Serializable, K : Any, T : Neo4JNode<ID>> notStartsWithUnaccentedLower(
    property: KProperty<K?>,
    nested: KProperty<T?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, NOT_STARTS_WITH_UNACCENTED_LOWER)).applyNested(nested)

fun <ID : Serializable, K : Any, T : Neo4JNode<ID>> endsWith(
    property: KProperty<K?>,
    nested: KProperty<T?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, ENDS_WITH)).applyNested(nested)

fun <ID : Serializable, K : Any, T : Neo4JNode<ID>> notEndsWith(
    property: KProperty<K?>,
    nested: KProperty<T?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, NOT_ENDS_WITH)).applyNested(nested)

fun <ID : Serializable, K : Any, T : Neo4JNode<ID>> endsWithUnaccentedLower(
    property: KProperty<K?>,
    nested: KProperty<T?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, ENDS_WITH_UNACCENTED_LOWER)).applyNested(nested)

fun <ID : Serializable, K : Any, T : Neo4JNode<ID>> notEndsWithUnaccentedLower(
    property: KProperty<K?>,
    nested: KProperty<T?>,
    value: String,
): Filter = Filter(property.nodePropertyName(), GenericFilterFunction(value, NOT_ENDS_WITH_UNACCENTED_LOWER)).applyNested(nested)
