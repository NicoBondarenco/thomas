package com.thomas.exposed.model.auditable

import com.thomas.exposed.table.ExposedTable
import com.thomas.exposed.table.enum
import com.thomas.exposed.table.nullableEnum
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.util.UUID
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextDouble
import kotlin.random.Random.Default.nextInt
import kotlin.random.Random.Default.nextLong
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.time

fun createAuditableEntity(
    id: UUID = randomUUID()
) = AuditableEntity.new(id) {
    stringValue = randomUUID().toString()
    booleanValue = nextBoolean()
    intValue = nextInt(50, 100)
    longValue = nextLong(50, 100)
    bigDecimal = BigDecimal(nextDouble(50.0, 100.0))
    dateValue = LocalDate.now()
    timeValue = LocalTime.now()
    datetimeValue = LocalDateTime.now()
    enumValue = AuditableEnum.entries.random()
    uuidEmpty = null
    stringEmpty = null
    booleanEmpty = null
    intEmpty = null
    longEmpty = null
    bigdecimalEmpty = null
    dateEmpty = null
    timeEmpty = null
    datetimeEmpty = null
    enumNull = null
    createdAt = OffsetDateTime.now()
    updatedAt = OffsetDateTime.now()
}

class AuditableEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<AuditableEntity>(AuditableTable)

    var stringValue by AuditableTable.stringValue
    var booleanValue by AuditableTable.booleanValue
    var intValue by AuditableTable.intValue
    var longValue by AuditableTable.longValue
    var bigDecimal by AuditableTable.bigDecimal
    var dateValue by AuditableTable.dateValue
    var timeValue by AuditableTable.timeValue
    var datetimeValue by AuditableTable.datetimeValue
    var enumValue by AuditableTable.enumValue.enum<AuditableEnum>()
    var uuidEmpty by AuditableTable.uuidEmpty
    var stringEmpty by AuditableTable.stringEmpty
    var booleanEmpty by AuditableTable.booleanEmpty
    var intEmpty by AuditableTable.intEmpty
    var longEmpty by AuditableTable.longEmpty
    var bigdecimalEmpty by AuditableTable.bigdecimalEmpty
    var dateEmpty by AuditableTable.dateEmpty
    var timeEmpty by AuditableTable.timeEmpty
    var datetimeEmpty by AuditableTable.datetimeEmpty
    var enumNull by AuditableTable.enumNull.nullableEnum<AuditableEnum>()
    var createdAt by AuditableTable.createdAt
    var updatedAt by AuditableTable.updatedAt
}

object AuditableTable : ExposedTable("common", "auditable") {

    val stringValue = varchar("string_value", 250)
    val booleanValue = bool("boolean_value")
    val intValue = integer("int_value")
    val longValue = long("long_value")
    val bigDecimal = decimal("big_decimal", 18, 5)
    val dateValue = date("date_value")
    val timeValue = time("time_value")
    val datetimeValue = datetime("datetime_value")
    val enumValue = varchar("enum_value", 250)
    val uuidEmpty = uuid("uuid_empty").nullable()
    val stringEmpty = varchar("string_empty", 250).nullable()
    val booleanEmpty = bool("boolean_empty").nullable()
    val intEmpty = integer("int_empty").nullable()
    val longEmpty = long("long_empty").nullable()
    val bigdecimalEmpty = decimal("bigdecimal_empty", 18, 5).nullable()
    val dateEmpty = date("date_empty").nullable()
    val timeEmpty = time("time_empty").nullable()
    val datetimeEmpty = datetime("datetime_empty").nullable()
    val enumNull = varchar("enum_null", 250).nullable()

}

enum class AuditableEnum {
    VALUE_ONE,
    VALUE_TWO,
}
