package com.thomas.management.data.exposed.mapping

import java.util.UUID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Join
import org.jetbrains.exposed.sql.ReferenceOption.RESTRICT
import org.jetbrains.exposed.sql.innerJoin

@Suppress("MagicNumber")
object GroupTable : ManagementTable("group") {

    val groupName = varchar("group_name", 250).uniqueIndex("un_group_group_name")

    val groupDescription = varchar("group_description", 250).nullable()

    val isActive = bool("is_active")

    val creatorId = reference("creator_id", UserTable, RESTRICT, RESTRICT, "fk_group_user")

    fun joined(): Join =
        this.innerJoin(UserTable, { UserTable.id }, { creatorId })

}

class GroupExposedEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<GroupExposedEntity>(GroupTable)

    var groupName by GroupTable.groupName

    var groupDescription by GroupTable.groupDescription

    var isActive by GroupTable.isActive

    var createdAt by GroupTable.createdAt

    var updatedAt by GroupTable.updatedAt

    var creatorId by GroupTable.creatorId

    val groupCreator by UserExposedEntity referencedOn GroupTable.creatorId

    val roleList by GroupRoleExposedEntity referrersOn GroupRoleTable.groupId

    val groupUsers by UserExposedEntity via UserGroupTable

}
