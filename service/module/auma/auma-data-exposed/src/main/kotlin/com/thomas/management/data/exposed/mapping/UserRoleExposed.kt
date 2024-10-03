package com.thomas.management.data.exposed.mapping

import com.thomas.core.model.security.SecurityRole
import com.thomas.exposed.table.enum
import java.util.UUID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption.RESTRICT

@Suppress("MagicNumber")
object UserRoleTable : ManagementTable("user_role") {

    val userId = reference("user_id", UserTable, RESTRICT, RESTRICT, "fk_user_role_user").index("dx_user_role_user_id")

    val roleAuthority = varchar("role_authority", 250).index("dx_user_role_role_authority")

    init {
        uniqueIndex(customIndexName = "un_user_role_user_id_role_authority", userId, roleAuthority)
    }

}

class UserRoleExposedEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<UserRoleExposedEntity>(UserRoleTable)

    var userId by UserRoleTable.userId

    var roleAuthority by UserRoleTable.roleAuthority.enum<SecurityRole>()

    var createdAt by UserRoleTable.createdAt

    var updatedAt by UserRoleTable.updatedAt

}
