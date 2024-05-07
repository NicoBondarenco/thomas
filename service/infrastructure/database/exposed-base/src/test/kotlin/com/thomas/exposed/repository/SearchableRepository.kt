package com.thomas.exposed.repository

import com.thomas.core.model.pagination.PageRequest
import com.thomas.exposed.expression.toLikeParameter
import com.thomas.exposed.expression.unaccentLower
import com.thomas.exposed.model.auditable.SearchableEntity
import com.thomas.exposed.model.auditable.SearchableTable
import com.thomas.exposed.model.auditable.toData
import com.thomas.exposed.table.list
import com.thomas.exposed.table.page
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like

class SearchableRepository(
    database: Database,
) : ExposedRepository(database) {

    fun searchList(
        name: String,
        value: Int,
    ) = transacted {
        SearchableTable.list(
            listOf(
                SearchableTable.stringValue.unaccentLower() like name.toLikeParameter(),
                SearchableTable.intValue lessEq value
            )
        ) { SearchableEntity.wrapRow(it).toData() }
    }

    fun searchList() = transacted {
        SearchableTable.list { SearchableEntity.wrapRow(it).toData() }
    }

    fun searchPage(
        name: String,
        value: Int,
        pageable: PageRequest,
    ) = transacted {
        SearchableTable.page(
            pageable,
            listOf(
                SearchableTable.stringValue.unaccentLower() like name.toLikeParameter(),
                SearchableTable.intValue lessEq value
            )
        ) { SearchableEntity.wrapRow(it).toData() }
    }

    fun searchPage(
        pageable: PageRequest,
    ) = transacted {
        SearchableTable.page(
            pageable
        ) { SearchableEntity.wrapRow(it).toData() }
    }

}
