package com.thomas.management.data.neo4j.model.node

import com.fasterxml.jackson.annotation.JsonIgnore
import com.thomas.core.model.security.SecurityUnitRole
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
@RelationshipEntity(type = "GROUP_ALLOWED_IN_UNIT")
data class GroupUnitNode(
    @Id
    @Property(name = "id")
    @Convert(UUIDConverter::class)
    override var id: UUID,

    @Property(name = "group_id")
    @Convert(UUIDConverter::class)
    var groupId: UUID,

    @Property(name = "unit_id")
    @Convert(UUIDConverter::class)
    var unitId: UUID,

    @Property(name = "group_roles")
    var groupRoles: Set<SecurityUnitRole>,

    @StartNode
    @JsonIgnore
    var groupNode: GroupNode? = null,

    @EndNode
    var unitNode: UnitNode

) : Neo4JNode<UUID> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GroupUnitNode

        if (id != other.id) return false
        if (groupId != other.groupId) return false
        if (unitId != other.unitId) return false
        if (groupRoles != other.groupRoles) return false
        if (unitNode != other.unitNode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + groupId.hashCode()
        result = 31 * result + unitId.hashCode()
        result = 31 * result + groupRoles.hashCode()
        result = 31 * result + unitNode.hashCode()
        return result
    }

    override fun toString(): String {
        return "GroupUnitNode(id='$id', groupId=$groupId, unitId=$unitId, groupRoles=$groupRoles, unitNode=$unitNode)"
    }

}