package com.thomas.exposed.table

import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection
import org.jetbrains.exposed.sql.ColumnSet
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and

fun <E> ColumnSet.list(
    predicates: List<Op<Boolean>> = listOf(),
    mapper: ((ResultRow) -> E),
): List<E> = this.query(predicates).map(mapper)

@Suppress("SpreadOperator")
fun <E> ColumnSet.page(
    pageable: PageRequest,
    predicates: List<Op<Boolean>> = listOf(),
    mapper: ((ResultRow) -> E),
): PageResponse<E> {
    val query = this.query(predicates)
    val sort = pageable.pageSort.toExposedSort(this)

    val total = query.count()
    val items = query
        .orderBy(*sort)
        .limit(pageable.take, pageable.skip)
        .map(mapper)

    return PageResponse.of(items, pageable, total)
}

private fun ColumnSet.query(
    predicates: List<Op<Boolean>>,
) = Query(this, predicates.takeIf { it.isNotEmpty() }?.reduce(Op<Boolean>::and))

private fun List<PageSort>.toExposedSort(
    table: ColumnSet
): Array<Pair<Expression<*>, SortOrder>> = this.mapNotNull { sort ->
    table.columnByName(sort.sortField)?.let {
        it to sort.sortDirection.toExposedDirection()
    }
}.toTypedArray()

private fun ColumnSet.columnByName(
    name: String
): Expression<*>? = this.columns.firstOrNull { it.name == name }

private fun PageSortDirection.toExposedDirection() = when (this) {
    PageSortDirection.ASC -> SortOrder.ASC
    PageSortDirection.DESC -> SortOrder.DESC
}

private val PageRequest.take
    get() = this.pageSize.toInt()

private val PageRequest.skip
    get() = (this.pageNumber - 1) * this.pageSize
