package com.thomas.core.coroutines.model.pagination

import com.thomas.core.model.pagination.PageRequestData
import com.thomas.core.model.pagination.PageSort
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

data class PageFlowResponse<T>(
    val contentList: Flow<T>,
    val totalItems: Long,
    val totalPages: Long,
    val pageNumber: Long,
    val pageSize: Long,
    val firstPage: Boolean,
    val lastPage: Boolean,
    val sortedBy: List<PageSort>
) {

    companion object {
        fun <T> of(contentList: Flow<T>, pageable: PageRequestData, totalItems: Long): PageFlowResponse<T> {
            val addPage = (totalItems % pageable.pageSize).takeIf { it == 0L } ?: 1L
            val totalPages = (totalItems / pageable.pageSize) + addPage

            return PageFlowResponse(
                contentList,
                totalItems,
                totalPages,
                pageable.pageNumber,
                pageable.pageSize,
                pageable.pageNumber == 1L,
                (pageable.pageNumber == totalPages) || (totalPages == 0L),
                pageable.pageSort
            )
        }
    }

    suspend fun <E> map(map: suspend (T) -> E) = coroutineScope {
        PageFlowResponse(
            contentList.transform { emit(map(it)) },
            totalItems,
            totalPages,
            pageNumber,
            pageSize,
            firstPage,
            lastPage,
            sortedBy
        )
    }

}
