package com.thomas.database.komapper.definition

import com.thomas.core.model.general.Gender
import com.thomas.database.komapper.entity.EntityTestEntity
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.util.UUID
import org.komapper.annotation.KomapperColumn
import org.komapper.annotation.KomapperEntityDef
import org.komapper.annotation.KomapperId
import org.komapper.annotation.KomapperTable

@KomapperEntityDef(EntityTestEntity::class)
@KomapperTable("entity_test", schema = "komapper", alwaysQuote = true)
data class EntityTestDefinition(
    @KomapperId
    @KomapperColumn(name = "id", alwaysQuote = true)
    val id: UUID,

    @KomapperColumn(name = "string_required", alwaysQuote = true)
    val stringRequired: String,

    @KomapperColumn(name = "string_nullable", alwaysQuote = true)
    val stringNullable: String?,

    @KomapperColumn(name = "integer_required", alwaysQuote = true)
    val integerRequired: Int,

    @KomapperColumn(name = "integer_nullable", alwaysQuote = true)
    val integerNullable: Int?,

    @KomapperColumn(name = "long_required", alwaysQuote = true)
    val longRequired: Long,

    @KomapperColumn(name = "long_nullable", alwaysQuote = true)
    val longNullable: Long?,

    @KomapperColumn(name = "double_required", alwaysQuote = true)
    val doubleRequired: Double,

    @KomapperColumn(name = "double_nullable", alwaysQuote = true)
    val doubleNullable: Double?,

    @KomapperColumn(name = "biginteger_required", alwaysQuote = true)
    val bigintegerRequired: BigInteger,

    @KomapperColumn(name = "biginteger_nullable", alwaysQuote = true)
    val bigintegerNullable: BigInteger?,

    @KomapperColumn(name = "bigdecimal_required", alwaysQuote = true)
    val bigdecimalRequired: BigDecimal,

    @KomapperColumn(name = "bigdecimal_nullable", alwaysQuote = true)
    val bigdecimalNullable: BigDecimal?,

    @KomapperColumn(name = "boolean_required", alwaysQuote = true)
    val booleanRequired: Boolean,

    @KomapperColumn(name = "boolean_nullable", alwaysQuote = true)
    val booleanNullable: Boolean?,

    @KomapperColumn(name = "enumeration_required", alwaysQuote = true)
    val enumerationRequired: Gender,

    @KomapperColumn(name = "enumeration_nullable", alwaysQuote = true)
    val enumerationNullable: Gender?,

    @KomapperColumn(name = "localdate_required", alwaysQuote = true)
    val localdateRequired: LocalDate,

    @KomapperColumn(name = "localdate_nullable", alwaysQuote = true)
    val localdateNullable: LocalDate?,

    @KomapperColumn(name = "localdatetime_required", alwaysQuote = true)
    val localdatetimeRequired: LocalDateTime,

    @KomapperColumn(name = "localdatetime_nullable", alwaysQuote = true)
    val localdatetimeNullable: LocalDateTime?,

    @KomapperColumn(name = "localtime_required", alwaysQuote = true)
    val localtimeRequired: LocalTime,

    @KomapperColumn(name = "localtime_nullable", alwaysQuote = true)
    val localtimeNullable: LocalTime?,

    @KomapperColumn(name = "offsetdatetime_required", alwaysQuote = true)
    val offsetdatetimeRequired: OffsetDateTime,

    @KomapperColumn(name = "offsetdatetime_nullable", alwaysQuote = true)
    val offsetdatetimeNullable: OffsetDateTime?,

    @KomapperColumn(name = "uuid_required", alwaysQuote = true)
    val uuidRequired: UUID,

    @KomapperColumn(name = "uuid_nullable", alwaysQuote = true)
    val uuidNullable: UUID?,

    @KomapperColumn(name = "created_at", alwaysQuote = true)
    val createdAt: OffsetDateTime,

    @KomapperColumn(name = "updated_at", alwaysQuote = true)
    val updatedAt: OffsetDateTime,
)