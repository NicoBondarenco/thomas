package com.thomas.database.komapper.repository

import com.thomas.core.extension.unaccentedLower
import com.thomas.core.model.general.Gender
import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
import com.thomas.database.komapper.definition._EntityTestDefinition
import com.thomas.database.komapper.definition.entityTestEntity
import com.thomas.database.komapper.entity.EntityTestEntity
import com.thomas.database.komapper.extension.toLike
import com.thomas.database.komapper.extension.unaccentLower
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.operator.coalesce
import org.komapper.core.dsl.operator.literal
import org.komapper.r2dbc.R2dbcDatabase

class EntityTestKomapperRepository(
    database: R2dbcDatabase
) : KomapperRepository<EntityTestEntity, UUID, _EntityTestDefinition>(
    metamodel = Meta.entityTestEntity,
    database = database,
) {

    suspend fun paged(
        entityIds: List<UUID>? = null,
        stringValue: String? = null,
        intValue: Int? = null,
        bigdecimalValue: BigDecimal? = null,
        enumValue: Gender? = null,
        createdStart: OffsetDateTime? = null,
        createdEnd: OffsetDateTime? = null,
        pageable: PageRequest = PageRequest(),
    ): PageResponse<EntityTestEntity> = paged(
        QueryDsl.from(metamodel).where {
            entityIds?.let { value ->
                and { metamodel.id inList value }
            }

            stringValue?.let { value ->
                and {
                    or { unaccentLower(metamodel.stringRequired) like value.unaccentedLower().toLike() }
                    or { unaccentLower(coalesce(metamodel.stringNullable, literal(""))) like value.unaccentedLower().toLike() }
                }
            }

            intValue?.let { value ->
                and {
                    or { metamodel.integerRequired eq value }
                    or { metamodel.integerNullable eq value }
                }
            }

            bigdecimalValue?.let { value ->
                and {
                    or { metamodel.bigdecimalRequired eq value }
                    or { metamodel.bigdecimalNullable eq value }
                }
            }

            enumValue?.let { value ->
                and {
                    or { metamodel.enumerationRequired eq value }
                    or { metamodel.enumerationNullable eq value }
                }
            }

            createdStart?.let { value ->
                and { metamodel.createdAt greaterEq value }
            }

            createdEnd?.let { value ->
                and { metamodel.createdAt lessEq value }
            }

        },
        pageable
    )

}