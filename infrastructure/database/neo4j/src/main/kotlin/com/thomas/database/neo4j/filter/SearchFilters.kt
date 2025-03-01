package com.thomas.database.neo4j.filter

import com.thomas.core.extension.withSessionContextIO
import com.thomas.core.model.pagination.PageRequestData
import com.thomas.core.model.pagination.PageResponse
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection
import com.thomas.database.neo4j.node.Neo4JNode
import org.neo4j.ogm.cypher.Filter
import org.neo4j.ogm.cypher.query.Pagination
import org.neo4j.ogm.cypher.query.SortOrder
import org.neo4j.ogm.session.SessionFactory
import org.neo4j.ogm.session.count

suspend inline fun <reified T : Neo4JNode> SessionFactory.list(
    filters: List<Filter> = listOf(),
    sorts: List<PageSort> = listOf(),
): List<T> = withSessionContextIO {
    this@list.openSession().loadAll(
        T::class.java,
        and(filters),
        sorts.toSortOrder(),
    ).toList()
}

suspend inline fun <reified T : Neo4JNode> SessionFactory.count(
    filters: List<Filter> = listOf(),
): Long = withSessionContextIO {
    this@count.openSession().count(
        T::class.java,
        and(filters),
    )
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