package com.thomas.exposed.model.auditable

import java.util.UUID
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextInt
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption.RESTRICT

fun createParentEntity(
    id: UUID = randomUUID(),
) = ParentEntity.new(id) {
    stringValue = randomUUID().toString()
    intValue = nextInt(50, 100)
}

fun createChildEntity(
    id: UUID = randomUUID(),
    parent: ParentEntity
) = ChildEntity.new(id) {
    stringValue = randomUUID().toString()
    intValue = nextInt(50, 100)
    childParent = parent
}

class ParentEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<ParentEntity>(ParentTable)

    var stringValue by ParentTable.stringValue
    var intValue by ParentTable.intValue

}

object ParentTable : UUIDTable("common.parent") {

    val stringValue = varchar("string_value", 250)
    val intValue = integer("int_value")

}

class ChildEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<ChildEntity>(ChildTable)

    var stringValue by ChildTable.stringValue
    var intValue by ChildTable.intValue
    var childParent by ParentEntity referencedOn ChildTable.parentId
}

object ChildTable : UUIDTable("common.child") {

    val stringValue = varchar("string_value", 250)
    val intValue = integer("int_value")
    val parentId = reference("parent_id", ParentTable, RESTRICT, RESTRICT, "fk_child_parent")

}
