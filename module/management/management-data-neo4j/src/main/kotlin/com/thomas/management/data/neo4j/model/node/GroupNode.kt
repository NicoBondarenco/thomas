package com.thomas.management.data.neo4j.model.node

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.thomas.database.neo4j.converter.UUIDConverter
import com.thomas.database.neo4j.node.Neo4JNode
import com.thomas.database.neo4j.node.NoArgsConstructor
import java.time.ZonedDateTime
import java.util.UUID
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.annotation.Relationship.Direction.OUTGOING
import org.neo4j.ogm.annotation.typeconversion.Convert

@NoArgsConstructor
@NodeEntity(value = "Group")
data class GroupNode(

    @Id
    @Property(name = "id")
    @Convert(UUIDConverter::class)
    override var id: UUID,

    @Property(name = "group_name")
    var groupName: String,

    @Property(name = "group_description")
    var groupDescription: String?,

    @JsonIgnoreProperties("groupNode")
    @Relationship(type = "GROUP_BELONGS_TO_ORGANIZATION", direction = OUTGOING)
    var groupOrganization: GroupOrganizationNode,

    @Property(name = "is_active")
    var isActive: Boolean,

    @Property(name = "created_at")
    var createdAt: ZonedDateTime,

    @Property(name = "updated_at")
    var updatedAt: ZonedDateTime,

    @JsonIgnoreProperties("groupNode")
    @Relationship(type = "GROUP_ALLOWED_IN_UNIT", direction = OUTGOING)
    var groupUnits: List<GroupUnitNode>?

) : Neo4JNode<UUID> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GroupNode

        if (id != other.id) return false
        if (groupName != other.groupName) return false
        if (groupDescription != other.groupDescription) return false
        if (groupOrganization.organizationId != other.groupOrganization.organizationId) return false
        if (isActive != other.isActive) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isActive.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + groupName.hashCode()
        result = 31 * result + (groupDescription?.hashCode() ?: 0)
        result = 31 * result + groupOrganization.id.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + updatedAt.hashCode()
        return result
    }

    override fun toString(): String {
        return "GroupNode(id=$id, " +
                "groupName='$groupName', " +
                "groupDescription=$groupDescription, " +
                "groupOrganization=${groupOrganization.organizationId}, " +
                "isActive=$isActive, " +
                "createdAt=$createdAt, " +
                "updatedAt=$updatedAt, " +
                "groupUnits=$groupUnits)"
    }


}
