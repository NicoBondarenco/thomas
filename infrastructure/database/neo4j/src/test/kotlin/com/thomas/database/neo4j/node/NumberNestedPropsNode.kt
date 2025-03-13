package com.thomas.database.neo4j.node

import com.thomas.database.neo4j.converter.UUIDConverter
import java.util.UUID
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.annotation.Relationship.Direction.OUTGOING
import org.neo4j.ogm.annotation.typeconversion.Convert

@NoArgsConstructor
@NodeEntity(value = "NumberNestedProps")
data class NumberNestedPropsNode(

    @Id
    @Property(name = "id")
    @Convert(UUIDConverter::class)
    override var id: UUID,

    @Relationship(type = "NUMBER_NESTED", direction = OUTGOING)
    val nestedNode: NumberPropsNode

) : Neo4JNode<UUID>
