package com.thomas.management.data.neo4j.model.node

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.thomas.core.model.general.Gender
import com.thomas.database.neo4j.converter.UUIDConverter
import com.thomas.database.neo4j.node.Neo4JNode
import com.thomas.database.neo4j.node.NoArgsConstructor
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.UUID
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.annotation.Relationship.Direction.OUTGOING
import org.neo4j.ogm.annotation.typeconversion.Convert

@NoArgsConstructor
@NodeEntity(value = "User")
data class UserNode(

    @Id
    @Property(name = "id")
    @Convert(UUIDConverter::class)
    override var id: UUID,

    @Property(name = "first_name")
    var firstName: String,

    @Property(name = "last_name")
    var lastName: String,

    @Property(name = "document_number")
    var documentNumber: String,

    @Property(name = "profile_photo")
    var profilePhoto: String?,

    @Property(name = "user_gender")
    var userGender: Gender?,

    @Property(name = "birth_date")
    var birthDate: LocalDate?,

    @Property(name = "password_salt")
    var passwordSalt: String,

    @Property(name = "password_hash")
    var passwordHash: String,

    @Property(name = "main_email")
    var mainEmail: String,

    @Property(name = "main_phone")
    var mainPhone: String,

    @Property(name = "is_active")
    var isActive: Boolean,

    @Property(name = "created_at")
    var createdAt: ZonedDateTime,

    @Property(name = "updated_at")
    var updatedAt: ZonedDateTime,

    @JsonIgnoreProperties("userNode")
    @Relationship(type = "USER_BELONGS_TO_ORGANIZATION", direction = OUTGOING)
    var userOrganization: UserOrganizationNode,

    @JsonIgnoreProperties("userNode")
    @Relationship(type = "USER_ALLOWED_IN_UNIT", direction = OUTGOING)
    var userUnits: Set<UserUnitNode>?,

    @Relationship(type = "USER_IN_GROUP", direction = OUTGOING)
    var userGroups: Set<UserGroupNode>?

) : Neo4JNode<UUID> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserNode

        if (id != other.id) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (documentNumber != other.documentNumber) return false
        if (profilePhoto != other.profilePhoto) return false
        if (userGender != other.userGender) return false
        if (birthDate != other.birthDate) return false
        if (passwordSalt != other.passwordSalt) return false
        if (passwordHash != other.passwordHash) return false
        if (mainEmail != other.mainEmail) return false
        if (mainPhone != other.mainPhone) return false
        if (isActive != other.isActive) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false
        if (userOrganization.organizationId != other.userOrganization.organizationId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isActive.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + documentNumber.hashCode()
        result = 31 * result + (profilePhoto?.hashCode() ?: 0)
        result = 31 * result + (userGender?.hashCode() ?: 0)
        result = 31 * result + (birthDate?.hashCode() ?: 0)
        result = 31 * result + passwordSalt.hashCode()
        result = 31 * result + passwordHash.hashCode()
        result = 31 * result + mainEmail.hashCode()
        result = 31 * result + mainPhone.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + updatedAt.hashCode()
        result = 31 * result + userOrganization.organizationId.hashCode()
        return result
    }

    override fun toString(): String {
        return "UserNode(" +
                "id=$id, " +
                "firstName='$firstName', " +
                "lastName='$lastName', " +
                "documentNumber='$documentNumber', " +
                "profilePhoto=$profilePhoto, " +
                "userGender=$userGender, " +
                "birthDate=$birthDate, " +
                "passwordSalt='$passwordSalt', " +
                "passwordHash='$passwordHash', " +
                "mainEmail='$mainEmail', " +
                "mainPhone='$mainPhone', " +
                "isActive=$isActive, " +
                "createdAt=$createdAt, " +
                "updatedAt=$updatedAt, " +
                "userOrganization=${userOrganization.organizationId}, " +
                "userUnits=$userUnits, " +
                "userGroups=$userGroups" +
                ")"
    }


}
