package com.thomas.mongodb.extension

import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.descending
import com.thomas.core.model.pagination.PageRequestData
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection.ASC
import org.bson.conversions.Bson

fun PageRequestData.orderBy(): Bson = this.pageSort.toOrderBy()

private fun List<PageSort>.toOrderBy(): Bson = Sorts.orderBy(this.map { it.toSort() })

private fun PageSort.toSort(): Bson = if (this.sortDirection == ASC) {
    ascending(this.sortField)
} else {
    descending(this.sortField)
}