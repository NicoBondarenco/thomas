package com.thomas.management.data.exposed.mapping

import java.util.UUID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption.RESTRICT

object UserGroupTable : ManagementTable("user_group") {

    val userId = reference("user_id", UserTable, RESTRICT, RESTRICT, "fk_user_group_user").index("dx_user_group_user_id")

    val groupId = reference("group_id", GroupTable, RESTRICT, RESTRICT, "fk_user_group_group").index("dx_user_group_group_id")

    init {
        uniqueIndex("un_user_group_user_id_group_id", userId, groupId)
    }

}

class UserGroupExposedEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<UserGroupExposedEntity>(UserGroupTable)

    var userId by UserGroupTable.userId

    var groupId by UserGroupTable.groupId

    var createdAt by UserGroupTable.createdAt

    var updatedAt by UserGroupTable.updatedAt

    val userEntity by UserExposedEntity referencedOn UserGroupTable.userId

    val groupEntity by GroupExposedEntity referencedOn UserGroupTable.groupId

}
