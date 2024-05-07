package com.thomas.exposed.model.auditable

import java.util.UUID
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextInt
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable

fun createNonAuditableEntity() = NonAuditableEntity.new(randomUUID()) {
    stringValue = randomUUID().toString()
    intValue = nextInt(50, 100)
}

class NonAuditableEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<NonAuditableEntity>(NonAuditableTable)

    var stringValue by NonAuditableTable.stringValue
    var intValue by NonAuditableTable.intValue

}

object NonAuditableTable : UUIDTable("common.non_auditable") {

    val stringValue = varchar("string_value", 250)
    val intValue = integer("int_value")

}
