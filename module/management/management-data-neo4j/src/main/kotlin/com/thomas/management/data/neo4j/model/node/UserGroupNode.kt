package com.thomas.management.data.neo4j.model.node

import com.fasterxml.jackson.annotation.JsonIgnore
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
@RelationshipEntity(type = "USER_IN_GROUP")
data class UserGroupNode(

    @Id
    @Property(name = "id")
    @Convert(UUIDConverter::class)
    override var id: UUID,

    @Property(name = "user_id")
    @Convert(UUIDConverter::class)
    var userId: UUID,

    @Property(name = "group_id")
    @Convert(UUIDConverter::class)
    var groupId: UUID,

    @StartNode
    @JsonIgnore
    var userNode: UserNode? = null,

    @EndNode
    var groupNode: GroupNode

) : Neo4JNode<UUID> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserGroupNode

        if (id != other.id) return false
        if (userId != other.userId) return false
        if (groupId != other.groupId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + groupId.hashCode()
        return result
    }

    override fun toString(): String {
        return "UserUnitNode(id='$id', userId=$userId, groupId=$groupId)"
    }

}