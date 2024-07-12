package com.thomas.management.data.exposed.mapping

import com.thomas.core.model.security.SecurityRole
import com.thomas.exposed.table.enum
import java.util.UUID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption.RESTRICT

@Suppress("MagicNumber")
object GroupRoleTable : ManagementTable("group_role") {

    val groupId = reference("group_id", GroupTable, RESTRICT, RESTRICT, "fk_group_role_group").index("dx_group_role_group_id")

    val roleAuthority = varchar("role_authority", 250).index("dx_group_role_role_authority")

    init {
        uniqueIndex(customIndexName = "un_group_role_group_id_role_authority", groupId, roleAuthority)
    }

}

class GroupRoleExposedEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<GroupRoleExposedEntity>(GroupRoleTable)

    var groupId by GroupRoleTable.groupId

    var roleAuthority by GroupRoleTable.roleAuthority.enum<SecurityRole>()

    var createdAt by GroupRoleTable.createdAt

    var updatedAt by GroupRoleTable.updatedAt

    val groupEntity by GroupExposedEntity referencedOn GroupRoleTable.groupId

}
