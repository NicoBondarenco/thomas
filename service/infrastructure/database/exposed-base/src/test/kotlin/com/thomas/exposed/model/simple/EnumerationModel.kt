package com.thomas.exposed.model.simple

import com.thomas.exposed.table.enum
import com.thomas.exposed.table.nullableEnum
import java.util.UUID
import java.util.UUID.randomUUID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable

fun createEnumerationEntity(
    id: UUID = randomUUID(),
    valueEnum: EnumerationValue = EnumerationValue.entries.random(),
    nullableEnum: EnumerationValue? = EnumerationValue.entries.random(),
) = EnumerationEntity.new(id) {
    this.valueEnum = valueEnum
    this.nullableEnum = nullableEnum
}

class EnumerationEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<EnumerationEntity>(EnumerationTable)

    var valueEnum by EnumerationTable.valueEnum
    var nullableEnum by EnumerationTable.nullableEnum

}

object EnumerationTable : UUIDTable("simple.enumerated") {

    val valueEnum = varchar("value_enum", 250).enum<EnumerationValue>()
    val nullableEnum = varchar("nullable_enum", 250).nullable().nullableEnum<EnumerationValue>()

}

enum class EnumerationValue {
    VALUE_ONE,
    VALUE_TWO,
}
