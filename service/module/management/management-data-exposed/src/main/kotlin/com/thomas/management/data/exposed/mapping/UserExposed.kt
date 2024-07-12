package com.thomas.management.data.exposed.mapping

import com.thomas.core.model.general.Gender
import com.thomas.exposed.table.nullableEnum
import java.util.UUID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Join
import org.jetbrains.exposed.sql.ReferenceOption.RESTRICT
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.javatime.date

@Suppress("MagicNumber")
object UserTable : ManagementTable("user") {

    val firstName = varchar("first_name", 250).index("dx_user_first_name")

    val lastName = varchar("last_name", 250).index("dx_user_last_name")

    val mainEmail = varchar("main_email", 250).uniqueIndex("un_user_main_email")

    val documentNumber = varchar("document_number", 250).uniqueIndex("un_user_document_number")

    val phoneNumber = varchar("phone_number", 250).nullable()

    val profilePhoto = text("profile_photo", eagerLoading = true).nullable()

    val birthDate = date("birth_date").nullable()

    val userGender = varchar("user_gender", 250).nullable()

    val isActive = bool("is_active").default(true)

    val creatorId = reference("creator_id", UserTable, RESTRICT, RESTRICT, "fk_user_creator").index("dx_user_creator_id")

    val creatorAlias = UserTable.alias("creator")

    fun joined(): Join =
        this.innerJoin(creatorAlias, { creatorId }, { creatorAlias[UserTable.id] })

}

class UserExposedEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<UserExposedEntity>(UserTable)

    var firstName by UserTable.firstName

    var lastName by UserTable.lastName

    var mainEmail by UserTable.mainEmail

    var documentNumber by UserTable.documentNumber

    var phoneNumber by UserTable.phoneNumber

    var profilePhoto by UserTable.profilePhoto

    var birthDate by UserTable.birthDate

    var userGender by UserTable.userGender.nullableEnum<Gender>()

    var isActive by UserTable.isActive

    var creatorId by UserTable.creatorId

    var createdAt by UserTable.createdAt

    var updatedAt by UserTable.updatedAt

    val roleList by UserRoleExposedEntity referrersOn UserRoleTable.userId

    val userGroups by GroupExposedEntity via UserGroupTable

}
