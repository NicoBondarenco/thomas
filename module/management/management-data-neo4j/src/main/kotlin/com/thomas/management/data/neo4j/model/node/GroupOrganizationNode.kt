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
@RelationshipEntity(type = "GROUP_BELONGS_TO_ORGANIZATION")
data class GroupOrganizationNode(

    @Id
    @Property(name = "id")
    @Convert(UUIDConverter::class)
    override var id: UUID,

    @Property(name = "group_id")
    @Convert(UUIDConverter::class)
    var groupId: UUID,

    @Property(name = "organization_id")
    @Convert(UUIDConverter::class)
    var organizationId: UUID,

    @Property(name = "group_roles")
    var groupRoles: Set<SecurityOrganizationRole>,

    @EndNode
    var organizationNode: OrganizationNode

) : Neo4JNode<UUID> {

    @StartNode
    @JsonIgnore
    lateinit var groupNode: GroupNode

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GroupOrganizationNode

        if (id != other.id) return false
        if (groupId != other.groupId) return false
        if (organizationId != other.organizationId) return false
        if (groupRoles != other.groupRoles) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + groupId.hashCode()
        result = 31 * result + organizationId.hashCode()
        result = 31 * result + groupRoles.hashCode()
        return result
    }

    override fun toString(): String {
        return "GroupOrganizationNode(id=$id, groupId=$groupId, organizationId=$organizationId, groupRoles=$groupRoles)"
    }

}
