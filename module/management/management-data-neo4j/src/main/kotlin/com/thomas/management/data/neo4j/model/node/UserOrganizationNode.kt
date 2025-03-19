package com.thomas.management.data.neo4j.model.node

import com.fasterxml.jackson.annotation.JsonIgnore
import com.thomas.core.model.security.SecurityOrganizationRole
import com.thomas.database.neo4j.converter.UUIDConverter
import com.thomas.database.neo4j.node.Neo4JNode
import com.thomas.database.neo4j.node.NoArgsConstructor
import java.util.UUID
import org.neo4j.ogm.annotation.EndNode
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.RelationshipEntity
import org.neo4j.ogm.annotation.StartNode
import org.neo4j.ogm.annotation.typeconversion.Convert

@NoArgsConstructor
@RelationshipEntity(type = "USER_BELONGS_TO_ORGANIZATION")
data class UserOrganizationNode(

    @Id
    @Property(name = "id")
    @Convert(UUIDConverter::class)
    override var id: UUID,

    @Property(name = "user_id")
    @Convert(UUIDConverter::class)
    var userId: UUID,

    @Property(name = "organization_id")
    @Convert(UUIDConverter::class)
    var organizationId: UUID,

    @Property(name = "user_roles")
    var userRoles: Set<SecurityOrganizationRole>,

    @StartNode
    @JsonIgnore
    var userNode: UserNode? = null,

    @EndNode
    var organizationNode: OrganizationNode

) : Neo4JNode<UUID> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserOrganizationNode

        if (id != other.id) return false
        if (userId != other.userId) return false
        if (organizationId != other.organizationId) return false
        if (userRoles != other.userRoles) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + organizationId.hashCode()
        result = 31 * result + userRoles.hashCode()
        return result
    }

    override fun toString(): String {
        return "UserOrganizationNode(id='$id', userId=$userId, organizationId=$organizationId, userRoles=$userRoles, organizationNode=$organizationNode)"
    }

}