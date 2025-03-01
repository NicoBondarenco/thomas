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
@NodeEntity(value = "DatetimeNestedProps")
data class DatetimeNestedPropsNode(

    @Id
    @Property(name = "id")
    @Convert(UUIDConverter::class)
    override var id: UUID,

    @Property(name = "prop_name")
    var propName: String,

    @Relationship(type = "DATETIME_NESTED", direction = OUTGOING)
    val nestedNode: DatetimePropsNode

) : Neo4JNode
