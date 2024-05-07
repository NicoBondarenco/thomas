package com.thomas.exposed.model.auditable

import java.util.UUID
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextInt
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable

fun createSearchableEntity() = SearchableEntity.new(randomUUID()) {
    stringValue = randomUUID().toString()
    intValue = nextInt(50, 100)
}

class SearchableEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<SearchableEntity>(SearchableTable)

    var stringValue by SearchableTable.stringValue
    var intValue by SearchableTable.intValue

}

object SearchableTable : UUIDTable("common.searchable") {

    val stringValue = varchar("string_value", 250)
    val intValue = integer("int_value")

}

data class SearchableData(
    val id: UUID,
    val stringValue: String,
    val intValue: Int,
)

fun SearchableEntity.toData() = SearchableData(
    id = this.id.value,
    stringValue = this.stringValue,
    intValue = this.intValue,
)
