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
    var groupUnits: List<GroupUnitNode>?,
) : Neo4JNode<UUID>
