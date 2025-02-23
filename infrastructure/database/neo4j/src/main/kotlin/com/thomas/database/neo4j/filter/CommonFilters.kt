package com.thomas.database.neo4j.filter

import com.thomas.core.extension.withSessionContextIO
import com.thomas.core.model.pagination.PageRequestData
import com.thomas.core.model.pagination.PageResponse
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection
import com.thomas.database.neo4j.function.ConditionalFilterFunction
import com.thomas.database.neo4j.function.GenericFilterFunction
import com.thomas.database.neo4j.node.Neo4JNode
import com.thomas.database.neo4j.operator.InOperator.IN
import com.thomas.database.neo4j.operator.InOperator.IN_INCLUDE_NULL
import com.thomas.database.neo4j.operator.InOperator.NOT_IN
import com.thomas.database.neo4j.operator.InOperator.NOT_IN_INCLUDE_NULL
import java.io.Serializable
import org.neo4j.ogm.cypher.BooleanOperator
import org.neo4j.ogm.cypher.ComparisonOperator.EQUALS
import org.neo4j.ogm.cypher.ComparisonOperator.IS_NULL
import org.neo4j.ogm.cypher.Filter
import org.neo4j.ogm.cypher.Filters
import org.neo4j.ogm.cypher.query.Pagination
import org.neo4j.ogm.cypher.query.SortOrder
import org.neo4j.ogm.session.SessionFactory
import org.neo4j.ogm.session.count

fun equals(
    property: String,
    value: Any,
): Filter = Filter(property, EQUALS, value)

fun notEquals(
    property: String,
    value: Any,
): Filter = equals(property, value).apply { isNegated = true }

fun isNull(
    property: String,
): Filter = Filter(property, IS_NULL)

fun isNotNull(
    property: String,
): Filter = isNull(property).apply { isNegated = true }

fun inValues(
    property: String,
    values: Collection<Any?>,
): Filter = Filter(property, GenericFilterFunction(values, (values.contains(null).takeIf { it }?.let { IN_INCLUDE_NULL } ?: IN)))

fun notInValues(
    property: String,
    values: Collection<Any?>,
): Filter = Filter(property, GenericFilterFunction(values, (values.contains(null).takeIf { it }?.let { NOT_IN_INCLUDE_NULL } ?: NOT_IN)))

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

suspend inline fun <reified T : Neo4JNode> SessionFactory.list(
    filters: List<Filter> = listOf(),
    sorts: List<PageSort>,
): List<T> = withSessionContextIO {
    this@list.openSession().loadAll(
        T::class.java,
        and(filters),
        sorts.toSortOrder(),
    ).toList()
}

suspend inline fun <reified T : Neo4JNode> SessionFactory.page(
    filters: List<Filter> = listOf(),
    pagination: PageRequestData,
): PageResponse<T> = withSessionContextIO {
    val result: List<T> = this@page.openSession().loadAll(
        T::class.java,
        and(filters),
        pagination.toSortOrder(),
        pagination.toPagination()
    ).toList()
    val total = this@page.openSession().count<T>(and(filters))
    PageResponse.of(result, pagination, total)
}

fun PageRequestData.toPagination(): Pagination = Pagination(
    this.pageNumber.toInt().minus(1),
    this.pageSize.toInt()
)

fun PageRequestData.toSortOrder(): SortOrder = this.pageSort.toSortOrder()

fun List<PageSort>.toSortOrder(): SortOrder = SortOrder().apply {
    this@toSortOrder.forEach {
        this.add(it.sortDirection.toSortOrderDirection(), it.sortField)
    }
}

fun PageSort.toSortOrder(): SortOrder = SortOrder(
    sortDirection.toSortOrderDirection(),
    sortField
)

fun PageSortDirection.toSortOrderDirection(): SortOrder.Direction = when (this) {
    PageSortDirection.ASC -> SortOrder.Direction.ASC
    PageSortDirection.DESC -> SortOrder.Direction.DESC
}
