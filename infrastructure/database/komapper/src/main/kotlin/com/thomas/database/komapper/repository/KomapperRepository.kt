package com.thomas.database.komapper.repository

import com.thomas.core.model.entity.BaseEntity
import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
import com.thomas.core.model.pagination.PageSort
import com.thomas.core.model.pagination.PageSortDirection
import com.thomas.database.komapper.extension.readTransaction
import com.thomas.database.komapper.extension.writeTransaction
import java.io.Serializable
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.metamodel.EntityMetamodel
import org.komapper.core.dsl.metamodel.PropertyMetamodel
import org.komapper.core.dsl.operator.asc
import org.komapper.core.dsl.operator.count
import org.komapper.core.dsl.operator.desc
import org.komapper.core.dsl.query.EntitySelectQuery
import org.komapper.core.dsl.query.firstOrNull
import org.komapper.r2dbc.R2dbcDatabase

abstract class KomapperRepository<E : BaseEntity<E>, I : Serializable, M : EntityMetamodel<E, I, M>>(
    protected val metamodel: M,
    protected val database: R2dbcDatabase
) {

    open suspend fun id(id: I): E? = database.runQuery(
        QueryDsl.from(metamodel).where {
            (metamodel.idProperties().first() as PropertyMetamodel<E, I, I>) eq id
        }.firstOrNull()
    )

    open suspend fun findAll(): List<E> = database.runQuery(
        QueryDsl.from(metamodel)
    )

    open suspend fun insert(entity: E): E = database.writeTransaction {
        database.runQuery { QueryDsl.insert(metamodel).single(entity) }
    }

    open suspend fun update(entity: E): E = database.writeTransaction {
        database.runQuery { QueryDsl.update(metamodel).single(entity) }
    }

    open suspend fun delete(id: I) = database.writeTransaction {
        database.runQuery(
            QueryDsl.delete(metamodel).where {
                (metamodel.idProperties().first() as PropertyMetamodel<E, I, I>) eq id
            }
        )
    }

    protected suspend fun paged(
        query: EntitySelectQuery<E>,
        pageable: PageRequest,
    ): PageResponse<E> = database.readTransaction {
        val entities = database.runQuery(
            query.orderBy(pageable.komapperSort())
                .offset(((pageable.pageNumber - 1) * pageable.pageSize).toInt())
                .limit(pageable.pageSize.toInt())
        )
        val total = database.runQuery(query.select(count()))!!
        PageResponse.of(entities, pageable, total)
    }

    protected fun PageRequest.komapperSort() = this.pageSort.komapperSort()

    protected fun List<PageSort>.komapperSort() = this.mapNotNull {
        it.toKomapperSort()
    }

    protected fun PageSort.toKomapperSort() = metamodel.properties().firstOrNull {
        it.columnName == this.sortField
    }?.let {
        this.sortDirection.toKomapperDirection(it)
    }

    private fun PageSortDirection.toKomapperDirection(
        property: PropertyMetamodel<E, *, *>
    ) = when (this) {
        PageSortDirection.ASC -> property.asc()
        PageSortDirection.DESC -> property.desc()
    }

}
