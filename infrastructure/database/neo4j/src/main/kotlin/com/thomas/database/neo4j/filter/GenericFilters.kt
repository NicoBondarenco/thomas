package com.thomas.database.neo4j.filter

import com.thomas.core.extension.withSessionContextIO
import com.thomas.database.neo4j.function.ConditionalFilterFunction
import com.thomas.database.neo4j.function.GenericFilterFunction
import com.thomas.database.neo4j.node.Neo4JNode
import com.thomas.database.neo4j.operator.InOperator.IN
import com.thomas.database.neo4j.operator.InOperator.IN_INCLUDE_NULL
import com.thomas.database.neo4j.operator.InOperator.NOT_IN
import com.thomas.database.neo4j.operator.InOperator.NOT_IN_INCLUDE_NULL
import java.io.Serializable
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaField
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.cypher.BooleanOperator
import org.neo4j.ogm.cypher.ComparisonOperator.EQUALS
import org.neo4j.ogm.cypher.ComparisonOperator.IS_NULL
import org.neo4j.ogm.cypher.Filter
import org.neo4j.ogm.cypher.Filters
import org.neo4j.ogm.session.SessionFactory

fun <T : Any> equals(
    property: KProperty<T?>,
    value: Any,
): Filter = Filter(property.nodePropertyName(), EQUALS, value)

fun <T : Any> notEquals(
    property: KProperty<T?>,
    value: Any,
): Filter = equals(property, value).apply { isNegated = true }

fun <T : Any> isNull(
    property: KProperty<T?>,
): Filter = Filter(property.nodePropertyName(), IS_NULL)

fun <T : Any> isNotNull(
    property: KProperty<T?>,
): Filter = isNull(property).apply { isNegated = true }

fun <T : Any> inValues(
    property: KProperty<T?>,
    values: Collection<Any?>,
): Filter = (values.contains(null).takeIf { it }?.let { IN_INCLUDE_NULL } ?: IN).let {
    Filter(property.nodePropertyName(), GenericFilterFunction(values, it))
}

fun <T : Any> notInValues(
    property: KProperty<T?>,
    values: Collection<Any?>,
): Filter = (values.contains(null).takeIf { it }?.let { NOT_IN_INCLUDE_NULL } ?: NOT_IN).let {
    Filter(property.nodePropertyName(), GenericFilterFunction(values, it))
}

fun <K : Any, T : Neo4JNode> equals(
    property: KProperty<K?>,
    nestedProperty: KProperty<T?>,
    value: Any,
): Filter = Filter(property.nodePropertyName(), EQUALS, value).applyNested(nestedProperty)

fun <K : Any, T : Neo4JNode> notEquals(
    property: KProperty<K?>,
    nestedProperty: KProperty<T?>,
    value: Any,
): Filter = equals(property, value).apply { isNegated = true }.applyNested(nestedProperty)

fun <K : Any, T : Neo4JNode> isNull(
    property: KProperty<K?>,
    nestedProperty: KProperty<T?>,
): Filter = Filter(property.nodePropertyName(), IS_NULL).applyNested(nestedProperty)

fun <K : Any, T : Neo4JNode> isNotNull(
    property: KProperty<K?>,
    nestedProperty: KProperty<T?>,
): Filter = isNull(property).apply { isNegated = true }.applyNested(nestedProperty)

fun <K : Any, T : Neo4JNode> inValues(
    property: KProperty<K?>,
    nestedProperty: KProperty<T?>,
    values: Collection<Any?>,
): Filter = (values.contains(null).takeIf { it }?.let { IN_INCLUDE_NULL } ?: IN).let {
    Filter(property.nodePropertyName(), GenericFilterFunction(values, it)).applyNested(nestedProperty)
}

fun <K : Any, T : Neo4JNode> notInValues(
    property: KProperty<K?>,
    nestedProperty: KProperty<T?>,
    values: Collection<Any?>,
): Filter = (values.contains(null).takeIf { it }?.let { NOT_IN_INCLUDE_NULL } ?: NOT_IN).let {
    Filter(property.nodePropertyName(), GenericFilterFunction(values, it)).applyNested(nestedProperty)
}

fun or(
    vararg filters: Filter,
) = Filter("", ConditionalFilterFunction(filters.toList(), BooleanOperator.OR))

fun and(
    filters: List<Filter> = listOf(),
) = Filters().apply {
    filters.forEach { filter -> this.and(filter) }
}

suspend inline fun <reified T : Neo4JNode> SessionFactory.one(
    id: Serializable,
): T? = withSessionContextIO {
    this@one.openSession().load(T::class.java, id)
}

fun <T : Any> KProperty<T?>.nodePropertyName() = this.javaField?.takeIf {
    it.isAnnotationPresent(Property::class.java)
}?.getAnnotation(Property::class.java)?.name ?: this.name

fun <T : Neo4JNode> Filter.applyNested(
    property: KProperty<T?>
): Filter = this.apply {
    nestedPropertyType = property.javaField!!.type
    nestedPropertyName = property.name
    setNestedPath(Filter.NestedPathSegment(property.name, property.javaField!!.type))
}
