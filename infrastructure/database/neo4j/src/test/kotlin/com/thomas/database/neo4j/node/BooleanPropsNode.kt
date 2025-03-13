package com.thomas.database.neo4j.node

import com.thomas.database.neo4j.converter.UUIDConverter
import java.util.UUID
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.typeconversion.Convert

@NoArgsConstructor
@NodeEntity(value = "BooleanProps")
data class BooleanPropsNode(

    @Id
    @Property(name = "id")
    @Convert(UUIDConverter::class)
    override var id: UUID,

    @Property(name = "prop_boolean")
    var propBoolean: Boolean

) : Neo4JNode<UUID>
