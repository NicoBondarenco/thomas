package com.thomas.exposed.model.simple

import java.util.UUID
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextInt
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable

fun createSimpleEntity(
    id: UUID = randomUUID(),
) = SimpleEntity.new(id) {
    stringValue = randomUUID().toString()
    booleanValue = nextBoolean()
    intValue = nextInt(50, 100)
}

class SimpleEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<SimpleEntity>(SimpleTable)

    var stringValue by SimpleTable.stringValue
    var booleanValue by SimpleTable.booleanValue
    var intValue by SimpleTable.intValue

}

object SimpleTable : UUIDTable("simple.simple_model") {

    val stringValue = varchar("string_value", 250)
    val booleanValue = bool("boolean_value")
    val intValue = integer("int_value")

}
